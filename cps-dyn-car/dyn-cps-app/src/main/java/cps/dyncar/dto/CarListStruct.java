package cps.dyncar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarListStruct {

  private String carId;

  private double latitude;

  private double longitude;

  private double speed;

  private int lane;

  private double distanceToLine;

}
