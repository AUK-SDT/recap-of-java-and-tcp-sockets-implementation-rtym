package net.tymoshuk.tcpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP client that connects to the server and supports NAME, WEATHER, QUIT, and echo.
 */
public class TcpClient implements AutoCloseable {

    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public TcpClient(ClientConfig config) {
        this.host = config.getHost();
        this.port = config.getPort();
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    /**
     * Send a line to the server (no newline is added; caller must send full line including newline semantics).
     * The server expects one line per command; we send the line and the PrintWriter flushes.
     */
    public void send(String line) {
        if (out != null) {
            out.println(line);
        }
    }

    /**
     * Read one line from the server. Returns null on EOF or error.
     */
    public String readLine() throws IOException {
        return in != null ? in.readLine() : null;
    }

    /**
     * Run interactive loop: background thread prints all server output; main thread reads stdin and sends to server until QUIT or EOF.
     */
    public void runInteractive() throws IOException {
        Thread reader = new Thread(() -> {
            try {
                String line;
                while (isConnected() && (line = readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException ignored) {
                // connection closed
            }
        }, "server-reader");
        reader.setDaemon(true);
        reader.start();

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String line;
        while (isConnected() && (line = stdin.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            send(line);
            if ("QUIT".equalsIgnoreCase(line)) {
                break;
            }
        }
    }

    @Override
    public void close() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
            }
            in = null;
        }
        if (out != null) {
            out.close();
            out = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            socket = null;
        }
    }
}
