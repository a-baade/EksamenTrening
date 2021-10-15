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

        String httpMessage = "Hello World";

        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + httpMessage.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                httpMessage;
        clientSocket.getOutputStream().write(httpResponse.getBytes());
    }

}
