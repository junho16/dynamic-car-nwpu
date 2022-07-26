package cps.device.mqtt;

import com.onenet.studio.acc.sdk.OpenApi;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备的消息订阅活发布类，用于设备与oneNet的mqtt消息沟通
 */
@Component
public class DeviceTopicSubOrPub {
    @Resource
    ConcurrentHashMap<String, List<OpenApi>> openApis;

    @PostConstruct
    public void init() {
        try {
            for (String productId : openApis.keySet()){
                for (OpenApi openApi:openApis.get(productId)){
                    DeviceOpenApiCallback callback = new DeviceOpenApiCallback();
                    callback.setProductId(productId);
                    callback.setOpenApi(openApi);
                    openApi.propertySetSubscribe(callback);
                    if ("710r9BWp9V".equals(productId)) {   //gps
                        openApi.serviceInvokeSubscribe("gpsStateControl",callback);
                    } else if ("5P2quw1MIt".equals(productId)) {    //北斗gps
                        openApi.serviceInvokeSubscribe("gpsStateControlBD",callback);
                    } else if ("5f5cUXBTC7".equals(productId)) {    //引擎
                        openApi.serviceInvokeSubscribe("setEngineState",callback);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
