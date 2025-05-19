package tr.com.my_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tr.com.my_app.model.DevreKarti;
import tr.com.my_app.service.DevreKartiService;
import tr.com.my_app.service.PinConfigService;

import java.util.List;

@Controller
public class DevreKartiController {

    @Autowired
    private DevreKartiService devreKartiService;

    @Autowired
    private PinConfigService pinConfigService;

    @GetMapping("/kartlar")
    public String kartlariListele(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        int start = page * size;
        List<DevreKarti> kartlar = devreKartiService.getKartlar(start, size, search);
        Long totalCount = devreKartiService.getTotalCount(search);

        model.addAttribute("kartlar", kartlar);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("search", search);

        return "kartlar_listesi";
    }

    @GetMapping("/kart-pin-form")
    public String showKartPinForm(Model model) {
        Integer totalCount = devreKartiService.getTotalCount(null).intValue();
        List<DevreKarti> kartlar = devreKartiService.getKartlar(0,totalCount,null);
        model.addAttribute("kartlar", kartlar);
        return "kart_form"; // JSP dosya adı
    }

    @GetMapping("/kart/{kartId}/config")
    public String kartConfigSayfasi(
            @PathVariable("kartId") Long kartId,
            Model model) {
        DevreKarti kart = devreKartiService.getKartDetay(kartId);
        model.addAttribute("kart", kart);
        return "kart_config";
    }

    @PostMapping("/kart/{kartId}/config/save")
    public ResponseEntity<String> kartConfigSave(
            @PathVariable("kartId") Long kartId,
            @RequestParam("isim") String isim,
            @RequestParam("pinValues") String pinValues
    ) {
        try {
            // Basit validasyon
            if (isim == null || isim.isBlank()) {
                return ResponseEntity.badRequest().body("İsim boş olamaz!");
            }
            if (pinValues == null || pinValues.isBlank()) {
                return ResponseEntity.badRequest().body("En az bir pin seçmelisiniz!");
            }
            Boolean result = pinConfigService.saveOrUpdate(null, isim, pinValues, kartId);
            if (!result) {
                return ResponseEntity.status(403).body(null);
            }
            return ResponseEntity.ok("Konfigürasyon kaydedildi!");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Sunucuda bir hata oluştu\nDaha sonra tekrar deneyiniz.");
        }
    }

}
