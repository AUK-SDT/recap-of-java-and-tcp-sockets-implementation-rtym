package net.tymoshuk.tcpclient;

import java.io.IOException;

/**
 * Entry point for the TCP client. Connects to the server, prints the greeting, then runs interactive mode.
 */
public class TcpClientApplication {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        System.err.println("Connecting to " + config.getHost() + ":" + config.getPort() + " ...");

        try (TcpClient client = new TcpClient(config)) {
            client.connect();
            System.err.println("Connected. Type commands (NAME, WEATHER, QUIT or any line for echo).");

            client.runInteractive();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

        System.err.println("Disconnected.");
    }
}
