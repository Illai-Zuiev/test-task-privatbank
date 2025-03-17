package com.service.four.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientContactService {
    public List<String> fetchClientContact(String clientId) {
        return List.of("contact1@example.com", "contact2@example.com");
    }
}
