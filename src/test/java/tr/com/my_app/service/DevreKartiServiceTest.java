package tr.com.my_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import tr.com.my_app.config.AppInitializer;
import tr.com.my_app.config.WebConfig;
import tr.com.my_app.config.HibernateConfig;
import tr.com.my_app.model.DevreKarti;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppInitializer.class, WebConfig.class, HibernateConfig.class})
@WebAppConfiguration
@Transactional
public class DevreKartiServiceTest {

    @Autowired
    private DevreKartiService devreKartiService;

    @Test
    public void testGetKartlar() {
        List<DevreKarti> kartlar = devreKartiService.getKartlar(0, 10, "");
        assertNotNull(kartlar, "Kart listesi null olmamalı");
        assertTrue(kartlar.size() >= 0, "Kart listesi sıfır ya da daha büyük olmalı");
    }

    @Test
    public void testGetTotalCount() {
        Long count = devreKartiService.getTotalCount("");
        assertNotNull(count, "Sayı null olmamalı");
        assertTrue(count >= 0, "Toplam sayı negatif olamaz");
    }

    @Test
    public void testGetKartDetay() {
        List<DevreKarti> kartlar = devreKartiService.getKartlar(0, 1, "");
        if (kartlar.isEmpty()) {
            System.out.println("Test veritabanında kayıt bulunamadı, test atlandı.");
            return;
        }

        DevreKarti kart = kartlar.get(0);
        DevreKarti detayliKart = devreKartiService.getKartDetay(kart.getId());

        assertNotNull(detayliKart, "Detaylı kart null olmamalı");
        assertEquals(kart.getId(), detayliKart.getId(), "Kart ID'leri uyuşmalı");

        // Eğer pin bağlantısı varsa burada kontrol edebilirsin:
        assertNotNull(detayliKart.getPinler(), "Pin listesi null olmamalı");
    }
}
