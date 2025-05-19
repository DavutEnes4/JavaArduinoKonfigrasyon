package tr.com.my_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tr.com.my_app.model.DevreKarti;
import tr.com.my_app.model.Pin;
import tr.com.my_app.model.PinConfig;
import tr.com.my_app.service.PinConfigService;

import java.util.*;

@Controller
@RequestMapping("/configlar")
public class PinConfigController {

    @Autowired
    PinConfigService pinConfigService;

    @GetMapping()
    public String configList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "search", required = false) String search,
            Model model
    ) {
        int start = page * size;
        List<PinConfig> pinConfigs = pinConfigService.getPinConfigList(start, size, search);
        Long totalCount = pinConfigService.getTotalCount(search);

        model.addAttribute("configlar", pinConfigs);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("search", search);

        return "pin_config_list";
    }

    @PostMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,String> deleteConfig(@RequestBody Map<String,Long> payload) {
        Long id = payload.get("id");
        boolean ok = pinConfigService.deletePinConfig(id);
        if (ok) {
            return Map.of("status","ok");
        } else {
            return Map.of("status","error","message","Silme başarısız");
        }
    }

    @PostMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,String> uploadConfig(@RequestBody Map<String,Long> payload) {
        Long id = payload.get("id");
        // burada Arduino’ya gönderme veya başka iş yap
        return Collections.singletonMap("status","ok");
    }

    /*** 4a) Düzenleme formu GET ***/
    @GetMapping("/edit")
    public String editForm(
            @RequestParam("id") Long id,
            Model model,
            Locale locale
    ) {
        PinConfig config = pinConfigService.getPinConfig(id);
        if (config == null) {
            return "redirect:/configlar";
        }
        model.addAttribute("config", config);
        model.addAttribute("lang", locale.getLanguage());
        return "pin_config_edit";
    }

    /*** 4b) Düzenleme submit POST ***/
    @PostMapping("/edit")
    public String editSubmit(
            @RequestParam("id") Long id,
            @RequestParam("adi") String adi,
            @RequestParam("pinValues") String pinValues
    ) {
        PinConfig config = pinConfigService.getPinConfig(id);
        if (config != null) {
            Long pinKartId = config.getDevreKarti().getId();
            pinConfigService.saveOrUpdate(id,adi,pinValues,pinKartId);
        }
        return "redirect:/configlar";
    }
}
