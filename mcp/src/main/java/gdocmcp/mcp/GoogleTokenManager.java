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
            : System.getProperty("user.home") + "/mcp/src/main/java/gdocmcp/mcp/gdocstokens.json";

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
}
