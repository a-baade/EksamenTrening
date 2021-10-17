package no.kristiania.quiz;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;


public class HttpServer {

    private final ServerSocket serverSocket;
    private Path directory;

    public HttpServer(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        new Thread(this::handleConnections).start();
    }

    private void handleConnections() {
        try {
            Socket clientSocket = serverSocket.accept();

            String[] requestLine = HttpMessage.readLine(clientSocket).split(" ");
            String requestTarget = requestLine[1];

            int questionPos = requestTarget.indexOf('?');
            String fileTarget;
            String query = null;
            if (questionPos != -1) {
                fileTarget = requestTarget.substring(0,questionPos);
                query = requestTarget.substring(questionPos + 1);
            }else {
                fileTarget = requestTarget;
            }

            if (fileTarget.equals("/hello")) {
                String yourName = "World";
                if(query !=  null) {
                    yourName = query.split("=")[1];
                }

                String responseBody = "<p>Hello " + yourName + "</p>";

                String responseText = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: " + responseBody.getBytes().length + "\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n"
                        + responseBody;
                clientSocket.getOutputStream().write(responseText.getBytes());

            } else {
                if (directory != null && Files.exists(directory.resolve(fileTarget.substring(1)))) {
                    String responseBody = Files.readString(directory.resolve(fileTarget.substring(1)));

                    String responseText = "HTTP/1.1 200 OK\r\n" +
                            "Content-Length: " + responseBody.getBytes().length + "\r\n" +
                            "Content-Type: text/html\r\n" +
                            "\r\n"
                            + responseBody;
                    clientSocket.getOutputStream().write(responseText.getBytes());
                }

                String responseText = "File not found: " + requestTarget;
                String response404 = "HTTP/1.1 404 Not found\r\n" +
                        "Content-Length: " + responseText.getBytes().length + "\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n"
                        + responseText;
                clientSocket.getOutputStream().write(response404.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1986);
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
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setRootDirectory(Path directory) {
        this.directory = directory;
    }
}
