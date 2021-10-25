package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpServer {

    private final ServerSocket serverSocket;
    private Path rootDirectory;
    private List<String> questionOptions = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();

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

        HttpMessage httpMessage = new HttpMessage(clientSocket);
        String[] requestLine = HttpMessage.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        String query = null;
        if (questionPos != -1) {
            fileTarget = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos + 1);
        } else {
            fileTarget = requestTarget;
        }
        if (fileTarget.equals("/hello")) {
            String queryOutput = "World";
            if (query != null) {
                queryOutput = query.split("=")[1];
            }
            String responseText = "<p>Hello " + queryOutput + "</p>";

            writeHttpResponse(clientSocket, responseText, "text/html", "HTTP/1.1 200 OK\r\n");

        } else if (fileTarget.equals("/api/newQuestion")) {
            Map<String, String> queryMap = parseRequestParameters(httpMessage.getMessageBody());
            Question question = new Question();
            question.setQuestions(queryMap.get("question"));
            questions.add(question);
            writeHttpResponse(clientSocket, "it is done", "text/html", "HTTP/1.1 200 OK\r\n");

        } else if (fileTarget.equals("/api/questionOptions")) {
            String responseText = "";

            int value = 1;
            for (String questionOptions : questionOptions) {
                responseText += "<option value=" + (value++) + ">" + questionOptions + "</option>";
            }

            writeHttpResponse(clientSocket, responseText, "text/html", "HTTP/1.1 200 OK\r\n");

        } else {
            if (rootDirectory != null && Files.exists(rootDirectory.resolve(fileTarget.substring(1)))) {
                String responseText = Files.readString(rootDirectory.resolve(fileTarget.substring(1)));

                String contentType = "text/plain";
                if (requestTarget.endsWith(".html")) {
                    contentType = "text/html";
                }
                writeHttpResponse(clientSocket, responseText, "", "HTTP/1.1 200 OK\r\n");
                return;
            }

            String responseText = "File not found: " + requestTarget;
            writeHttpResponse(clientSocket, responseText, "", "HTTP/1.1 404 Not found\r\n");
        }
    }

    private Map<String, String> parseRequestParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalsPosition = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalsPosition);
            String parameterValue = queryParameter.substring(equalsPosition + 1);
            queryMap.put(parameterName, parameterValue);
        }
        return queryMap;
    }

    private void writeHttpResponse(Socket clientSocket, String responseBody, String contentType, String statusCode) throws IOException {
        String responseText = statusCode +
                "Content-Length: " + responseBody.getBytes().length + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n"
                + responseBody;
        clientSocket.getOutputStream().write(responseText.getBytes());
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setRootDirectory(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void setQuestionOptions(List<String> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

}
