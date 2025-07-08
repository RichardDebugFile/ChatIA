package demo.chat.intel.ollama;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Component
public class ConexionOllama {

    private final String apiUrl = "http://localhost:11434/api/generate";

    public String obtenerRespuesta(String model, String prompt) throws IOException {
        // 1) Monta y envía la petición
        HttpURLConnection conn = (HttpURLConnection)new URL(apiUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        JSONObject body = new JSONObject()
                .put("model", model)
                .put("prompt", prompt)
                .put("stream", false);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }

        // 2) Chequea código
        if (conn.getResponseCode() != 200) {
            throw new IOException("Ollama error: " + conn.getResponseCode());
        }

        // 3) Lee la respuesta completa
        StringBuilder txt = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                txt.append(line);
            }
        }

        // 4) Parse JSON y extrae el texto
        JSONObject resp = new JSONObject(txt.toString());
        // Si viene directamente "response", úsalo:
        if (resp.has("response")) {
            return resp.getString("response");
        }
        // Si estamos en modo OpenAI-like con "choices":
        if (resp.has("choices")) {
            return resp
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }
        // Fallback: devuelve todo para depuración
        return resp.toString();
    }
}
