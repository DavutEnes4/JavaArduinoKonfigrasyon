package tr.com.my_app.service;

import tr.com.my_app.model.DevreKarti;

public interface ChatService {
    /**
     * Mevcut DevreKarti nesnesi üzerinden LLM API'ine soru yöneltir ve yanıt döner.
     */
    String chatAbout(DevreKarti kart, String userQuestion);
}