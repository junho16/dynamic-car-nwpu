package cps.device.onenetapi.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.lang.Double;
import java.lang.String;

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
      name = "suggest"
  )
  private String suggest;

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

  public void setSuggest(String suggest) {
    this.suggest = suggest;
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

  public String getSuggest() {
    return this.suggest;
  }
}
