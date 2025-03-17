package com.service.two.service;

import org.springframework.stereotype.Service;

@Service
public class ClientNameService {
    public String fetchClientName(String clientId) {
        return "Іван Іванов";
    }
}
