package online.bankapp.services.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bankapp.services.notification.config.PubSubConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor

//for testing purposes
//delete this class for prod
public class Runner implements CommandLineRunner {

    String EMAIL_TO_SEND_TEST_MSG = "testEmail@mackiewicz.info";

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) {

        log.info("Sending test msg");
        UUID userUUID = UUID.randomUUID();
        String routingKey = "user.created." + userUUID;

        rabbitTemplate.convertAndSend(
                PubSubConfig.topicExchangeName,
                routingKey,
                EMAIL_TO_SEND_TEST_MSG
        );
    }
}
