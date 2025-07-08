package demo.chat.moderator.controller;

import demo.chat.moderator.model.ModerationResult;
import demo.chat.moderator.service.ChatModeratorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderator")
@CrossOrigin(origins = "*")
public class ChatModeratorController {

    private final ChatModeratorService moderatorService;

    public ChatModeratorController(ChatModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    /**
     * Obtiene el estado actual de la moderación
     */
    @GetMapping("/status")
    public ModerationResult getModerationStatus() {
        return moderatorService.getCurrentStatus();
    }

    /**
     * Obtiene las infracciones de los últimos X minutos
     */
    @GetMapping("/violations")
    public List<ModerationResult.Violation> getRecentViolations(
            @RequestParam(defaultValue = "5") int minutes) {
        return moderatorService.getViolationsFromLastMinutes(minutes);
    }

    /**
     * Fuerza una ejecución manual del escaneo de moderación
     */
    @PostMapping("/scan")
    public String triggerManualScan() {
        moderatorService.scanNewMessages();
        return "Escaneo manual ejecutado";
    }
} 