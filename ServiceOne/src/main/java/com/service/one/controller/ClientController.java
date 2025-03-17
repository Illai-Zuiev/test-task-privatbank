package com.service.one.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.one.config.JmsConfig;
import com.service.one.model.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    private static final String VALID_SID = "VALID_SID";

    @PostMapping
    public ResponseEntity<String> receiveClient(@RequestHeader("sid") String sid,
                                                @RequestBody ClientRequest clientRequest) {
        if (!VALID_SID.equals(sid)) {
            logger.warn("Невдала спроба авторизації, sid={}", sid);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid SID");
        }

        logger.info("Отримано запит для clientId: {}", clientRequest.getClientId());

        String json = "";
        try {
            json = objectMapper.writeValueAsString(clientRequest);
            logger.info("Надсилання повідомлення: {}", json);
            String finalJson = json;
            jmsTemplate.send(JmsConfig.CLIENT_ID_QUEUE, session -> session.createTextMessage(finalJson));
            return ResponseEntity.ok("Запит прийнято");
        } catch (JsonProcessingException e) {
            logger.info("Помилка надсилання повідомлення: {}", json);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Запит скасовано");
        }
    }
}
