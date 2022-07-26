package cps.device.engine.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class StartTheAbnormalStructDTO {
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
