package com.service.four.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.four.config.JmsConfig;
import com.service.four.model.ClientData;
import com.service.four.service.ClientContactService;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientContactListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientContactListener.class);
    private final ClientContactService clientContactService;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = JmsConfig.INPUT_QUEUE)
    public void receiveMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String json = ((TextMessage) message).getText();
                ClientData clientData = objectMapper.readValue(json, ClientData.class);
                logger.info("Отримано clientId: {}", clientData.getClientId());

                List<String> contacts = clientContactService.fetchClientContact(clientData.getClientId());
                clientData.setContacts(contacts);
                logger.info("Додано контакти для clientId {}", clientData.getClientId());

                String outputJson = objectMapper.writeValueAsString(clientData);
                jmsTemplate.send(JmsConfig.OUTPUT_QUEUE, session -> session.createTextMessage(outputJson));
            }
        } catch (Exception e) {
            logger.error("Помилка під час обробки повідомелння у ClientContactListener", e);
        }
    }
}
