package tr.com.my_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tr.com.my_app.model.DevreKarti;
import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class OpenAIChatService implements ChatService {
    @Value("${openai.api.key}")
    private String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String chatAbout(DevreKarti kart, String userQuestion) {
        try {
            // Kart bilgilerini sistem mesajına koy
            String pinsInfo = kart.getPinler().stream()
                    .map(p -> p.getAdi() + "(" + p.getAciklamasi() + ")")
                    .reduce((a,b) -> a + ", " + b).orElse("");

            Map<String,Object> systemMsg = Map.of(
                    "role", "system",
                    "content", String.format(
                            "Kart: %s\nAçıklama: %s\nPinler: %s",
                            kart.getAdi(), kart.getAciklamalar(), pinsInfo
                    )
            );
            Map<String,Object> userMsg = Map.of(
                    "role", "user",
                    "content", userQuestion
            );
            Map<String,Object> payload = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(systemMsg, userMsg)
            );

            String body = mapper.writeValueAsString(payload);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            Map<?,?> json = mapper.readValue(resp.body(), Map.class);
            var choices = (List<?>) json.get("choices");
            if (!choices.isEmpty()) {
                var first = (Map<?,?>) choices.get(0);
                var msg = (Map<?,?>) first.get("message");
                return (String) msg.get("content");
            }
            return "Yanıt alınamadı.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Hata: " + e.getMessage();
        }
    }
}
