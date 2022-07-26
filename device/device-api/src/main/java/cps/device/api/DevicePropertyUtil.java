package cps.device.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DevicePropertyUtil {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    //速度
    public static Integer getSpeedValue() {
        return Integer.parseInt(df.format(Math.random() * 100));
    }
//    public static Double getDoubleValueSection(String dataType) {
//        if ("abnormalTemp".equals(dataType)) {
//            // 异常温度
//            return BigDecimal.valueOf(18 + Math.random() * (28 - 18)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//        } else if ("normalTemp".equals(dataType)) {
//            // 正常温度
//            return BigDecimal.valueOf(18 + Math.random() * (27 - 18)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//        } else if ("abnormalHum".equals(dataType)) {
//            // 异常湿度
//            return BigDecimal.valueOf(40 + Math.random() * (61 - 40)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//        } else if ("normalHum".equals(dataType)) {
//            // 正常湿度
//            return BigDecimal.valueOf(40 + Math.random() * (60 - 40)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//        } else if ("tarTemp".equals(dataType)) {
//            return BigDecimal.valueOf(18 + Math.random() * (22 - 18)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//        } else if ("tarHum".equals(dataType)) {
//            return BigDecimal.valueOf(40 + Math.random() * (50 - 40)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//        } else if ("air".equals(dataType)) {
//            return BigDecimal.valueOf(1050 + Math.random() * (2000 - 1050)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//        } else {
//            return null;
//        }
//    }
}
