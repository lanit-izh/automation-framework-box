package ru.lanit.at;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.annotations.Test;

@SpringBootApplication
public class Application {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
    }

    @Test
    public void main() throws InterruptedException {
        context = SpringApplication.run(Application.class);
        while (true) {
            Thread.sleep(10_000);
            if (!context.isRunning()) {
                break;
            }
        }
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
