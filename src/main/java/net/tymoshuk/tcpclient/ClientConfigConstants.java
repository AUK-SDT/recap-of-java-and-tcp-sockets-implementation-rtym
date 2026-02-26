package net.tymoshuk.tcpclient;

/**
 * Configuration constants for the TCP client (env var names and defaults).
 */
public final class ClientConfigConstants {

    private ClientConfigConstants() {
    }

    /** Environment variable for server host. */
    public static final String ENV_TCP_SERVER_HOST = "TCP_SERVER_HOST";

    /** Environment variable for server port. */
    public static final String ENV_TCP_SERVER_PORT = "TCP_SERVER_PORT";

    /** Default server host. */
    public static final String DEFAULT_HOST = "sdt-303.tymoshuk.net";

    /** Default server port. */
    public static final int DEFAULT_PORT = 8080;
}
