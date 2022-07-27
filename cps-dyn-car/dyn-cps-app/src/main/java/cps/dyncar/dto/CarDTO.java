package cps.dyncar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    //信号灯信息 便于调试
    private Integer leftTime;
    private Integer lightColor;


    private String vid; // 车辆Id
    private double lat; // 纬度
    private double lon;
    private double speed;
//    private double suggestSpeed;
    private String suggest;
    private int gpsType; // gcj82:1
}
