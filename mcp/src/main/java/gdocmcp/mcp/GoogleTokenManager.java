package gdocmcp.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.URI;
import java.net.http.*;
import java.time.Instant;
import java.util.*;

public class GoogleTokenManager {

    private static final String TOKEN_PATH = System.getProperty("user.home") + "/.gdocs-tokens.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Map<String, Object> tokenData;

    public GoogleTokenManager() throws Exception {
        tokenData = MAPPER.readValue(new File(TOKEN_PATH), Map.class);
    }

    public String getAccessToken() throws Exception {
        Instant expiry = Instant.parse((String) tokenData.get("expiry_time"));
        if (Instant.now().isAfter(expiry.minusSeconds(60))) {
            refreshAccessToken();
        }
        return (String) tokenData.get("access_token");
    }

    private void refreshAccessToken() throws Exception {
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
        tokenData.put("expiry_time", Instant.now().plusSeconds((Integer) newToken.get("expires_in")).toString());

        MAPPER.writeValue(new File(TOKEN_PATH), tokenData);
    }
}
