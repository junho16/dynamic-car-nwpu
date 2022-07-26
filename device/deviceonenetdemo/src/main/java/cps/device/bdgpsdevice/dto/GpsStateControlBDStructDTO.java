package cps.device.bdgpsdevice.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class GpsStateControlBDStructDTO {
  @JSONField(
      name = "outParam"
  )
  private String outParam;

  public void setOutParam(String outParam) {
    this.outParam = outParam;
  }

  public String getOutParam() {
    return this.outParam;
  }
}
