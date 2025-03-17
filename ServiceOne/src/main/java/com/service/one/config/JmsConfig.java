package com.service.one.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {
    public static final String CLIENT_ID_QUEUE = "client.id.queue";

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setSessionTransacted(true);
        return template;
    }

    @Bean
    public Queue clientIdQueue() {
        return new ActiveMQQueue(CLIENT_ID_QUEUE);
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper();
    }

//    @Bean
//    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        return converter;
//    }
}
