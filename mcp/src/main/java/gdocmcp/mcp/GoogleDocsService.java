package gdocmcp.mcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import jakarta.annotation.PostConstruct;
@Service
public class GoogleDocsService {

    private static String token = "ya29.a0AW4XtxiL67oFgUBjpRbciYjIjIu0Bh3B7gJ1PoNkpF1sT90tmiB8vbo1zRUeGMywWzZoBnbDEygsqa8s21qxaFXBJUkyzTsXtVpJrbYIME8_IsP_641QNHXXGcp1_E10PNYpInkplQc6wEcsfpNRZY1VxwPDFTVTHpzYxbxJaCgYKAWgSARcSFQHGX2MiNcYqbSMK6Q4Jl6i1rm7jdw0175";
    private static String transcriptApiKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6IjEifQ.eyJpc3MiOiJuYWRsZXMiLCJpYXQiOiIxNzQ4MDQ5MzYyIiwicHVycG9zZSI6ImFwaV9hdXRoZW50aWNhdGlvbiIsInN1YiI6ImI2YWYyMTE0OTBkMjRiZTg5ZDQ2NGEwYTZkZmRmMzU5In0.r512iKn-mxh3zOGqC9XUxo472FfTextzp60jQeAGeCI";

    @PostConstruct
    public void init() {
        System.out.println(token + " is the token");
    }

    @Tool(name = "get_transcript", description = "Retrieves the transcript of Youtube video")
    public String getTranscript(String videoUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.supadata.ai/v1/youtube/transcript?url=" + videoUrl))
            .header("x-api-key", transcriptApiKey)
            .timeout(Duration.ofSeconds(70))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    @Tool(name = "create_doc", description = "Creates a new Google Doc with a title")
    public String createDoc(String title) throws Exception {
        if (token == null) {
            return "ERROR: access_token is null ";
        }

        String body = String.format("{\"title\": \"%s\"}", title);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://docs.googleapis.com/v1/documents"))
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .timeout(Duration.ofSeconds(20))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body(); // You can also parse and return the documentId here
    }

    @Tool(name = "append_to_doc", description = "Appends text to an existing Google Doc")
    public String appendToDoc(String docId, String text) throws Exception {
        if (token == null) {
            return "ERROR: access_token was not initialized";
        }

        String body = String.format("""
            {
              "requests": [
                {
                  "insertText": {
                    "location": {
                      "index": 1
                    },
                    "text": "%s"
                  }
                }
              ]
            }
            """, escapeJson(text));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://docs.googleapis.com/v1/documents/" + docId + ":batchUpdate"))
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .timeout(Duration.ofSeconds(20))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    @Tool(name = "read_doc", description = "Reads text content from a Google Doc")
    public String readDoc(String docId) throws Exception {
        if (token == null) {
            return "ERROR: access_token not set.";
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://docs.googleapis.com/v1/documents/" + docId))
            .header("Authorization", "Bearer " + token)
            .timeout(Duration.ofSeconds(20))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}