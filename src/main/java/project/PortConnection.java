/*
package project;

import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller //I think we can treat it as controller - tbd
public class PortConnection {
    private static final int PORT_NO = 1;
    private final StringBuilder measurement;
    private final SerialPort comPort;
    private final project.backend.UserService userService;

    public PortConnection(StringBuilder measurements, SerialPort inputPort, project.backend.UserService userService) {
        this.comPort = inputPort;
        this.measurement = measurements;
        this.userService = userService;
    }

    public StringBuilder getAuthorization() {
        InputStream in = comPort.getInputStream();

        try {
            int bytesPerPacket = getBytesPerPacket();
            byte[] buffer = new byte[bytesPerPacket];

            // Czekaj, aż dane będą dostępne
            while (in.available() < bytesPerPacket) {
                Thread.sleep(100);
            }

            // Odczytaj dane do bufora
            in.read(buffer, 0, bytesPerPacket);

            // Konwertuj bajty do Stringa
            measurement.append(new String(buffer));
        } catch (InterruptedException | IOException exception) {
            log.error("Something happened during reading buffer, message: {}", exception.getMessage(), exception);
        } finally {
            comPort.flushIOBuffers();
        }



        return new StringBuilder(userService.authorize(measurement.toString())); // pokemon, zmienie w next commicie kiedyśtam xd
    }

    private int getBytesPerPacket() throws InterruptedException {
        Thread.sleep(3000);
        return comPort.bytesAvailable();
    }

    //dla testow
    public static void main(String[] args) {

        for (int i = 0; i < 20; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            SerialPort comPort = SerialPort.getCommPorts()[PORT_NO];
            comPort.openPort();
//            PortConnection connection = new PortConnection(stringBuilder, comPort, new AuthorizingServicePoC(n));
//            StringBuilder measurement = connection.getAuthorization();
//            System.out.println(measurement);
            comPort.closePort();
        }

    }
}