package tr.com.my_app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = MotorService.class)
public class MotorServiceTest {

    @Autowired
    private MotorService motorService;

    @Test
    public void testGetAvailablePorts() {
        List<Map<String, String>> ports = motorService.getAvailablePorts();
        assertNotNull(ports, "Port listesi null olmamalı");
        System.out.println("Bulunan port sayısı: " + ports.size());
        ports.forEach(p -> System.out.println(p.get("device") + " - " + p.get("description")));
    }

    /**
     * Bu test, yalnızca gerçek bir cihaz COM5'e bağlıysa çalıştırılmalıdır.
     * Port adını sistemine göre güncelle: COM5, /dev/ttyUSB0 vs.
     */
    @Test
    public void testSendCommandToRealDevice() {
        String portName = "COM5";  // Gerçek bağlı cihazın portu
        int baudRate = 9600;
        String komut = "S";

        boolean success = motorService.sendCommand(portName, baudRate, komut);
        assertTrue(success, "Komut gönderimi başarılı olmalı (eğer cihaz bağlıysa)");
    }
}
