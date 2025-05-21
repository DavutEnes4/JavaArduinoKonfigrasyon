package tr.com.my_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.com.my_app.service.MotorService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * MotorController: JSP paneli ve JSON API endpointlerini barındırır.
 */
@Controller
@RequestMapping("/motor")
public class MotorController {

    private final MotorService motorService;

    @Autowired
    public MotorController(MotorService motorService) {
        this.motorService = motorService;
    }


    @GetMapping("/panel")
    public String motorPanel() {
        return "motor/motor_control";
    }

    /**
     * REST endpoint: seri port listesini JSON olarak döner.
     */
    @GetMapping(path = "/get_ports", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Map<String, String>> getPorts() {
        return motorService.getAvailablePorts();
    }

    /**
     * REST endpoint: komut alır, seri porta yollar.
     */
    @PostMapping(path = "/kontrol_komut",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> kontrolKomut(@RequestBody Map<String, Object> payload) {
        String komut = (String) payload.get("komut");
        String port = (String) payload.get("port");
        Integer baudrate = (Integer) payload.get("baudrate");

        if (komut == null || port == null || baudrate == null) {
            return Map.of("status", "error", "message", "Eksik parametre");
        }

        boolean ok = motorService.sendCommand(port, baudrate, komut);
        return ok ? Map.of("status", "ok")
                : Map.of("status", "error", "message", "Komut gönderilemedi");
    }
}
