package no.kristiania.quiz;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class HttpClient {

    private final int statusCode;
    private final Map<String, String> header = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public HttpClient(String host, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(host, port);

        socket.getOutputStream().write(
                ("GET " + requestTarget + " HTTP/1.1\r\n" +
                        "Connection: close \r\n" +
                        "Host:" + host + "\r\n" +
                        "\r\n").getBytes()
        );

        String[] statusLine = readLine(socket).split(" ");
        this.statusCode = Integer.parseInt(statusLine[1]);
        readHeaders(socket);
    }

    private String readLine(Socket socket) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        int c;
        while ((c = in.read()) != -1 && c != '\r') {
            result.append((char) c);
        }
        return result.toString();
    }

    private void readHeaders(Socket socket) throws IOException {
        String responseHeader;
        while (!((responseHeader = readLine(socket)).isBlank())) {
            String[] headerField = responseHeader.split(":");
            header.put(headerField[0].trim(), headerField[1].trim()); //arr[0] is key, arr[1] value
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getHeader(String headerName) {

        return header.get(headerName);
    }

    public String getMessageBody() {
        return null;
    }
}
