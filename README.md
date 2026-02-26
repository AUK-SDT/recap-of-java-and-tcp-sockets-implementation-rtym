[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/srlEOKBY)
# Java TCP Client

A Java TCP client that connects to the [TCP Socket Server](https://github.com/your-org/java-tcp-server) and supports the same protocol: **NAME**, **WEATHER**, **QUIT**, and echo for any other line.

**Default server:** `sdt-303.tymoshuk.net:8080`

## Requirements

- Java 21 or later
- Maven 3.6+

## Build

```bash
mvn clean install
```

Or:

```bash
mvn clean package
```

The runnable JAR is at `target/tcp-client-1.0.0-SNAPSHOT.jar`.

## Run

**Default (connects to sdt-303.tymoshuk.net:8080):**

```bash
java -jar target/tcp-client-1.0.0-SNAPSHOT.jar
```

Or with Maven:

```bash
mvn exec:java
```

**Override host/port via environment:**

```bash
TCP_SERVER_HOST=localhost TCP_SERVER_PORT=8080 java -jar target/tcp-client-1.0.0-SNAPSHOT.jar
```

## Configuration (environment variables)

| Variable           | Default                  | Description    |
|--------------------|--------------------------|----------------|
| `TCP_SERVER_HOST`  | `sdt-303.tymoshuk.net`   | Server hostname |
| `TCP_SERVER_PORT`  | `8080`                   | Server port     |

## Protocol (matches server)

| Command            | Description |
|--------------------|-------------|
| `NAME <username>`  | Set your name; server replies "Hello, \<username>!" |
| `WEATHER <location>` | Get a random weather forecast for the location |
| `QUIT`             | Disconnect; server replies "BYE" |
| Any other line     | Echoed back as "ECHO: \<line>" |

## Example session

```
$ java -jar target/tcp-client-1.0.0-SNAPSHOT.jar
Connecting to sdt-303.tymoshuk.net:8080 ...
Connected. Type commands (NAME, WEATHER, QUIT or any line for echo).

# Welcome to the TCP server!
# Commands: NAME <your name> | WEATHER <location> | QUIT
NAME Alice
Hello, Alice!
WEATHER London
Weather in London: 18°C, partly cloudy, humidity 65%, wind 12 km/h
hello
ECHO: hello
QUIT
BYE
Disconnected.
```

## Project layout

```
java-tcp-client/
├── pom.xml
├── README.md
└── src/main/java/net/tymoshuk/tcpclient/
    ├── TcpClientApplication.java   # Entry point
    ├── ClientConfig.java           # Config from env (host, port)
    └── TcpClient.java              # Socket connect, send/read, interactive loop
```
