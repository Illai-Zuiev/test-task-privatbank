package com.service.three.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.three.config.JmsConfig;
import com.service.three.model.ClientData;
import com.service.three.service.ClientAddressService;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientAddressListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientAddressListener.class);
    private final ClientAddressService clientAddressService;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = JmsConfig.INPUT_QUEUE)
    public void receiveMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String json = ((TextMessage) message).getText();
                ClientData clientData = objectMapper.readValue(json, ClientData.class);
                logger.info("Отримано clientId: {}", clientData.getClientId());

                String address = clientAddressService.fetchClientAddress(clientData.getClientId());
                clientData.setAddress(address);
                logger.info("Додано адресу для clientId {}: {}", clientData.getClientId(), clientData.getAddress());

                String outputJson = objectMapper.writeValueAsString(clientData);
                jmsTemplate.send(JmsConfig.OUTPUT_QUEUE, session -> session.createTextMessage(outputJson));
            }
        } catch (Exception e) {
            logger.error("Помилка під час обробки повідомелння у ClientAddressListener", e);
        }
    }
}