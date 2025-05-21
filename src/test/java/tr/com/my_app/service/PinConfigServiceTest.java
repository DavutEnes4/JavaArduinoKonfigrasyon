package tr.com.my_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import tr.com.my_app.config.AppInitializer;
import tr.com.my_app.config.HibernateConfig;
import tr.com.my_app.config.WebConfig;
import tr.com.my_app.model.DevreKarti;
import tr.com.my_app.model.PinConfig;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppInitializer.class, WebConfig.class, HibernateConfig.class})
@Transactional
@WebAppConfiguration
public class PinConfigServiceTest {

    @Autowired
    private PinConfigService pinConfigService;

    @Autowired
    private DevreKartiService devreKartiService; // DevreKarti oluşturmak için servis kullanılabilir

    @Test
    public void testSaveNewPinConfig() {
        // Hazırlık: Devre kartı oluştur
        Long devreKartiId = 3L;
        String adi = "32fsdfkl32rlkfşdsfö3ğrwpeflösişdç";
        String pinValues = "1,0,1,0,1,0";

        // 1. Kaydetme işlemini gerçekleştir
        boolean success = pinConfigService.saveOrUpdate(null, adi, pinValues, devreKartiId);
        assertTrue(success, "PinConfig başarıyla kaydedilmeli.");

        // 2. Doğrulama: Veri veritabanında gerçekten var mı?
        List<PinConfig> kayitlar = pinConfigService.getPinConfigList(0, 10, "32fsdfkl32rlkfşdsfö3ğrwpeflösişdç");
        assertFalse(kayitlar.isEmpty(), "Kaydedilen veri bulunmalı.");

        // 3. İçeriği kontrol et
        PinConfig pinConfig = kayitlar.get(0);
        assertEquals(adi, pinConfig.getAdi());
        assertEquals(pinValues, pinConfig.getPinValues());
        assertNotNull(pinConfig.getDevreKarti(), "Devre kartı null olmamalı.");
        assertEquals(devreKartiId, pinConfig.getDevreKarti().getId());
    }

    @Test
    public void testDeletePinConfig() {
        DevreKarti kart = new DevreKarti();
        kart.setId(3L);

        kart = devreKartiService.getKartDetay(kart.getId());
        pinConfigService.saveOrUpdate(null, "SilinecekPin", "1,1,0", kart.getId());

        List<PinConfig> list = pinConfigService.getPinConfigList(0, 10, "SilinecekPin");
        assertFalse(list.isEmpty());

        PinConfig pin = list.get(0);
        Boolean deleted = pinConfigService.deletePinConfig(pin.getId());
        assertTrue(deleted);

        PinConfig afterDelete = pinConfigService.getPinConfig(pin.getId());
        assertNull(afterDelete, "Pin silindikten sonra null olmalı.");
    }

    @Test
    public void testGetPinConfigListAndCount() {
        DevreKarti kart = new DevreKarti();
        kart.setId(3L);
        DevreKarti devreKarti =  devreKartiService.getKartDetay(kart.getId());

        for (int i = 0; i < 3; i++) {
            pinConfigService.saveOrUpdate(null, "Liste-" + i, "1,0," + i, devreKarti.getId());
        }

        List<PinConfig> list = pinConfigService.getPinConfigList(0, 10, "Liste-");
        assertEquals(3, list.size());

        Long count = pinConfigService.getTotalCount("Liste-");
        assertEquals(3L, count);
    }
}
