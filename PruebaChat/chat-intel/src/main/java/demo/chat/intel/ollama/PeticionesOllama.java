package demo.chat.intel.ollama;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PeticionesOllama {

    private final ConexionOllama conn;
    private String model = "gemma3";

    @Autowired
    public PeticionesOllama(ConexionOllama conn) {
        this.conn = conn;
    }

    public String consultar(String prompt) {
        try {
            return conn.obtenerRespuesta(model, prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al comunicarse con Ollama.";
        }
    }
}
