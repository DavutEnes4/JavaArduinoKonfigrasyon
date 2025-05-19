package tr.com.my_app.service;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

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
     * @param command   Tek karakterlik komut, örn. 'I','G','L','R','S'
     * @return başarılıysa true, açma/yazma hatasında false
     */
    public boolean sendCommand(String portName, int baudRate, char command) {
        SerialPort port = SerialPort.getCommPort(portName);
        port.setBaudRate(baudRate);
        port.setNumDataBits(8);
        port.setNumStopBits(SerialPort.ONE_STOP_BIT);
        port.setParity(SerialPort.NO_PARITY);

        if (!port.openPort()) {
            return false;
        }
        try {
            byte[] b = new byte[]{ (byte) command };
            port.writeBytes(b, 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            port.closePort();
        }
    }
}
