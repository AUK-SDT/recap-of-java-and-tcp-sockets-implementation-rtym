package net.tymoshuk.tcpclient;

/**
 * Client configuration from environment variables. Uses {@link ClientConfigConstants} for defaults and env keys.
 */
public final class ClientConfig {

    private final String host;
    private final int port;

    public ClientConfig() {
        this.host = getEnv(ClientConfigConstants.ENV_TCP_SERVER_HOST, ClientConfigConstants.DEFAULT_HOST);
        this.port = parseInt(
                getEnv(ClientConfigConstants.ENV_TCP_SERVER_PORT, String.valueOf(ClientConfigConstants.DEFAULT_PORT)),
                ClientConfigConstants.DEFAULT_PORT);
    }

    /**
     * Config with explicit host and port (e.g. for tests).
     */
    public ClientConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null && !value.isBlank() ? value.trim() : defaultValue;
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
