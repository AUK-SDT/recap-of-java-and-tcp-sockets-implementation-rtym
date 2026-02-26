package net.tymoshuk.tcpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * In-process TCP server that implements the same protocol as the real server (NAME, WEATHER, QUIT, echo).
 * Used for request-scenario tests.
 */
final class ProtocolTestServer implements AutoCloseable {

    private static final String[] CONDITIONS = {"sunny", "partly cloudy", "cloudy", "rainy", "clear"};
    private static final int[] TEMP_RANGE = {5, 30};
    private static final int[] HUMIDITY_RANGE = {40, 90};
    private static final int[] WIND_RANGE = {5, 25};

    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    ProtocolTestServer() throws IOException {
        this.serverSocket = new ServerSocket(0);
    }

    int getPort() {
        return serverSocket.getLocalPort();
    }

    void start() {
        executor.submit(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket client = serverSocket.accept();
                    executor.submit(() -> handleClient(client));
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {
            out.println("# Welcome to the TCP server!");
            out.println("# Commands: NAME <your name> | WEATHER <location> | QUIT");
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.toUpperCase().startsWith("NAME ")) {
                    String name = line.substring(5).trim();
                    out.println("Hello, " + name + "!");
                } else if (line.toUpperCase().startsWith("WEATHER ")) {
                    String location = line.substring(8).trim();
                    out.println(formatWeather(location));
                } else if ("QUIT".equalsIgnoreCase(line)) {
                    out.println("BYE");
                    break;
                } else {
                    out.println("ECHO: " + line);
                }
            }
        } catch (IOException e) {
            // ignore
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static String formatWeather(String location) {
        if (location.isEmpty()) {
            location = "Unknown";
        }
        int hash = location.hashCode();
        int temp = TEMP_RANGE[0] + Math.abs(hash % (TEMP_RANGE[1] - TEMP_RANGE[0] + 1));
        int humidity = HUMIDITY_RANGE[0] + Math.abs((hash / 31) % (HUMIDITY_RANGE[1] - HUMIDITY_RANGE[0] + 1));
        int wind = WIND_RANGE[0] + Math.abs((hash / 31 / 31) % (WIND_RANGE[1] - WIND_RANGE[0] + 1));
        String condition = CONDITIONS[Math.abs(hash % CONDITIONS.length)];
        return String.format("Weather in %s: %d°C, %s, humidity %d%%, wind %d km/h",
                location, temp, condition, humidity, wind);
    }

    @Override
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
        executor.shutdownNow();
    }
}
