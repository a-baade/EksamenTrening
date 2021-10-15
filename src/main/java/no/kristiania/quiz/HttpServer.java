package no.kristiania.quiz;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private final ServerSocket serverSocket;

    public HttpServer(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        new Thread(this::handleConnections).start();
        System.out.println("New thread, running Connections");
    }

    private void handleConnections() {
        try {
            Socket clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
