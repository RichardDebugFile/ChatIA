package demo.chat.intel.controller;

import demo.chat.intel.model.SummaryResult;
import demo.chat.intel.service.ChatIntelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/summary")
public class ChatIntelController {
    private final ChatIntelService chatIntelService;

    public ChatIntelController(ChatIntelService chatIntelService) {
        this.chatIntelService = chatIntelService;
    }

    @GetMapping("/last")
    public ResponseEntity<SummaryResult> getLastSummary() {
        SummaryResult summary = chatIntelService.getLastSummary();
        if (summary == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(summary);
    }
} 