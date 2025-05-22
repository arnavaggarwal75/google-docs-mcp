package dev.arnav.google_docs_mcp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {


    @GetMapping("/")
    public String home() {
        return "server running 🐒";
    }
    
}
