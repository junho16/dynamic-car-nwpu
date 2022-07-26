package cps.device.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备连接上线初始化获取openapi的实例化类，用于提供通过读取配置文件中设备信息进行获取对应设备的openapi交由容器管理
 */
@Component
public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    //设备接入路径
    @Value("${devices.bean.url}")
    private String url;

    //北斗GPS设备产品id
    @Value("${devices.bean.bdgps_product_id}")
    private String bdGpsProductId;

    //北斗GPS设备json文件名
    @Value("${devices.bean.bdgps_filename}")
    private String bdGpsFilename;

    //gps设备产品id
    @Value("${devices.bean.gps_product_id}")
    private String gpsProductId;

    //gps设备json文件名
    @Value("${devices.bean.gps_filename}")
    private String gpsFilename;

    //引擎设备产品id
    @Value("${devices.bean.engine_product_id}")
    private String engineProductId;

    //引擎设备json文件名
    @Value("${devices.bean.engine_filename}")
    private String engineFilename;

    /**
     * 创建设备的openAPI
     *
     * @return List<OpenApi>
     */
    @Bean
    public ConcurrentHashMap<String,List<OpenApi>> creatBdGpsDeviceOpenAPI() {
        ConcurrentHashMap<String,List<OpenApi>> openApis = new ConcurrentHashMap<>();

        //创建北斗GPS设备的openApi
        List<OpenApi> bdGpsOpenApis = new ArrayList<>();
        JSONArray bdDevices = JSON.parseArray(this.readJsonFile(bdGpsFilename));
        if (bdDevices != null) {
            for (int i = 0; i < bdDevices.size(); i++) {
                //设备json数据
                JSONObject device = bdDevices.getJSONObject(i);
                try {
                    OpenApi openApi = OpenApi.Builder.newBuilder()
                            .url(url)
                            .productId(bdGpsProductId)
                            .devKey(device.getString("name"))
                            .accessKey(device.getString("sec_key"))
                            .build();
                    bdGpsOpenApis.add(openApi);
                } catch (Exception e) {
                    log.error("北斗GpsOpenApi创建异常：productId：{}，deviceName：{}，sec_key：{}", bdGpsProductId, device.getString("name"), device.getString("sec_key"));
                }
            }
        }
        if (bdGpsOpenApis.size()>0){
            openApis.put(bdGpsProductId,bdGpsOpenApis);
        }
        //创建gps的openAPI
        List<OpenApi> gpsOpenApis = new ArrayList<>();
        JSONArray gpsDevices = JSON.parseArray(this.readJsonFile(gpsFilename));
        if (gpsDevices != null) {
            for (int i = 0; i < gpsDevices.size(); i++) {
                //设备json数据
                JSONObject device = gpsDevices.getJSONObject(i);
                try {
                    OpenApi openApi = OpenApi.Builder.newBuilder()
                            .url(url)
                            .productId(gpsProductId)
                            .devKey(device.getString("name"))
                            .accessKey(device.getString("sec_key"))
                            .build();
                    gpsOpenApis.add(openApi);
                } catch (Exception e) {
                    log.error("GpsOpenApi创建异常：productId：{}，deviceName：{}，sec_key：{}", gpsProductId, device.getString("name"), device.getString("sec_key"));
                }
            }
        }
        if (gpsOpenApis.size()>0){
            openApis.put(gpsProductId,gpsOpenApis);
        }

        //创建引擎的openAPI
        List<OpenApi> engineOpenApis = new ArrayList<>();
        JSONArray engineDevices = JSON.parseArray(this.readJsonFile(engineFilename));
        if (engineDevices != null) {
            for (int i = 0; i < engineDevices.size(); i++) {
                //设备json数据
                JSONObject device = engineDevices.getJSONObject(i);
                try {
                    OpenApi openApi = OpenApi.Builder.newBuilder()
                            .url(url)
                            .productId(engineProductId)
                            .devKey(device.getString("name"))
                            .accessKey(device.getString("sec_key"))
                            .build();
                    engineOpenApis.add(openApi);
                } catch (Exception e) {
                    log.error("engineOpenApi创建异常：productId：{}，deviceName：{}，sec_key：{}", gpsProductId, device.getString("name"), device.getString("sec_key"));
                }
            }
        }
        if (engineOpenApis.size()>0){
            openApis.put(engineProductId,engineOpenApis);
        }

        return openApis;
    }

    /**
     * 读取json文件
     * @param fileName 文件路径
     * @return 文件内容
     */
    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            InputStream inputStream = BeanFactory.class.getResourceAsStream(fileName);
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
