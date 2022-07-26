package com.onenet.studio.acc.sdk.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.lang.String;

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
