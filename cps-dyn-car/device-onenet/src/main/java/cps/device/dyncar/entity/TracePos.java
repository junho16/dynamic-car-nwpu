package cps.device.dyncar.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junho
 * @date 2022/7/26 20:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TracePos {

    @JSONField(
            name = "rid"
    )
    private String rid;

    @JSONField(
            name = "lon"
    )
    private Double lon;

    @JSONField(
            name = "lat"
    )
    private Double lat;

    @JSONField(
            name = "speed"
    )
    private Double speed;

    @JSONField(
            name = "timeStamp"
    )
    private Long ts;

}
