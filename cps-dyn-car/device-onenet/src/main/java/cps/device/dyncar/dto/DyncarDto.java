package cps.device.dyncar.dto;

import lombok.Data;

/**
 * @author Junho
 * @date 2022/7/26 20:35
 */
@Data
public class DyncarDto {

    private String rid;

    private Double lon;

    private Double lat;

    private Double speed;

    private String suggest;

}
