package cps.cpsruntimeui.controller;

import cps.IoTBridge.api.IoTBridge;
import cps.IoTBridge.api.PlatformQueryException;
import com.alibaba.fastjson.JSONObject;

import cps.api.entity.CPSInstance;
import cps.api.entity.meta.CPEntityMeta;
import cps.api.entity.meta.DeviceMeta;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cps.cpsruntimeui.unit.NacosServiceClient;
import cps.runtime.api.service.CPEntityException;
import cps.runtime.api.service.CPEntityService;
import cps.runtime.api.service.CPSInstanceException;
import cps.runtime.api.service.CPSInstanceFactory;
import cps.runtime.api.service.DeviceException;
import cps.runtime.api.service.DeviceService;

/**
 * 接口调用
 */
@RestController
@RequestMapping(value = "/consumer")
public class ConsumerController {


    @Autowired
    private RestTemplate  restTemplate;  // 将RestTemplate 对象注入到了 spring 容器中直接引用

    /**
     * 1.使用RestTemplate进行接口调用
     * @return
     */
    @GetMapping("/restTemplateTest")
    public String restTemplateTest() {
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject("http://localhost:1001/test",String.class);
        //使用LoadBalanced 直接使用注册服务名调用接口
        String result = restTemplate.getForObject("http://cpsruntimeservice/test",String.class);
        return "Return : " + result;
    }

    @Autowired
    private NacosServiceClient nacosServiceClient;

    /**
     * 2.feign接口调用
     * @return
     */
    @GetMapping("/feignTest")
    public String feignTest() {
        return nacosServiceClient.testParam("nacos");
    }

    @Reference
    private IoTBridge ioTBridge;

    @GetMapping("/testNacosService")
    public String testNacosService() throws PlatformQueryException {
        return ioTBridge.queryIoTMeta();
    }
    @Reference
    public DeviceService deviceService;

    @GetMapping("/deviceMeta")
    public String DeviceMetaTest() throws DeviceException {
        DeviceMeta deviceMeta = deviceService.getDeviceMetaByUUID("1");
        return JSONObject.toJSONString(deviceMeta);
    }

    @Reference
    public CPEntityService cpEntityService;

    @GetMapping("/cpEntityMeta")
    public String CPEntityMetaTest() throws CPEntityException {
        CPEntityMeta cpEntityMeta = cpEntityService.getCPEntityMetaByUUID("1");
        return JSONObject.toJSONString(cpEntityMeta);
    }

    @Reference
    public CPSInstanceFactory cpsInstanceFactory;

    @GetMapping("/buildCPSInstanceTest")
    public String buildCPSInstanceTest() throws DeviceException, CPSInstanceException, CPEntityException {
        CPSInstance cpsInstance = cpsInstanceFactory.buildCPSInstance("1");
        return JSONObject.toJSONString(cpsInstance);
    }
}
