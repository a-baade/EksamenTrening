package no.kristiania.http;

import java.io.IOException;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.setRootDirectory(Paths.get("src/main/resources"));
    }
}
