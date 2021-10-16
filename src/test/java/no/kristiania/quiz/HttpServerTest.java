package no.kristiania.quiz;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private final HttpServer server = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }

    @Test
    void shouldReturn404ForUnknownRequest() throws IOException {
        HttpClient client = new HttpClient("localhost",server.getPort(), "/non-existing");
        assertEquals(404,client.getStatusCode());
    }
}
