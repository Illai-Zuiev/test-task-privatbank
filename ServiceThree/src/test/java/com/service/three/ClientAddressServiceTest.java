package com.service.three;

import com.service.three.service.ClientAddressService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientAddressServiceTest {

    @Test
    public void testFetchAddressName() {
        ClientAddressService service = new ClientAddressService();
        String address = service.fetchClientAddress("12345");
        assertNotNull(address);
        assertEquals("вул. Незалежності, 10", address);
    }
}
