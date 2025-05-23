package gdocmcp.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class GoogleDocsService {

    private GoogleTokenManager tokenManager;
    private static final Logger log = LoggerFactory.getLogger(GoogleDocsService.class);

    @PostConstruct
    public void init() {
        try {
            String path = System.getenv("GOOGLE_TOKEN_PATH");
            log.info("[init] Using GOOGLE_TOKEN_PATH: {}", path);

            this.tokenManager = new GoogleTokenManager();

            log.info("[init] GoogleTokenManager initialized successfully.");
        } catch (Exception e) {
            log.error("[init] GoogleTokenManager failed: {}", e.getMessage(), e);
            // Show failure reason in fallback tool message
            this.tokenManager = null;
        }
    }

    @Tool(name = "create_doc", description = "Creates a new Google Doc with a title")
    public String createDoc(String title) throws Exception {
        if (tokenManager == null) {
            return "ERROR: GoogleTokenManager was not initialized. Check GOOGLE_TOKEN_PATH and token file. All env vars: " + System.getenv();
        }

        String token = tokenManager.getAccessToken();

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
        if (tokenManager == null) {
            return "ERROR: GoogleTokenManager was not initialized. Check GOOGLE_TOKEN_PATH and token file.";
        }

        String token = tokenManager.getAccessToken();

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
        if (tokenManager == null) {
            return "ERROR: GoogleTokenManager was not initialized. Check GOOGLE_TOKEN_PATH and token file.";
        }

        String token = tokenManager.getAccessToken();

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