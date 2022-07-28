package cps.device.dyncar.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynCarDTO {

    private String rid;

    private Double lon;

    private Double lat;

    private Double speed;

    private String suggest;

    private Long ts;

}
