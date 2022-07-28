package cps.dyncar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DynCarListStruct {

  private String rid;

  private Double lon;

  private Double lat;

  private Double speed;

  private Long ts;

}
