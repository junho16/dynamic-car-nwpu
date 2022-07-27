package cps.device.onenetapi.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DynCarListStructDTO {
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
      name = "ts"
  )
  private Long ts;

  public void setRid(String rid) {
    this.rid = rid;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  public void setTs(Long ts) {
    this.ts = ts;
  }

  public String getRid() {
    return this.rid;
  }

  public Double getLon() {
    return this.lon;
  }

  public Double getLat() {
    return this.lat;
  }

  public Double getSpeed() {
    return this.speed;
  }

  public Long getTs() {
    return this.ts;
  }
}
