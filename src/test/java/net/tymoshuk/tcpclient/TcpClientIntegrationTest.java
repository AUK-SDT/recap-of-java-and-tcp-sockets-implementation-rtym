package net.tymoshuk.tcpclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for TCP client request scenarios against an in-process protocol server.
 */
class TcpClientIntegrationTest {

    private ProtocolTestServer server;
    private ClientConfig config;
    private TcpClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new ProtocolTestServer();
        server.start();
        config = new ClientConfig("127.0.0.1", server.getPort());
        client = new TcpClient(config);
    }

    @AfterEach
    void tearDown() {
        if (client != null) {
            client.close();
        }
        if (server != null) {
            server.close();
        }
    }

    /** Skip the server greeting lines (ProtocolTestServer sends exactly two #-prefixed lines). */
    private void skipGreeting() throws IOException {
        for (int i = 0; i < 2; i++) {
            String line = client.readLine();
            if (line == null || !line.startsWith("#")) {
                break;
            }
        }
    }

    @Test
    void nameRequestReceivesGreeting() throws IOException {
        client.connect();
        assertTrue(client.isConnected());
        skipGreeting();

        client.send("NAME Alice");
        String response = client.readLine();
        assertNotNull(response);
        assertEquals("Hello, Alice!", response);
    }

    @Test
    void nameRequestWithMultipleWordsReceivesGreeting() throws IOException {
        client.connect();
        skipGreeting();
        client.send("NAME Bob Smith");
        String response = client.readLine();
        assertNotNull(response);
        assertEquals("Hello, Bob Smith!", response);
    }

    @Test
    void echoRequestReceivesEcho() throws IOException {
        client.connect();
        skipGreeting();
        client.send("hello world");
        String response = client.readLine();
        assertNotNull(response);
        assertEquals("ECHO: hello world", response);
    }

    @Test
    void quitRequestReceivesBye() throws IOException {
        client.connect();
        skipGreeting();
        client.send("QUIT");
        String response = client.readLine();
        assertNotNull(response);
        assertEquals("BYE", response);
    }

    @Test
    void multipleCommandsNameThenEcho() throws IOException {
        client.connect();
        skipGreeting();

        client.send("NAME TestUser");
        assertEquals("Hello, TestUser!", client.readLine());

        client.send("some text");
        assertEquals("ECHO: some text", client.readLine());
    }

    @Test
    void weatherRequestReceivesForecast() {
        // TODO: Implement by students. Test WEATHER <location> request against the server
        // (e.g. connect, skipGreeting(), send "WEATHER <location>", assert response contains location and weather data).
        // ProtocolTestServer now responds with: "Weather in <location>: <temp>°C, <condition>, humidity %d%%, wind %d km/h"
    }
}
