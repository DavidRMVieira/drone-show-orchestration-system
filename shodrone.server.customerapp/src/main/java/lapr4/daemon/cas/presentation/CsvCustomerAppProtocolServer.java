package lapr4.daemon.cas.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lapr4.customerapp.csvprotocol.server.CustomerAppProtocolRequest;
import lapr4.customerapp.csvprotocol.server.CsvCustomerAppProtocolMessageParser;


/**
 * Server socket for CAS daemon using the CSV-based protocol.
 *
 */
public class CsvCustomerAppProtocolServer {
    private static final Logger LOGGER = LogManager.getLogger(CsvCustomerAppProtocolServer.class);

    private static String lastDeviceConnected = "";

    /**
     * Client socket.
     *
     */
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private final CsvCustomerAppProtocolMessageParser parser;

        public ClientHandler(final Socket socket, final CsvCustomerAppProtocolMessageParser parser) {
            this.clientSocket = socket;
            this.parser = parser;
        }

        @Override
        public void run() {
            final var clientIP = clientSocket.getInetAddress();
            if (!lastDeviceConnected().equals(clientIP.getHostAddress())) {
                LOGGER.info("Accepted connection from {} : {}", clientIP.getHostAddress(), clientSocket.getPort());
            }
            lastDeviceConnected = clientIP.getHostAddress();


            try (var out = new PrintWriter(clientSocket.getOutputStream(), true);
                 var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    LOGGER.info("\n |Received message: \n{}\n----", inputLine);
                    final CustomerAppProtocolRequest request = parser.parse(inputLine);
                    final String response = request.execute();
                    out.println(response);
                    // Limit the response length to 200 characters for logging
                    int maxLen = 110;
                    String limitedResponse = response;
                    if (response != null && response.length() > maxLen) {
                        limitedResponse = response.substring(0, maxLen) + " - etc.";
                    }
                    LOGGER.info("\n |Sent message: \n{}\n----", limitedResponse);
                    if (request.isGoodbye()) {
                        break;
                    }
                }
            } catch (final IOException e) {
                LOGGER.error(e);
            } finally {
                try {
                    clientSocket.close();
                    LOGGER.debug("Closing client socket {} : {}", clientIP.getHostAddress(), clientSocket.getPort());
                } catch (final IOException e) {
                    LOGGER.error("While closing the client socket {} : {}", clientIP.getHostAddress(),
                            clientSocket.getPort(), e);
                }
                // null the reference to ensure it will be caught by the garbage collector
                clientSocket = null;

                // helper debug - SHOULD NOT BE USED IN PRODUCTION CODE!!!
                if (LOGGER.isDebugEnabled()) {
                    final int finalThreadCount = Thread.activeCount();
                    LOGGER.debug("Ending client thread - final thread count: {}", finalThreadCount);
                    final Thread[] t = new Thread[finalThreadCount];
                    final int n = Thread.enumerate(t);
                    for (var i = 0; i < n; i++) {
                        LOGGER.debug("T {} : {}", t[i].getId(), t[i].getName());
                    }
                }
            }
        }
    }

    private final CsvCustomerAppProtocolMessageParser parser;

    public CsvCustomerAppProtocolServer(final CsvCustomerAppProtocolMessageParser parser) {
        this.parser = parser;
    }

    /**
     * Wait for connections.
     * <p>
     * Suppress warning java:S2189 - Loops should not be infinite
     *
     * @param port
     */
    @SuppressWarnings("java:S2189")
    private void listen(final int port) {
        try (var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, parser).start();
            }
        } catch (final IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Wait for connections.
     *
     * @param port
     * @param blocking
     *            if {@code false} the socket runs in its own thread and does not block calling
     *            thread.
     */
    public void start(final int port, final boolean blocking) {
        if (blocking) {
            listen(port);
        } else {
            new Thread(() -> listen(port)).start();
        }
    }

    public static String lastDeviceConnected() {
        return lastDeviceConnected;
    }

}
