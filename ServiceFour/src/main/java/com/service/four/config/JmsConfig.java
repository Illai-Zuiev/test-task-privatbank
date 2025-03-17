package com.service.four.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {
    public static final String INPUT_QUEUE = "client.address.queue";
    public static final String OUTPUT_QUEUE = "client.contact.queue";

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setSessionTransacted(true);
        return template;
    }

    @Bean
    public Queue inputQueue() {
        return new ActiveMQQueue(INPUT_QUEUE);
    }

    @Bean
    public Queue outputQueue() {
        return new ActiveMQQueue(OUTPUT_QUEUE);
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
