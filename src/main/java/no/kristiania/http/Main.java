package no.kristiania.http;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.setQuestionOptions(List.of("1+1= ", "2+2= ", "3+3= "));
        httpServer.setRootDirectory(Paths.get("src/main/resources"));
    }
}
