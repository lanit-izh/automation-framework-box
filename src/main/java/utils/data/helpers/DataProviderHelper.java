package utils.data.helpers;


import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import ru.lanit.at.Config;
import ru.lanit.at.citrus.CitrusRunner;
import ru.lanit.at.context.Context;
import ru.lanit.at.exceptions.FrameworkRuntimeException;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/** Вспомогательный класс для работы с ДатаПровайдером */
public class DataProviderHelper {
    private static Logger logger = LoggerFactory.getLogger(DataProviderHelper.class);

    /** Получить данные из ДП по ключу алиасу */
    public static Map getDpData(String alias) {
        CitrusRunner citrusRunner = Context.getInstance().getBean(CitrusRunner.class);
        HttpClient e2e = (HttpClient) citrusRunner.getTestContext().getApplicationContext().getBean("dataprovider");
        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .send()
                .get("/aliases?alias_name=eq." + alias)
                .header("Accept-Charset", "utf-8")
                .accept("application/json"));

        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON).extractFromPayload("$.[0].value", "alias.value"));
        try {
            return new ObjectMapper().readValue(citrusRunner.getTestContext().getVariable("alias.value", String.class), HashMap.class);
        } catch (IOException e) {
            throw new FrameworkRuntimeException("Не удалось получить данные из ДП по alias:" + alias, e);
        }
    }

    /** Сохранить данные в ДП по ключу алиасу */
    public static void saveDpData(String alias, Map data) {
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>() {
        }.getType();
        String gsonString = gson.toJson(data, gsonType);
        CitrusRunner citrusRunner = Context.getInstance().getBean(CitrusRunner.class);
        HttpClient e2e = (HttpClient) citrusRunner.getTestContext().getApplicationContext().getBean("dataprovider");
        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .send()
                .post("/aliases?alias_name=eq." + alias)
                .header("Prefer", "resolution=merge-duplicates")
                .accept("application/json")
                .contentType("application/json;charset=UTF-8")
                .messageType(MessageType.JSON)
                .payload("{\"pipeline_id\":null,\"run_uid\":null,\"step_id\":null,\"alias_name\":\"" + alias + "\","
                        + "\"value\":"
                        + gsonString
                        + "}"
                ));


        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .receive()
                .response(HttpStatus.CREATED)
        );
    }

    /** Получить данные из ДП по значению value столбца key. Параметры таблицы и адреса ДП можно задать в файле default.properties */
    public static Map<String, Object> getDpDataValue(String key, String value) {
        CitrusRunner citrusRunner = Context.getInstance().getBean(CitrusRunner.class);
        HttpClient e2e = (HttpClient) citrusRunner.getTestContext().getApplicationContext().getBean("dataprovider");
        String table = System.getProperty("table", Config.loadProperty("dp.table"));
        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .send()
                .get(String.format("/%s?%s=eq.%s", table, key, value))
                .header("Accept-Charset", "utf-8")
                .accept("application/json"));

        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .extractFromPayload("$.[0]", "user"));

        try {
            TypeReference<HashMap<String,Object>> typeRef
                    = new TypeReference<HashMap<String,Object>>() {};
            return new ObjectMapper().readValue(citrusRunner.getTestContext().getVariable("user", String.class), typeRef);
        } catch (IOException e) {
            throw new FrameworkRuntimeException("Не удалось получить данные учетной записи из ДП по значению:" + value, e);
        }
    }

    /** Сохранить данные из ДП по значению value столбца key.*/
    public static void saveDpDataValue(List<Map<String, String>> data) {
        String table = System.getProperty("table", Config.loadProperty("dp.table"));
        CitrusRunner citrusRunner = Context.getInstance().getBean(CitrusRunner.class);
        HttpClient e2e = (HttpClient) citrusRunner.getTestContext().getApplicationContext().getBean("dataprovider");
        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .send()
                .post(String.format("/%s", table))
                .header("Prefer", "resolution=merge-duplicates")
                .accept("application/json")
                .contentType("application/json;charset=UTF-8")
                .messageType(MessageType.JSON)
                .payload(getData(data)));

        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .receive()
                .response(HttpStatus.CREATED)
        );
    }

    /** Обновить данные из ДП по значению value столбца key.*/
    public static void patchDpDataValue(String key, String value, List<Map<String, String>> data) {
        String table = System.getProperty("table", Config.loadProperty("dp.table"));
        CitrusRunner citrusRunner = Context.getInstance().getBean(CitrusRunner.class);
        HttpClient e2e = (HttpClient) citrusRunner.getTestContext().getApplicationContext().getBean("dataprovider");
        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .send()
                .patch(String.format("/%s?%s=eq.%s", table, key, value))
                .header("Prefer", "resolution=merge-duplicates")
                .accept("application/json")
                .contentType("application/json;charset=UTF-8")
                .messageType(MessageType.JSON)
                .payload(getData(data)));

        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .receive()
                .response(HttpStatus.NO_CONTENT)
        );
    }

    public static String getData(List<Map<String, String>> list) {
        StringBuilder data = new StringBuilder();
        data.append("{");
        list.forEach(element -> data.append("\"").append(element.get("Ключ")).append("\":")
                .append("\"").append(element.get("Значение")).append("\","));
        data.deleteCharAt(data.length() - 1);
        data.append("}");
        logger.info(data.toString());
        return data.toString();
    }

    /** Удалить все данные из ДП.*/
    public static void deleteAllDpDataValues() {
        String table = System.getProperty("table", Config.loadProperty("dp.table"));
        CitrusRunner citrusRunner = Context.getInstance().getBean(CitrusRunner.class);
        HttpClient e2e = (HttpClient) citrusRunner.getTestContext().getApplicationContext().getBean("dataprovider");
        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .send()
                .delete(String.format("/%s", table))
                .header("Accept-Charset", "utf-8"));

        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .receive()
                .response(HttpStatus.NO_CONTENT)
        );
    }

    /** Удалить данные из ДП по значению value столбца key.*/
    public static void deleteDpDataValue(String key, String value) {
        String table = System.getProperty("table", Config.loadProperty("dp.table"));
        CitrusRunner citrusRunner = Context.getInstance().getBean(CitrusRunner.class);
        HttpClient e2e = (HttpClient) citrusRunner.getTestContext().getApplicationContext().getBean("dataprovider");
        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .send()
                .delete(String.format("/%s?%s=eq.%s", table, key, value))
                .header("Prefer", "resolution=merge-duplicates"));

        citrusRunner.http(httpActionBuilder -> httpActionBuilder
                .client(e2e)
                .receive()
                .response(HttpStatus.NO_CONTENT)
        );
    }
}
