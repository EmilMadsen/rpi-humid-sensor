package dk.emad.humidsensor.sensor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HumidSensorLog {
    private Double humidity;
    private Double temperature;
}
