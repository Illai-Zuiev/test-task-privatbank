package com.service.five.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.five.config.JmsConfig;
import com.service.five.entity.ClientEntity;
import com.service.five.model.ClientData;
import com.service.five.repository.ClientRepository;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ClientToSaveListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientToSaveListener.class);
    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper;

    private static final ConcurrentHashMap<String, Boolean> processedIds = new ConcurrentHashMap<>();

    @JmsListener(destination = JmsConfig.INPUT_QUEUE)
    public void receiveMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String json = ((TextMessage) message).getText();
                ClientData clientData = objectMapper.readValue(json, ClientData.class);
                logger.info("Отримано дані для збереження користувача з clientId: {}", clientData.getClientId());

                if (processedIds.putIfAbsent(clientData.getClientId(), true) != null) {
                    logger.info("Повідомлення для clientId {} вже оброблено (за кешем). Пропускаємо обробку.", clientData.getClientId());
                    return;
                }

                ClientEntity entity = new ClientEntity();
                entity.setClientId(clientData.getClientId());
                entity.setName(clientData.getName());
                entity.setAddress(clientData.getAddress());
                if (clientData.getContacts() != null) {
                    entity.setContacts(String.join(",", clientData.getContacts()));
                }
                clientRepository.save(entity);
                logger.info("Дані збережено для clientId: {}", clientData.getClientId());
            }
        } catch (Exception e) {
            logger.error("Помилка обробки повідомлення у ClientToSaveListener", e);
        }
    }
}
