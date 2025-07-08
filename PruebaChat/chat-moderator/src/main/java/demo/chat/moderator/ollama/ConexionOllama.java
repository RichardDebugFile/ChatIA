package demo.chat.moderator.ollama;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class ConexionOllama {

    private final String apiUrl;

    public ConexionOllama(@Value("${ollama.url}") String baseUrl) {
        // Aseguramos que apunte al endpoint /api/generate
        this.apiUrl = baseUrl.endsWith("/api/generate")
                ? baseUrl
                : baseUrl + "/api/generate";
    }

    public String obtenerRespuesta(String model, String prompt) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
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

        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IOException("Ollama error: " + code);
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        }

        JSONObject resp = new JSONObject(sb.toString());
        // Como en chat-intel: primero response, luego choices…
        if (resp.has("response")) {
            return resp.getString("response");
        } else {
            return resp
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }
    }
}
