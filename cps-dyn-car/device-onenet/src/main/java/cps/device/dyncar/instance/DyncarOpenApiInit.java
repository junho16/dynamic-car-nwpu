package cps.device.dyncar.instance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import cps.device.dyncar.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Junho
 * @date 2022/7/26 22:53
 */
@Component
@Slf4j
public class DyncarOpenApiInit {

    //设备接入路径
    @Value("${devices.bean.url}")
    private String url;

    //设备产品id
    @Value("${devices.bean.product_id}")
    private String productId;

    //设备json文件名
    @Value("${devices.bean.file_name}")
    private String fileName;

    /**
     * 初始化实时车辆的openAPI
     * @return
     */
    @Bean(name = "dyncarOpenApi")
    public OpenApi creatDeviceOpenAPI() {
        OpenApi openApi = null;
        //设备json数据
        JSONObject device = JSON.parseObject(JsonUtil.readJsonFile(fileName));
        try {
            openApi = OpenApi.Builder.newBuilder()
                .url(url)
                .productId(productId)
                .devKey(device.getString("name"))
                .accessKey(device.getString("sec_key"))
                .build();

        } catch (Exception e) {
            log.error("实时车辆-OpenApi创建异常：productId：{}，deviceName：{}，sec_key：{}", productId, device.getString("name"), device.getString("sec_key"));
        }
        return openApi;
    }
}
