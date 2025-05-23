package gdocmcp.mcp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class GoogleDocsService {

    private static final Logger log = LoggerFactory.getLogger(GoogleDocsService.class);
    
    private final List<String> templates = List.of("Meeting Notes", "Project Plan", "Weekly Report");

    @Tool(name = "get_doc_templates", description = "Returns a list of available Google Doc templates")
    public List<String> getAvailableTemplates() {
        log.info("Fetching available templates...");
        return templates;
    }

    @PostConstruct
    public void init() {
        System.out.println("GoogleDocsService loaded.");
    }


}
