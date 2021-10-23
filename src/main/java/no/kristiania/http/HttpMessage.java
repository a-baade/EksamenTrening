package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class HttpMessage {
    private final Map<String, String> header = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private String messageBody;
    public String startLine;

    public HttpMessage(Socket socket) throws IOException {
        startLine = HttpMessage.readLine(socket);
        readHeaders(socket);
        if (header.containsKey("Content-Length")) {
            messageBody = readBytes(socket, getContentLength());
        }
    }

    static String readLine(Socket socket) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        int c;
        while ((c = in.read()) != '\r') {
            result.append((char) c);
        }
        int expectedNewline = socket.getInputStream().read();
        assert expectedNewline == '\n';
        return result.toString();
    }

    String readBytes(Socket socket, int contentLength) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            result.append((char) socket.getInputStream().read());
        }
        return result.toString();
    }

    private void readHeaders(Socket socket) throws IOException {
        String responseHeader;
        while (!((responseHeader = readLine(socket)).isBlank())) {
            String[] headerField = responseHeader.split(":");
            header.put(headerField[0].trim(), headerField[1].trim());
        }
    }

    public String getStartLine() {
        return startLine;
    }

    public String getHeader(String headerName) {
        return header.get(headerName);
    }

    public String getMessageBody() {
        return messageBody;
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }
}
