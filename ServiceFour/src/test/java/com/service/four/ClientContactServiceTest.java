package com.service.four;

import com.service.four.service.ClientContactService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientContactServiceTest {

    @Test
    public void testFetchClientContact() {
        List<String> expected = List.of("contact1@example.com", "contact2@example.com");
        ClientContactService service = new ClientContactService();
        List<String> contacts = service.fetchClientContact("12345");
        assertNotNull(contacts);
        assertArrayEquals(expected.toArray(), contacts.toArray());
    }
}
