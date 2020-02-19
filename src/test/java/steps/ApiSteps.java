package steps;


import com.consol.citrus.http.client.HttpClient;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Тогда;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class ApiSteps extends BaseSteps {

    @Тогда("сохранить в переменные цитруса по ключу {string} значение {string}")
    public void saveVariable(String key, String value) {
        setCitrusVariable(key, value);
    }


    @Тогда("версия сервиса должна быть {string}")
    public void checkVersionFoass(String version) {
        HttpClient endpoint = (HttpClient) getEndpointByName("foass");
        getCitrusRunner().http(httpActionBuilder -> httpActionBuilder
                .client(endpoint)
                .send()
                .get("/version"));
        getCitrusRunner().http(httpActionBuilder -> httpActionBuilder
                .client(endpoint)
                .receive()
                .response(HttpStatus.OK)
                .payload(version));
    }

    @И("отправить в сервис запрос  {string} и получить ответ {string}")
    public void sendHttpMessage(String request, String response) {
        getCitrusRunner().http(httpActionBuilder -> httpActionBuilder
                .client("foass")
                .send()
                .get(request));

        getCitrusRunner().http(httpActionBuilder -> httpActionBuilder
                .client("foass")
                .receive()
                .response(HttpStatus.OK)
                .payload(response));
    }

    @И("отправить сообщение с именем {string} в сервис запрос {string} и получить ответ {string}")
    public void sendHttpMessageWithName(String messageName, String request, String response) {
        getCitrusRunner().http(httpActionBuilder -> httpActionBuilder
                .client("foass")
                .send()
                .get(request).name(messageName));

        getCitrusRunner().http(httpActionBuilder -> httpActionBuilder
                .client("foass")
                .receive()
                .response(HttpStatus.OK)
                .payload(response).name("response:" + messageName));
    }


    @Тогда("отправляю соап сообщение")
    public void sendSoapMessage() {
        getCitrusRunner().soap(soapActionBuilder -> soapActionBuilder
                .client("http://soapclient.com/xml/soapresponder.wsdl")
                .send()

                .payload(new ClassPathResource("templates/soapTest.xml"), Charset.forName(StandardCharsets.UTF_8.name())));

        getCitrusRunner().soap(soapActionBuilder -> soapActionBuilder
                .client("http://soapclient.com/xml/soapresponder.wsdl")
                .receive()
                .payload(new ClassPathResource("templates/soapTestRequest.xml"), Charset.forName(StandardCharsets.UTF_8.name())));
    }


    @И("проверить что существует сообщение с именем {string}")
    public void checkMessageExist(String messageName) {
        softAssert().assertTrue(getMessageByName(messageName) != null, "Сообщение с именем:" + messageName + ", отсуствует");
    }
}