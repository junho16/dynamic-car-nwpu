package cps.device.bdgpsdevice.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class LOS_Warning_BDStructDTO {
  @JSONField(
      name = "message"
  )
  private String message;

  @JSONField(
      name = "local"
  )
  private String local;

  public void setMessage(String message) {
    this.message = message;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public String getMessage() {
    return this.message;
  }

  public String getLocal() {
    return this.local;
  }
}
