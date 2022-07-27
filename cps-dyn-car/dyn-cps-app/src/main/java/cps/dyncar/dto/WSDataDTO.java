package cps.dyncar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSDataDTO {
    private Long timeStamp;
    private String did; // 设备序列号
    private List<CarDTO> vehicles;
}
