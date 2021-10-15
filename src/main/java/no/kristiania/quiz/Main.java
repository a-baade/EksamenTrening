package no.kristiania.quiz;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8020);
        HttpClient client = new HttpClient("localhost", 80, "/browse");
    }
}
