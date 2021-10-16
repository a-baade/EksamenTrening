package no.kristiania.quiz;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class HttpServer {

    private final ServerSocket serverSocket;

    public HttpServer(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();

        String httpRequest = HttpMessage.readLine(clientSocket);

        System.out.println(httpRequest);

        String headerLine;
        while (!(headerLine = HttpMessage.readLine(clientSocket)).isBlank()) {
            System.out.println(headerLine);
        }

        String httpMessage = "Hello World || Hållo Wørld";

        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + httpMessage.getBytes().length + "\r\n" +
                "Connection: close\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "\r\n" +
                httpMessage;
        clientSocket.getOutputStream().write(httpResponse.getBytes());

        new Thread(this::handleConnections).start();
    }

    private void handleConnections() {
        try (Socket clientSocket = serverSocket.accept())
        {
            String response404 = "HTTP/1.1 404 NotFound\r\nContent-Length: 0\r\n\r\n";
            clientSocket.getOutputStream().write(response404.getBytes());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
