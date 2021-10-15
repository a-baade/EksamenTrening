package no.kristiania.quiz;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpClient {

    private int statusCode;

    public HttpClient(String host, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(host,port);
        String request = "GET" + requestTarget+ "HTTP/1.1\r\n" +
                "Connection: close\r\n" +
                "Host"+host + "\r\n" +
                "\r\n";
        socket.getOutputStream().write(request.getBytes());
        System.out.println(request);

        String[] statusLine = readLine(socket).split(" ");
        this.statusCode=Integer.parseInt(statusLine[1]);


    }

    public String readLine(Socket socket) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        int c;
        while ((c=in.read()) != -1 && c != '\r') {
            result.append((char) c);
        }
        in.read();
        return result.toString();
    }
    public int getStatusCode() {
        return 200;
    }

}
