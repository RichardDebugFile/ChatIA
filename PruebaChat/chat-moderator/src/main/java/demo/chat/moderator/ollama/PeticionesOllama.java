package demo.chat.moderator.ollama;

import demo.chat.moderator.config.OllamaConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PeticionesOllama {
    private final ConexionOllama conexion;
    private final String model;

    public PeticionesOllama(ConexionOllama conexion,
                            @Value("${ollama.model}") String model) {
        this.conexion = conexion;
        this.model    = model;
    }

    public String consultar(String prompt) {
        try {
            return conexion.obtenerRespuesta(model, prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
