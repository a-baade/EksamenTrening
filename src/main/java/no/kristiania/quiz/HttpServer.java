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
            while (true) {
                handleConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection() throws IOException {
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
            writeHttpResponse(clientSocket, responseBody, "HTTP/1.1 200 OK\r\n");
        } else {
            if (directory != null && Files.exists(directory.resolve(fileTarget.substring(1)))) {
                String responseBody = Files.readString(directory.resolve(fileTarget.substring(1)));

                writeHttpResponse(clientSocket, responseBody, "HTTP/1.1 200 OK\r\n");
            }
            String responseText = "File not found: " + requestTarget;
            writeHttpResponse(clientSocket, responseText, "HTTP/1.1 404 Not found\r\n");
        }
    }

    private void writeHttpResponse(Socket clientSocket, String responseBody, String statusCode) throws IOException {
        String responseText = statusCode +
                "Content-Length: " + responseBody.getBytes().length + "\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n"
                + responseBody;
        clientSocket.getOutputStream().write(responseText.getBytes());
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setRootDirectory(Path directory) {
        this.directory = directory;
    }
}
