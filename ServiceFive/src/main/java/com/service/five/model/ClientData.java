package com.service.five.model;

import lombok.Data;

import java.util.List;

@Data
public class ClientData {
    private String clientId;
    private String name;
    private String address;
    private List<String> contacts;
}