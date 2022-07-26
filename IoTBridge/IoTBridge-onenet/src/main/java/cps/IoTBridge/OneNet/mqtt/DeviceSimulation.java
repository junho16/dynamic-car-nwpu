package cps.IoTBridge.OneNet.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cps.IoTBridge.api.IoTBridge;
import cps.IoTBridge.api.SendMessageException;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 功能描述
 * 解决服务器无法支撑更多设备接入IOT,将设备虚拟上报设备消息
 *
 * @author: mdd
 * @date: 2022年06月22日 11:25
 */
@Component
@EnableScheduling
public class DeviceSimulation {

    private static final Logger log = LoggerFactory.getLogger(DeviceSimulation.class);

    @Reference
    private IoTBridge ioTBridge;

//    @Scheduled(cron = "* */5 * * * ?")
    @Scheduled(cron = "*/3 * * * * ?")
    public void propertyPCUpload() {
        JSONObject devSimulation = JSON.parseObject(this.readJsonFile("/deviceSimulation_car.json"));
        if(0 == 1)
            for (String key : devSimulation.keySet()) {
            String devValue = devSimulation.getString(key);
            JSONArray devInfos = JSON.parseArray(devValue);
            for (int i = 0; i < devInfos.size(); i++) {
                JSONObject msgJson = new JSONObject();
                JSONObject devInfo = devInfos.getJSONObject(i);
                msgJson.put("productId", key);
                msgJson.put("deviceName", devInfo.getString("name"));

                JSONObject params = new JSONObject();
//                params.put("status", "on");
                params.put("time", new Date().getTime());
                /**
                 * 加上车辆模拟设备--但是发现从onenet上来的数据并不是从这里模拟的，应该在MQTTlistener里
                 * 这里先写一下测试一下
                 */
                if("syxHSBqS8g".equals(key)){
                    Integer value = getIntegerValueSection("Speed");
                    log.info("时刻："+ new Date().getTime() +" 模拟上传的速度为：" + value);
                    params.put("CarSpeed", value);
                }else if ("7e6DFz0zgU".equals(key)) {
                    String name = devInfo.getString("name");
                    int index = Integer.parseInt(name.substring(name.lastIndexOf("_") + 1));
                    if (index <= 104) {
                        params.put("ActualPower", getPcVal("ec"));
                    } else {
                        params.put("ActualPower", getPcVal("ac"));
                    }
                } else if ("0P4d6cee10".equals(key)) {
                    params.put("CurrentTemperature", getDoubleValueSection("Temp"));
                    params.put("CurrentHumidity", getDoubleValueSection("Hum"));
                }
                msgJson.put("data", params);
//                System.out.println(msgJson.toJSONString());
                log.info("时刻："+ new Date().getTime() + "上传的数据："+msgJson.toJSONString());
                try {
                    ioTBridge.sendDevicePropertyToCPS(msgJson.toJSONString());
                } catch (SendMessageException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    /**
     * 读取json文件
     *
     * @param fileName 文件路径
     * @return 文件内容
     */
    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            InputStream inputStream = DeviceSimulation.class.getResourceAsStream(fileName);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = inputStream.read()) != -1) {
                sb.append((char) ch);
            }
            inputStream.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Float getPcVal(String devKey) {
        Float val = null;
        if ("ac".equals(devKey)) {
            val = Float.parseFloat(df.format(990 + Math.random() * (1400 - 990)));
        } else if ("ec".equals(devKey)) {
            val = Float.parseFloat(df.format(1700 + Math.random() * (2000 - 1700)));
        }
        return val;
    }

    public static Integer getIntegerValueSection(String dataType) {
        if("Speed".equals(dataType)){
            return (int) (Math.random() * 100);
        }
        return null;
    }

    public static Double getDoubleValueSection(String dataType) {
        if ("Temp".equals(dataType)) {
            // 正常温度
            return BigDecimal.valueOf(18 + Math.random() * (27 - 18)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        } else if ("Hum".equals(dataType)) {
            // 正常湿度
            return BigDecimal.valueOf(40 + Math.random() * (60 - 40)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        } else if ("tarTemp".equals(dataType)) {
            return BigDecimal.valueOf(18 + Math.random() * (22 - 18)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        } else if ("tarHum".equals(dataType)) {
            return BigDecimal.valueOf(40 + Math.random() * (50 - 40)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        } else if ("air".equals(dataType)) {
            return BigDecimal.valueOf(1050 + Math.random() * (2000 - 1050)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        } else {
            return null;
        }
    }
}
