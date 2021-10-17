package no.kristiania.quiz;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private final HttpServer server = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }

    @Test
    void shouldReturn404ForUnknownRequest() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    public void shouldRespondWithRequestTargetIn404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals("File not found: /non-existing", client.getMessageBody());
    }

    @Test
    void shouldReturn200forKnownRequest() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/hello");
        assertAll(
                () -> assertEquals(200, client.getStatusCode()),
                () -> assertEquals("text/html", client.getHeader("Content-Type")),
                () -> assertEquals("<p>Hello World</p>", client.getMessageBody())
        );
    }

    @Test
    public void shouldServeFiles() throws IOException {
        server.setRootDirectory(Paths.get("target/test-classes"));
        String fileContent = "New file created with the timestamp: " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/test-file.txt"),fileContent.getBytes(StandardCharsets.UTF_8));
        HttpClient client = new HttpClient("localhost",server.getPort(),"/test-file.txt");
        assertEquals(fileContent,client.getMessageBody());
    }
}
