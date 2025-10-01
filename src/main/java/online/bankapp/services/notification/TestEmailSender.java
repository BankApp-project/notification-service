package online.bankapp.services.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bankapp.services.notification.config.AppProperties;
import online.bankapp.services.notification.config.WelcomeMessagePubSubConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@ConditionalOnBooleanProperty(
        name = "app.send-test-email"
)
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class TestEmailSender implements CommandLineRunner {

    private final AppProperties appProperties;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) {

        String EMAIL_TO_SEND_TEST_MSG = appProperties.testEmailAddress();

        log.info("Sending test msg");
        UUID userUUID = UUID.randomUUID();
        String routingKey = "user.created." + userUUID;

        rabbitTemplate.convertAndSend(
                WelcomeMessagePubSubConfig.TOPIC_EXCHANGE_NAME,
                routingKey,
                EMAIL_TO_SEND_TEST_MSG
        );
    }
}