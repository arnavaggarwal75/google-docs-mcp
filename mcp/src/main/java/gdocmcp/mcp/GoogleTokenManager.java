package gdocmcp.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.URI;
import java.net.http.*;
import java.util.*;

public class GoogleTokenManager {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final File tokenFile;
    private Map<String, Object> tokenData;

    public GoogleTokenManager() throws Exception {
        String envPath = System.getenv("GOOGLE_TOKEN_PATH");
        String path = (envPath != null && !envPath.isBlank())
            ? envPath
            : System.getProperty("user.home") + "/.gdocs-tokens.json";

        System.out.println("[GoogleTokenManager] Loading tokens from: " + path);

        this.tokenFile = new File(path);
        if (!tokenFile.exists()) {
            throw new IllegalStateException("Token file not found: " + tokenFile.getAbsolutePath());
        }

        try {
            tokenData = MAPPER.readValue(tokenFile, Map.class);
        } catch (Exception e) {
            System.out.println("[GoogleTokenManager] Failed to parse token file: " + e.getMessage());
            throw e;
        }
    }

    public String getAccessToken() {
        return (String) tokenData.get("access_token");
    }

    public void refreshAccessToken() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        Map<Object, Object> params = new LinkedHashMap<>();
        params.put("client_id", tokenData.get("client_id"));
        params.put("client_secret", tokenData.get("client_secret"));
        params.put("refresh_token", tokenData.get("refresh_token"));
        params.put("grant_type", "refresh_token");

        String form = params.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .reduce((a, b) -> a + "&" + b)
            .orElse("");

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://oauth2.googleapis.com/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(form))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, Object> newToken = MAPPER.readValue(response.body(), Map.class);
        tokenData.put("access_token", newToken.get("access_token"));

        // optionally log
        System.out.println("[GoogleTokenManager] Refreshed access token");

        MAPPER.writeValue(tokenFile, tokenData);
    }
}
