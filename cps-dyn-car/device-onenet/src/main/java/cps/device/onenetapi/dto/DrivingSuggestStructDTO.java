package cps.device.onenetapi.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class DrivingSuggestStructDTO {
  @JSONField(
      name = "suggestMsg"
  )
  private String suggestMsg;

  public void setSuggestMsg(String suggestMsg) {
    this.suggestMsg = suggestMsg;
  }

  public String getSuggestMsg() {
    return this.suggestMsg;
  }
}
