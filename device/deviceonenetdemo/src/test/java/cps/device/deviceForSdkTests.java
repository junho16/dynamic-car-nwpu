package cps.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import cps.device.bdgpsdevice.BdGpsOpenApiExtention;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * 设备SDK测试类
 */
public class deviceForSdkTests {

    private static final Logger log = LoggerFactory.getLogger(deviceForSdkTests.class);

    String url = "tcp://218.201.45.7:1883"; //url: ssl://183.230.102.116:8883  加密连接     HOST
    String productId = "L2qXMm0D5U";        //产品ID  USERNAME
    String devKey = "temp_humi_gather";     //设备名称  CLIENTID
    String accessKey = "1+XbyX/V2//tsZcBc0s5b5n3x4XXq/07W8djESkHfaU=";  //产品key

    //设备SDK相关
    OpenApi openApi;
    OpenApiExtentionForSdkTest openApiExtention;

    public void openApi() throws Exception {
        openApi = OpenApi.Builder.newBuilder()
                .url(url)
                .productId(productId)
                .devKey(devKey)
                .accessKey(accessKey)
                .build();

        openApiExtention = new OpenApiExtentionForSdkTest(openApi);
    }

    /**
     * 属性上报测试
     */
    @Test
    public void propertyTest(){
        Double currentHumidity = 21.0;
        Double currentTemperature = 21.0;
        long timeout = 5000L;
        try {
            this.openApi();
            int humRes = openApiExtention.CurrentHumidityPropertyUpload(currentHumidity, timeout);
            int temRes = openApiExtention.CurrentTemperaturePropertyUpload(currentTemperature, timeout);
            System.out.println("up result: humRes=>"+humRes+",temRes=>"+temRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量设备上线并实现数据上报
     */
    @Test
    public void batchIoTDevicesManage(){
        //产品id默认北斗GPS设备
        String product_id = "5P2quw1MIt";

        String res = readJsonFile("/bdDevice.json");
        JSONArray bdDevices = JSON.parseArray(res);
        int statue = 0;
        if (bdDevices != null) {
            for (int i = 0; i < bdDevices.size(); i++) {
                //设备json数据
                JSONObject device = bdDevices.getJSONObject(i);
                try {
                    OpenApi openApi = OpenApi.Builder.newBuilder()
                            .url(url)
                            .productId(product_id)
                            .devKey(device.getString("name"))
                            .accessKey(device.getString("sec_key"))
                            .build();
                    BdGpsOpenApiExtention extention = new BdGpsOpenApiExtention(openApi);
                    //经度
                    extention.latiTudeBDPropertyUpload(getLatiTudeValue(), 5000);
                    //纬度
                    extention.longiTudeBDPropertyUpload(getLongiTudeValue(), 5000);
                    //运行状态
                    extention.runningStateBDPropertyUpload(statue, 5000);
                    if (statue == 0) {
                        statue = 1;
                    } else {
                        statue = 0;
                    }
                    log.info("openApi==>{}==>{}", device.getString("name"), openApi);
                } catch (Exception e) {
                    log.error("连接异常：productId：{}，deviceName：{}，sec_key：{}", productId, device.getString("name"), device.getString("sec_key"));
                }
            }
        }
    }

    private DecimalFormat df = new DecimalFormat("0.000000");

    //经度
    private String getLatiTudeValue() {
        return df.format(Math.random() * 180);
    }

    //经度
    private String getLongiTudeValue() {
        return df.format(Math.random() * 900);
    }

    //读取json文件
    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            InputStream inputStream = DeviceDemoApplication.class.getResourceAsStream(fileName);
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
}
