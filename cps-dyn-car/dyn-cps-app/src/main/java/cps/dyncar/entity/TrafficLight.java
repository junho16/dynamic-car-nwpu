package cps.dyncar.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junho
 * @date 2022/7/10 17:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficLight {
    private Double latitude;

    private Double longitude;

    private Integer color;

    private Integer leftTime;
}
