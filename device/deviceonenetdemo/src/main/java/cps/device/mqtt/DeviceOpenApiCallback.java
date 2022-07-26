package cps.device.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import com.onenet.studio.acc.sdk.interfaces.OpenApiCallback;
import cps.device.bdgpsdevice.BdGpsOpenApiExtention;
import cps.device.engine.EngineOpenApiExtention;
import cps.device.gpsdevice.GpsOpenApiExtention;

/**
 * 收到oneNet的设备属性设置消息，在此回调类模拟真实物理设备设置完属性后，向oneNet发送响应消息
 */
public class DeviceOpenApiCallback implements OpenApiCallback {

    private OpenApi openApi;
    private String productId;

    @Override
    public void callback(String oneJson) {
        JSONObject mqttMessage = JSONObject.parseObject(oneJson);
        JSONObject params = mqttMessage.getJSONObject("params");
        try {
            if ("710r9BWp9V".equals(productId)) {
                GpsOpenApiExtention extention = new GpsOpenApiExtention(openApi);
                //回应消息
                extention.propertySetReply(mqttMessage.getString("id"), 200, "GPS改完了！");
                //数据上报
                extention.propertyUpload(5000,
                        params.getString("latiTude"),
                        params.getString("longiTude"),
                        params.getInteger("runningState"));

            } else if ("5P2quw1MIt".equals(productId)) {    //北斗gps
                BdGpsOpenApiExtention extention = new BdGpsOpenApiExtention(openApi);
                //回应消息
                extention.propertySetReply(mqttMessage.getString("id"), 200, "北斗GPS改完了！");
                //数据上报
                extention.propertyUpload(5000, null,
                        params.getString("latiTudeBD"),
                        params.getString("longiTudeBD"),
                        params.getInteger("runningStateBD"));

            } else if ("5f5cUXBTC7".equals(productId)) {    //引擎
                EngineOpenApiExtention extention = new EngineOpenApiExtention(openApi);
                //回应消息
                extention.propertySetReply(mqttMessage.getString("id"), 200, "引擎改完了！");
                //数据上报
                extention.propertyUpload(5000,
                        params.getInteger("powerStatus"),
                        params.getDouble("temperature"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("属性设置回调异常！" + e.getMessage());
        }
    }

    public void setOpenApi(OpenApi openApi) {
        this.openApi = openApi;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
