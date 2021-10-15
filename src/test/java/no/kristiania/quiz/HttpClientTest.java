package no.kristiania.quiz;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {
    @Test
    public void returnStatusCode() throws IOException {
        HttpClient client = new HttpClient("httpbin.org",80,"/html");
        assertEquals(200, client.getStatusCode());
    }
}
