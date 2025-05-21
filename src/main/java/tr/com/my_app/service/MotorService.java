package tr.com.my_app.service;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MotorService {

    /**
     * Seri port listesini alır.
     * Dönen List<Map> içinde her eleman:
     *   "device"      → port.getSystemPortName()
     *   "description" → port.getDescriptivePortName()
     */
    public List<Map<String,String>> getAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("[MotorService] Bulunan portlar: " +
                Arrays.stream(ports)
                        .map(SerialPort::getSystemPortName)
                        .collect(Collectors.joining(","))
        );
        List<Map<String,String>> list = new ArrayList<>();
        for (SerialPort p : ports) {
            Map<String,String> info = new HashMap<>();
            info.put("device", p.getSystemPortName());
                    info.put("description", p.getDescriptivePortName());
                            list.add(info);
        }
        return list;
    }


    /**
     * Verilen port, baudrate ve komut char’ını seri porta yollar.
     * @param portName  Örn. "COM3" veya "/dev/ttyUSB0"
     * @param baudRate  Örn. 9600
     * @param commandLine   Tek karakterlik komut, örn. 'I','G','L','R','S'
     * @return başarılıysa true, açma/yazma hatasında false
     */
    public boolean sendCommand(String portName, int baudRate, String commandLine) {

        SerialPort port = SerialPort.getCommPort(portName);
        port.setBaudRate(baudRate);
        port.setNumStopBits(SerialPort.ONE_STOP_BIT);
        port.setParity(SerialPort.NO_PARITY);
        port.setNumDataBits(8);

        port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        port.clearDTR();  // reset'i önlemeye çalış
        port.clearRTS();  // bazen bu da gerekir

        if (!port.openPort(1000)) {
            System.err.println("[MotorService] Port açılamadı!");
            return false;
        }

        try {
            Thread.sleep(1200);

            String msg = commandLine + "\n";
            byte[] out = msg.getBytes(StandardCharsets.US_ASCII);

            int written = port.writeBytes(out, out.length);
            System.out.println("[MotorService] → " + msg.trim()
                    + "  (" + written + " bayt)");
            return written == out.length;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            port.closePort();
        }
    }


}
