package cps.app;

import com.alibaba.fastjson.JSON;
import com.github.cm.heclouds.onenet.studio.api.IotClient;
import com.github.cm.heclouds.onenet.studio.api.IotProfile;
import com.github.cm.heclouds.onenet.studio.api.auth.SignatureMethod;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.QueryDevicePropertyRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.QueryDevicePropertyResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.SetDevicePropertyRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.SetDevicePropertyResponse;
import com.github.cm.heclouds.onenet.studio.api.exception.IotClientException;
import com.github.cm.heclouds.onenet.studio.api.exception.IotServerException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class cpsAppTests {

    //用户ID
    private final String userId = "247205";
    //用户accessKey
    private final String accessKey = "wvnRbP3vfm12WCMvV/Keo4CILSx6jkCwOmxs+c9G+eZJAvlyMm2BSrwBtkre7UDiJv4x5prjShrnJz+eVE1mMA==";

    private String projectId = "oJTWFD";
    private String productId = "L2qXMm0D5U";
    private String deviceName = "temp_humi_gather";

    IotClient client;

    @Before
    public void initClient() {
        IotProfile profile = new IotProfile();
        profile.userId(userId)
                .accessKey(accessKey)
                .signatureMethod(SignatureMethod.SHA256);
        client = IotClient.create(profile);
    }

    /**
     * 平台设备参数获取
     */
    @Test
    public void cpsPropertyGetTest(){
        QueryDevicePropertyRequest request = new QueryDevicePropertyRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        try {
            QueryDevicePropertyResponse response = client.sendRequest(request);
            System.out.println("query result: "+JSON.toJSONString(response));
        } catch (IotClientException e) {
            e.printStackTrace();
        } catch (IotServerException e) {
            System.out.println(e.getCode());
            e.printStackTrace();
        }finally {
            try {
                this.destroyClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设备参数设置
     */
    @Test
    public void cpsPropertySetTest(){
        SetDevicePropertyRequest proRequest = new SetDevicePropertyRequest();
        proRequest.setProjectId(projectId);
        proRequest.setProductId(productId);
        proRequest.setDeviceName(deviceName);

        proRequest.addParam("CurrentHumidity",3);
        try {
            SetDevicePropertyResponse proResponse = client.sendRequest(proRequest);
            System.out.println(JSON.toJSONString(proResponse));
        } catch (IotClientException e) {
            e.printStackTrace();
        } catch (IotServerException e) {
            System.err.println(e.getCode());
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     * @throws IOException
     */
    public void destroyClient() throws IOException {
        client.close();
    }
}
