package online.bankapp.services.notification.config;

import online.bankapp.services.notification.DefaultEmailReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailOtpPubSubConfig {

    public static final String TOPIC_EXCHANGE_NAME = "notifications.commands.v1.exchange";
    public static final String QUEUE_NAME = "email-otp-queue";
    public static final String ROUTING_KEY = "send.otp.email";

    private final Queue QUEUE = new Queue(QUEUE_NAME, true);
    private final TopicExchange TOPIC_EXCHANGE = new TopicExchange(TOPIC_EXCHANGE_NAME);

    @Bean
    Binding emailOtpBinding() {
        return BindingBuilder.bind(QUEUE).to(TOPIC_EXCHANGE).with(ROUTING_KEY);
    }

    @Bean
    SimpleMessageListenerContainer emailOtpContainer(ConnectionFactory connectionFactory,
                                                     DefaultEmailReceiver messageReceiver) {

        var listenerAdapter = getMessageListenerAdapter(messageReceiver);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    private MessageListenerAdapter getMessageListenerAdapter(DefaultEmailReceiver defaultEmailReceiver) {

        var listenerAdapter = new MessageListenerAdapter(defaultEmailReceiver, "receive");
        listenerAdapter.setMessageConverter(getMessageConverter());

        return listenerAdapter;
    }

    private MessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
