package com.service.two;

import com.service.two.service.ClientNameService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientNameServiceTest {

    @Test
    public void testFetchClientName() {
        ClientNameService service = new ClientNameService();
        String name = service.fetchClientName("12345");
        assertNotNull(name);
        assertEquals("Іван Іванов", name);
    }
}
