package cps.device.gpsdevice.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class LOS_WarningStructDTO {
  @JSONField(
      name = "message"
  )
  private String message;

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
