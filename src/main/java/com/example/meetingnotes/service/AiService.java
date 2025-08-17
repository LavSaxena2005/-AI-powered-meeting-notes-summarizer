package com.example.meetingnotes.service;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    // Dummy summarizer: creates bullets, actions, decisions based on transcript content.
    public String summarizeWithAi(String transcript, String prompt) {
        if (transcript == null || transcript.trim().isEmpty()) {
            return "No transcript provided.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Prompt: ").append(prompt).append("\n\n");
        sb.append("Summary (auto-generated):\n");

        // Very simple heuristic: take first few non-empty lines as bullets
        String[] lines = transcript.split("\\r?\\n");
        int bullets = 0;
        for (String line : lines) {
            String t = line.trim();
            if (t.isEmpty()) continue;
            sb.append("- ").append(t).append("\n");
            bullets++;
            if (bullets >= 6) break;
        }

        sb.append("\nAction Items:\n");
        // simple action detection: lines containing 'will' or 'to' are treated as actions
        int actions = 0;
        for (String line : lines) {
            String low = line.toLowerCase();
            if (low.contains("will") || low.contains("to ") || low.contains("assign")) {
                sb.append("* ").append(line.trim()).append("\n");
                actions++;
                if (actions >= 5) break;
            }
        }
        if (actions == 0) {
            sb.append("* No explicit action items found.\n");
        }

        sb.append("\nDecisions:\n* No explicit decisions detected.\n");

        return sb.toString();
    }
}
