package project.backend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArduinoResponse {
    private String firstRowText;
    private String secondRowText;
    private boolean greenLedOn;

}
