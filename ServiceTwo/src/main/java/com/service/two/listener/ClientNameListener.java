package com.service.two.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.two.config.JmsConfig;
import com.service.two.model.ClientData;
import com.service.two.service.ClientNameService;
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
public class ClientNameListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientNameListener.class);
    private final ClientNameService clientNameService;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = JmsConfig.INPUT_QUEUE)
    public void receiveMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String json = ((TextMessage) message).getText();
                ClientData clientData = objectMapper.readValue(json, ClientData.class);
                logger.info("Отримано clientId: {}", clientData.getClientId());

                // Мокаем получение имени по clientId
                String name = clientNameService.fetchClientName(clientData.getClientId());
                clientData.setName(name);
                logger.info("Додано ім'я: {}", name);

                // Сериализуем обновлённый объект и отправляем в следующую очередь
                String outputJson = objectMapper.writeValueAsString(clientData);
                jmsTemplate.send(JmsConfig.OUTPUT_QUEUE, session -> session.createTextMessage(outputJson));
            }
        } catch (Exception e) {
            logger.error("Помилка під час обробки повідомелння у ClientNameListener", e);
        }
    }
}
