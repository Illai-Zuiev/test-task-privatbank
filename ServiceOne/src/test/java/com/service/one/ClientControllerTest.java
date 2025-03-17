package com.service.one;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.one.config.JmsConfig;
import com.service.one.controller.ClientController;
import com.service.one.model.ClientRequest;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

public class ClientControllerTest {

    @Test
    public void testReceiveClientValidSid() {
        JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ClientController controller = new ClientController(jmsTemplate, objectMapper);

        ClientRequest request = new ClientRequest();
        request.setClientId("12345");

        ResponseEntity<String> response = controller.receiveClient("VALID_SID", request);
        assertEquals(200, response.getStatusCodeValue());
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setClientId("12345");
        verify(jmsTemplate).send(anyString(), any());
    }

    @Test
    public void testReceiveClientInvalidSid() {
        JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ClientController controller = new ClientController(jmsTemplate, objectMapper);

        ClientRequest request = new ClientRequest();
        request.setClientId("12345");

        ResponseEntity<String> response = controller.receiveClient("INVALID", request);
        assertEquals(401, response.getStatusCodeValue());
    }
}
