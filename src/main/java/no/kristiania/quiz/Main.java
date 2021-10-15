package no.kristiania.quiz;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        System.out.println(client);
    }
}
