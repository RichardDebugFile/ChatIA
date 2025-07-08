package demo.chat.moderator.service;

import demo.chat.moderator.model.ChatMessageEntity;
import demo.chat.moderator.model.ModerationResult;
import demo.chat.moderator.repo.ChatMessageEntityRepository;
import demo.chat.moderator.ollama.PeticionesOllama;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatModeratorService {

    private static final Logger log = LoggerFactory.getLogger(ChatModeratorService.class);

    private final ChatMessageEntityRepository repo;
    private final PeticionesOllama ollama;

    /** Arranca 5 min atrás para no saltarse mensajes al inicio */
    private LocalDateTime lastCheck = LocalDateTime.now().minusMinutes(5);
    
    /** Historial de infracciones detectadas */
    private final List<ModerationResult.Violation> violationsHistory = new ArrayList<>();
    
    /** Contador de mensajes escaneados */
    private int totalMessagesScanned = 0;

    public ChatModeratorService(ChatMessageEntityRepository repo,
                                PeticionesOllama ollama) {
        this.repo   = repo;
        this.ollama = ollama;
    }

    /** Cada 60 s busca nuevos mensajes y pide a Ollama nombres de infractores o "NA" */
    @Scheduled(initialDelay = 5_000, fixedDelay = 60_000)
    public void scanNewMessages() {
        LocalDateTime now = LocalDateTime.now();
        log.info("🔍 Buscando en MySQL entre {} y {}", lastCheck, now);

        List<ChatMessageEntity> rec = repo.findByTimestampBetween(lastCheck, now);
        log.info("🔍 Encontrados {} mensajes nuevos", rec.size());
        lastCheck = now;
        totalMessagesScanned += rec.size();

        if (rec.isEmpty()) {
            return;
        }

        String prompt = "Debes detectar usuarios con lenguaje inapropiado, pero no seas tan estricto.\n" +
                "Si hay alguno, devuelve solo el nombre de usuario (o varios separados por comas).\n" +
                "Si no hay nada inapropiado, devuelve exactamente NA.\n\n" +
                rec.stream()
                        .map(m -> m.getUsername() + ": " + m.getMessage())
                        .collect(Collectors.joining("\n"));

        log.info("🤖 Enviando prompt a Ollama ({} líneas)…", rec.size());
        String respuesta = ollama.consultar(prompt).trim();
        log.info("🤖 Respuesta de Ollama: \"{}\"", respuesta);

        // Caso "sin respuesta" o IA devolvió "NA"
        if (respuesta.isEmpty() || respuesta.equalsIgnoreCase("NA")) {
            log.info("✅ No se detectaron infracciones en este lote.");
            return;
        }

        // Aquí IA devolvió uno o varios nombres de usuario
        log.warn("⚠️ Posibles infractores: {}", respuesta);
        String[] nombres = respuesta.split("\\s*,\\s*");
        for (String user : nombres) {
            rec.stream()
                    .filter(m -> m.getUsername().equalsIgnoreCase(user))
                    .findFirst()
                    .ifPresent(m -> {
                        ModerationResult.Violation violation = new ModerationResult.Violation(
                            m.getUsername(), m.getMessage(), m.getTimestamp(), m.getPlatform()
                        );
                        violationsHistory.add(violation);
                        log.warn("⚠️ [{}] Usuario infractor='{}': \"{}\"",
                                m.getTimestamp(), m.getUsername(), m.getMessage());
                    });
        }
    }

    /**
     * Obtiene el estado actual de la moderación
     */
    public ModerationResult getCurrentStatus() {
        return new ModerationResult(lastCheck, totalMessagesScanned, new ArrayList<>(violationsHistory));
    }

    /**
     * Obtiene las infracciones de los últimos X minutos
     */
    public List<ModerationResult.Violation> getViolationsFromLastMinutes(int minutes) {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(minutes);
        return violationsHistory.stream()
                .filter(v -> v.getTimestamp().isAfter(cutoff))
                .collect(Collectors.toList());
    }
}
