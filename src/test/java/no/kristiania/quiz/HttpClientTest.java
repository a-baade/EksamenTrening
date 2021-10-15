package no.kristiania.quiz;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {
    @Test
    public void shouldReturnStatusCode() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldReturn404StatusCode() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/this-page-does-not-exist");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldReadResponseHeaders() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReadMessageBody() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertTrue(
                client.getMessageBody().startsWith("\n<!DOCTYPE html>"),
                "this is html: " + client.getMessageBody());
    }
}
