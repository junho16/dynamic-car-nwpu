package cps.device.bdgpsdevice.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class $GeoLocationStructDTO {
  @JSONField(
      name = "longitude"
  )
  private Double longitude;

  @JSONField(
      name = "latitude"
  )
  private Double latitude;

  @JSONField(
      name = "altitude"
  )
  private Double altitude;

  @JSONField(
      name = "coordinateSystem"
  )
  private Integer coordinateSystem;

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public void setAltitude(Double altitude) {
    this.altitude = altitude;
  }

  public void setCoordinateSystem(Integer coordinateSystem) {
    this.coordinateSystem = coordinateSystem;
  }

  public Double getLongitude() {
    return this.longitude;
  }

  public Double getLatitude() {
    return this.latitude;
  }

  public Double getAltitude() {
    return this.altitude;
  }

  public Integer getCoordinateSystem() {
    return this.coordinateSystem;
  }
}
