package utils.data.helpers;


import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import ru.lanit.at.citrus.CitrusRunner;
import ru.lanit.at.context.Context;
import ru.lanit.at.exceptions.FrameworkRuntimeException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/** Вспомогательный класс для работы с ДатаПровайдером */
public class DataProviderHelper {


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
}
