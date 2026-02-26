package net.tymoshuk.tcpclient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ClientConfig}.
 */
class ClientConfigTest {

    @Test
    void constructorWithHostAndPortReturnsGivenValues() {
        ClientConfig config = new ClientConfig("test.example.com", 9000);
        assertEquals("test.example.com", config.getHost());
        assertEquals(9000, config.getPort());
    }

    @Test
    void defaultConstructorReturnsNonNullHostAndPort() {
        ClientConfig config = new ClientConfig();
        assertNotNull(config.getHost());
        assertTrue(config.getPort() > 0 && config.getPort() <= 65535);
    }
}
