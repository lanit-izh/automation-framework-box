package citrus.endpoints;

import com.consol.citrus.TestCase;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.Message;
import com.consol.citrus.report.MessageTracingTestListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utils.AllureHelper;


@Configuration
public class EndpointConfig {

    @Bean(name = "foass")
    public HttpClient foass() {
        return CitrusEndpoints
                .http()
                .client()
                .requestUrl("https://foaas.com/")
                .build();
    }

    @Bean(name = "dataprovider")
    public HttpClient dataprovider() {
        return CitrusEndpoints
                .http()
                .client()
                .requestUrl(System.getProperty("dp","http://10.19.87.79:13001"))
                .build();
    }

    @Bean(name = "messageTracingTestListener")
    public MessageTracingTestListener messageTracingTestListener() {
        return new CustomMessageListener();
    }

    private static class CustomMessageListener extends MessageTracingTestListener {
        private final StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void onInboundMessage(Message message, TestContext context) {
            stringBuilder.append("INBOUND_MESSAGE:").append(newLine()).append(message).append(newLine()).append(separator()).append(newLine());
            AllureHelper.attachTxt("ответ на исходящее сообщение", message.toString());
            super.onInboundMessage(message,context);
        }

        @Override
        public void onOutboundMessage(Message message, TestContext context) {
            stringBuilder.append("OUTBOUND_MESSAGE:").append(newLine()).append(message).append(newLine()).append(separator()).append(newLine());
            AllureHelper.attachTxt("исходящее сообщение", message.toString());
            super.onInboundMessage(message,context);
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            try {
                super.afterPropertiesSet();
            } catch (CitrusRuntimeException ignore) {
            }
        }

        private String newLine() {
            return "\n";
        }

        @Override
        public void onTestFinish(TestCase test) {
            super.onTestFinish(test);
            AllureHelper.attachTxt("Запросы теста", stringBuilder.toString());
            stringBuilder.setLength(0);
        }

        private String separator() {
            return "======================================================================";
        }
    }
}

