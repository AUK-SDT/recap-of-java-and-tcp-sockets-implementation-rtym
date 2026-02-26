package net.tymoshuk.tcpclient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link TcpClient} (no real server connection).
 */
class TcpClientUnitTest {

    @Test
    void isConnectedReturnsFalseWhenNotConnected() {
        ClientConfig config = new ClientConfig("127.0.0.1", 1);
        TcpClient client = new TcpClient(config);
        assertFalse(client.isConnected());
        client.close();
    }

    @Test
    void sendWhenNotConnectedDoesNotThrow() {
        ClientConfig config = new ClientConfig("127.0.0.1", 1);
        TcpClient client = new TcpClient(config);
        client.send("NAME Alice");
        client.close();
    }

    @Test
    void readLineWhenNotConnectedReturnsNull() throws Exception {
        ClientConfig config = new ClientConfig("127.0.0.1", 1);
        TcpClient client = new TcpClient(config);
        assertNull(client.readLine());
        client.close();
    }

    @Test
    void closeWhenNotConnectedDoesNotThrow() {
        ClientConfig config = new ClientConfig("127.0.0.1", 1);
        TcpClient client = new TcpClient(config);
        client.close();
        client.close();
    }
}
