package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

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
    void shouldServeFiles() throws IOException {
        server.setRootDirectory(Paths.get("target/test-classes"));
        String fileContent = "New file created with the timestamp: " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/test-file.txt"), fileContent.getBytes(StandardCharsets.UTF_8));
        HttpClient client = new HttpClient("localhost", server.getPort(), "/test-file.txt");
        assertEquals(fileContent, client.getMessageBody());
    }

    @Test
    void shouldUseFileExtensionForHtmlFile() throws IOException {
        server.setRootDirectory(Paths.get("target/test-classes"));
        String fileContent = "<p>Hello</p>";
        Files.write(Paths.get("target/test-classes/test-file.html"), fileContent.getBytes(StandardCharsets.UTF_8));
        HttpClient client = new HttpClient("localhost", server.getPort(), "/test-file.html");
        assertEquals(fileContent, client.getMessageBody());
    }

    @Test
    void shouldEchoQueryParameter() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/hello?yourName=andreas");
        assertEquals("<p>Hello andreas</p>", client.getMessageBody());
    }

    @Test
    void shouldHandleMoreThanOneRequest() throws IOException {
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello")
                .getStatusCode());
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello")
                .getStatusCode());
    }

    @Test
    void shouldReturnQuestionsFromServer() throws IOException {
        server.setQuestionOptions(List.of("Question 1", "Question 2"));

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/questionOptions");
        assertEquals(
                "<option value=1>Question 1</option><option value=2>Question 2</option>",
                client.getMessageBody());
    }

    @Test
    void shouldCreateNewQuestion() throws IOException {
        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestion",
                "title=foo&text=nar"
        );
        assertEquals(200, postClient.getStatusCode());
        Question questions = server.getQuestions().get(0);
        assertEquals("4+4: ", questions.getQuestions());
    }
}
