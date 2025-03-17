package com.service.three.service;

import org.springframework.stereotype.Service;

@Service
public class ClientAddressService {
    public String fetchClientAddress(String clientId) {
        return "вул. Незалежності, 10";
    }
}
