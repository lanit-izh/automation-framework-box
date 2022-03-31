package application.settings;

import org.redisson.Redisson;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lanit.at.queueservice.dto.TestResult;

import java.io.File;
import java.io.IOException;

@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient getRedissonClient() {
        RedissonClient client = null;
        try {
            Config config = Config.fromYAML(new File("src/main/resources/redis_config.yaml"));
            client = Redisson.create(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Bean
    public RQueue<TestResult> getResultQueue() {
        return getRedissonClient().getQueue("resultQueue");
    }


}
