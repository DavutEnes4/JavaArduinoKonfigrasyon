package tr.com.my_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.com.my_app.model.DevreKarti;
import tr.com.my_app.service.DevreKartiService;
import tr.com.my_app.service.ChatService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardChatController {

    private final DevreKartiService kartService;
    private final ChatService chatService;

    @Autowired
    public CardChatController(DevreKartiService kartService, ChatService chatService) {
        this.kartService = kartService;
        this.chatService = chatService;
    }

    @GetMapping
    public List<String> allCards() {
        // Tüm kartları getKartlar ile alıyoruz (başlangıç=0, limit=1000, sorgu boş)
        List<DevreKarti> kartlar = kartService.getKartlar(0, 1000, "");
        return kartlar.stream()
                .map(DevreKarti::getAdi)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DevreKarti getCard(@PathVariable("id") Long id) {
        // Kart detayı al
        DevreKarti kart = kartService.getKartDetay(id);
        if (kart == null) {
            throw new NoSuchElementException("Kart bulunamadı: " + id);
        }
        return kart;
    }

    @PostMapping("/{id}/chat")
    public String chat(@PathVariable("id") Long id,
                       @RequestBody Map<String, String> body) {
        DevreKarti kart = kartService.getKartDetay(id);
        if (kart == null) {
            throw new NoSuchElementException("Kart bulunamadı: " + id);
        }
        String question = body.getOrDefault("question", "");
        return chatService.chatAbout(kart, question);
    }
}
