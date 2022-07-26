package cps.device.engine.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class TemperatureWarningStructDTO {
  @JSONField(
      name = "temperature"
  )
  private Double temperature;

  @JSONField(
      name = "message"
  )
  private String message;

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Double getTemperature() {
    return this.temperature;
  }

  public String getMessage() {
    return this.message;
  }
}
