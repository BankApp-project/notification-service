package online.bankapp.services.notification.config;

import online.bankapp.services.notification.WelcomeMessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WelcomeMessagePubSubConfig {

    public static final String TOPIC_EXCHANGE_NAME = "bankapp-exchange";

    public static final String QUEUE_NAME = "notification-queue";
    private static final String ROUTING_KEY = "user.created.#";

    private final Queue WELCOME_EMAIL_QUEUE = new Queue(QUEUE_NAME, true);
    private final TopicExchange WELCOME_EMAIL_TOPIC_EXCHANGE = new TopicExchange(TOPIC_EXCHANGE_NAME);

    @Bean
    Binding userCreatedBinding() {
        return BindingBuilder.bind(WELCOME_EMAIL_QUEUE).to(WELCOME_EMAIL_TOPIC_EXCHANGE).with(ROUTING_KEY);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             WelcomeMessageReceiver messageReceiver) {

        var listenerAdapter = getMessageListenerAdapter(messageReceiver);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return  container;
    }

    private MessageListenerAdapter getMessageListenerAdapter(WelcomeMessageReceiver welcomeMessageReceiver) {
        return new MessageListenerAdapter(welcomeMessageReceiver, "receive");
    }
}
