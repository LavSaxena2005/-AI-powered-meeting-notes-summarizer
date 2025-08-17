package com.example.meetingnotes.controller;

import com.example.meetingnotes.service.AiService;
import com.example.meetingnotes.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AiService aiService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/summarize", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> summarize(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam(value = "text", required = false) String text,
                                         @RequestParam("prompt") String prompt) throws IOException {

        String transcript = "";
        if (file != null && !file.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                transcript = br.lines().collect(Collectors.joining("\n"));
            }
        }
        if (StringUtils.hasText(text)) {
            if (!transcript.isEmpty()) transcript += "\n";
            transcript += text;
        }
        if (!StringUtils.hasText(transcript)) {
            transcript = "No transcript provided.";
        }

        String summary = aiService.summarizeWithAi(transcript, prompt);
        Map<String, String> resp = new HashMap<>();
        resp.put("summary", summary);
        return resp;
    }

    @PostMapping(value = "/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> email(@RequestBody Map<String, String> body) {
        String to = body.getOrDefault("to", "");
        String summary = body.getOrDefault("summary", "");
        String subject = body.getOrDefault("subject", "Meeting Summary");

        String result = emailService.send(to, subject, summary);
        Map<String, String> resp = new HashMap<>();
        resp.put("status", result);
        return resp;
    }
}
