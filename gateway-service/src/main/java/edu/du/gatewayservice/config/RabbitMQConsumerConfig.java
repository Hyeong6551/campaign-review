package edu.du.gatewayservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfig {

    public static final String QUEUE_NAME = "user.update.queue";
    public static final String EXCHANGE_NAME = "user.update.exchange";
    public static final String ROUTING_KEY = "user.update";

    @Bean
    public Queue userUpdateQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public TopicExchange userUpdateExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(userUpdateQueue())
                .to(userUpdateExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
