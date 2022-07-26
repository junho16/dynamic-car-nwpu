package cps.IoTBridge.OneNet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.cm.heclouds.onenet.studio.api.IotClient;
import com.github.cm.heclouds.onenet.studio.api.IotProfile;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.CallServiceRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.CallServiceResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.AddGroupDeviceRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.AddGroupDeviceResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.CreateGroupRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.CreateGroupResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.*;
import com.github.cm.heclouds.onenet.studio.api.entity.common.*;
import com.github.cm.heclouds.onenet.studio.api.entity.common.Device;
import com.github.cm.heclouds.onenet.studio.api.exception.IotClientException;
import com.github.cm.heclouds.onenet.studio.api.exception.IotServerException;
import cps.IoTBridge.OneNet.mq.KafkaProducer;
import cps.IoTBridge.api.*;
import cps.api.entity.*;
import cps.api.entity.meta.DeviceActionMeta;
import cps.api.entity.meta.DeviceAffairMeta;
import cps.api.entity.meta.DeviceAttributeMeta;
import cps.api.entity.meta.DeviceMeta;
import cps.api.entity.meta.DeviceTypeEnum;
import cps.runtime.api.entity.imp.MemoryDevice;
import cps.runtime.api.service.DeviceException;
import cps.runtime.api.service.DeviceService;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IoTOneNetBridge implements IoTBridge {

    private static final Logger logger = LoggerFactory.getLogger(IoTOneNetBridge.class);

    //用户ID
    @Value("${oneNet.userConfig.user_id}")
    private String userId;
//    private String userId = "247205";

    //用户accessKey
    @Value("${oneNet.userConfig.access_key}")
    private String accessKey;
//    private String accessKey = "wvnRbP3vfm12WCMvV/Keo4CILSx6jkCwOmxs+c9G+eZJAvlyMm2BSrwBtkre7UDiJv4x5prjShrnJz+eVE1mMA==";

    //项目ID
    @Value("${oneNet.userConfig.project_id}")
    private String projectId;
//    private String projectId = "oJTWFD";

    //kafka消费生产者
    @Resource
    private KafkaProducer kafkaProducer;

    // 设备属性消息队列
    @Value("${topic.oneNetBridge.deviceProperty}")
    private String topicDevicePropertyTopic;

    // 设备事件消息队列
    @Value("${topic.oneNetBridge.deviceEvent}")
    private String topicDeviceEventTopic;

    //设备元数据接口
    @Reference(check = false)
    private DeviceService deviceService;

    @Value("${iotDeviceConfig_filename}")
    public String iotDeviceConfigFilename;

    /**
     * 初始化并构造客户端
     */
    private IotClient getIotClient() {
        IotProfile profile = new IotProfile();
        profile.userId(userId)
                .accessKey(accessKey);
        return IotClient.create(profile);
    }

    /**
     * 获取Iot平台信息
     *
     * @return String
     * @throws PlatformQueryException 平台信息查询异常
     */
    @Override
    public String queryIoTMeta() throws PlatformQueryException {
        JSONObject metaJson = new JSONObject();
        metaJson.put("platformName", "OneNET");
        metaJson.put("platformVersions", "v1");
        metaJson.put("platformDescription", "在物联网应用和真实设备之间搭建高效、稳定、安全的应用平台。");
        return JSON.toJSONString(metaJson);
    }


    /**
     * 获取指定设备的iot元数据
     *
     * @param iotUuid 物联网设备的唯一id号
     * @return IoTDeviceMeta 设备元数据实体
     * @throws DeviceQueryException 设备信息查询异常
     */
    @Override
    public IoTDeviceMeta queryIoTDeviceMeta(String iotUuid) throws DeviceQueryException {
        IotClient client = getIotClient();
        String productId = iotUuid.substring(0, iotUuid.indexOf("_"));
        String deviceName = iotUuid.substring(iotUuid.indexOf("_") + 1);
        QueryDeviceDetailRequest devRequest = new QueryDeviceDetailRequest();
//        QueryThingModelRequest devRequest = new QueryThingModelRequest();
        devRequest.setProductId(productId);
        devRequest.setDeviceName(deviceName);

//        QueryThingModelResponse devResponse = null;
        QueryDeviceDetailResponse devResponse = null;
        try {
            devResponse = client.sendRequest(devRequest);
        } catch (IotClientException | IotServerException e) {
            throw new DeviceQueryException("获取指定设备的iot元数据异常：" + e.getMessage(), e);
        }
        //释放资源
        try {
            client.close();
        } catch (IOException e) {
            throw new DeviceQueryException("客户端资源释放异常：" + e.getMessage(), e);
        }
        System.out.println("this is result " + JSON.toJSONString(devResponse));
        return null;//JSON.toJSONString(metaJson);
    }

    /**
     * 获取平台产品类型清单
     *
     * @param offset 请求起始位置，默认0
     * @param limit  每次请求记录数，默认10
     * @return String
     * @throws PlatformQueryException 平台信息查询异常
     */
    @Override
    public String queryIoTProductTypeList(String offset, String limit) throws PlatformQueryException {
        IotClient client = getIotClient();
        QueryProductListRequest proRequest = new QueryProductListRequest();
        if (StringUtils.isBlank(offset)) {
            offset = "0";
        }
        if (StringUtils.isBlank(limit)) {
            limit = "10";
        }
        //封装请求数据参数
        proRequest.setProjectId(projectId);
        proRequest.setOffset(Integer.parseInt(offset));
        proRequest.setLimit(Integer.parseInt(limit));

        //发送请求
        QueryProductListResponse proResponse = null;
        try {
            proResponse = client.sendRequest(proRequest);
        } catch (IotClientException | IotServerException e) {
            throw new PlatformQueryException("获取平台产品类型清单异常：" + e.getMessage(), e);
        }
        //释放资源
        try {
            client.close();
        } catch (IOException e) {
            throw new PlatformQueryException("客户端资源释放异常：" + e.getMessage(), e);
        }
        return JSON.toJSONString(proResponse);
    }

    /**
     * 获取平台设备列表
     *
     * @param offset 请求记录起始位置，默认 0
     * @param limit  每次请求记录数，默认10, 范围[1, 100]
     * @return String
     * @throws DeviceQueryException 设备信息查询异常
     */
    @Override
    public String queryIoTDeviceList(String productId, String offset, String limit) throws DeviceQueryException {
        IotClient client = getIotClient();
        QueryDeviceListRequest devRequest = new QueryDeviceListRequest();
        if (StringUtils.isBlank(offset)) {
            offset = "0";
        }
        if (StringUtils.isBlank(limit)) {
            limit = "10";
        } else if (Integer.parseInt(limit) > 100) {
            throw new RuntimeException("每次请求记录数范围[1, 100]");
        }
        //封装请求数据参数
        devRequest.setProjectId(projectId);
        devRequest.setOffset(Integer.parseInt(offset));
        devRequest.setLimit(Integer.parseInt(limit));
        devRequest.setProductId(productId);

        //发送请求
        QueryDeviceListResponse devResponse = null;
        try {
            devResponse = client.sendRequest(devRequest);
        } catch (IotClientException | IotServerException e) {
            throw new DeviceQueryException("获取平台设备列表异常：" + e.getMessage(), e);
        }
        //释放资源
        try {
            client.close();
        } catch (IOException e) {
            throw new DeviceQueryException("客户端资源释放异常!：" + e.getMessage(), e);
        }
        return JSON.toJSONString(devResponse);
    }

    /**
     * 获取平台设备详情信息
     *
     * @param iotUuid 物联网设备的唯一id号
     * @return String
     * @throws DeviceQueryException 设备信息查询异常
     */
    @Override
    public String queryIoTDeviceDetail(String iotUuid) throws DeviceQueryException {
        IotClient client = getIotClient();
        String productId = iotUuid.substring(0, iotUuid.indexOf("_"));
        String deviceName = iotUuid.substring(iotUuid.indexOf("_") + 1);
        QueryDeviceDetailRequest devRequest = new QueryDeviceDetailRequest();
        devRequest.setProductId(productId);
        devRequest.setDeviceName(deviceName);

        //发送请求
        QueryDeviceDetailResponse devResponse = null;
        try {
            devResponse = client.sendRequest(devRequest);
        } catch (IotClientException | IotServerException e) {
            throw new DeviceQueryException("获取平台设备详情信息异常：" + e.getMessage(), e);
        }
        //释放资源
        try {
            client.close();
        } catch (IOException e) {
            throw new DeviceQueryException("客户端资源释放异常：" + e.getMessage(), e);
        }
        return JSON.toJSONString(devResponse);
    }

    /**
     * 向设备推送设备属性设置操作报文
     *
     * @param iotUuid    物联网设备的唯一id号
     * @param actionName 操作标识
     * @param params     设备操作报文{"product_id":"","device_name":"","params":{"属性名":"属性值",...}}
     * @return String
     * @throws DeviceActionException 物联网平台设备管理异常
     */
    @Override
    public String sendActionToIoT(String iotUuid, String actionName, ConcurrentHashMap<String, Object> params) throws DeviceActionException {
        IotClient client = getIotClient();
        String productId = iotUuid.substring(0, iotUuid.indexOf("_"));
        String deviceName = iotUuid.substring(iotUuid.indexOf("_") + 1);
        CallServiceRequest serviceRequest = new CallServiceRequest();
        serviceRequest.setProjectId(projectId);
        serviceRequest.setProductId(productId);
        serviceRequest.setDeviceName(deviceName);
        serviceRequest.setIdentifier(actionName);
        //封装请求参数
        for (String key : params.keySet()) {
            serviceRequest.addParam(key, params.get(key));
        }

        //发送请求
        CallServiceResponse serviceResponse = null;
        try {
            serviceResponse = client.sendRequest(serviceRequest);
        } catch (IotClientException | IotServerException e) {
            throw new DeviceActionException("向设备推送设备属性设置操作报文异常：" + e.getMessage(), e);
        }
        //释放资源
        try {
            client.close();
        } catch (IOException e) {
            throw new DeviceActionException("客户端资源释放异常：" + e.getMessage(), e);
        }
        return JSON.toJSONString(serviceResponse);
    }

    /**
     * 将OneNet设备属性上报消息转发至CPS
     *
     * @param iotPropertyMessage 消息内容
     * @throws SendMessageException 消息发送异常
     */
    @Override
    public void sendDevicePropertyToCPS(String iotPropertyMessage) throws SendMessageException {
        try {
            JSONObject propertyMsgJson = JSONObject.parseObject(iotPropertyMessage);
            boolean isNeedSendMsg = false;

            // 获取元数据需要根据设备iotUuid查询到设备元数据
            String iotUuid = propertyMsgJson.getString("productId") + "_" + propertyMsgJson.getString("deviceName");
            DeviceMeta deviceMeta = deviceService.getDeviceMetaByIotUUID(iotUuid);

            if (deviceMeta != null) {
                // 创建设备实体
                MemoryDevice memoryDevice = new MemoryDevice(deviceMeta);

                // 消息的数据值
                JSONObject propertyParams = propertyMsgJson.getJSONObject("data").getJSONObject("params");

                if (propertyParams != null) {
                    for (String propertyKey : propertyParams.keySet()) {
                        // 判断iot消息的的属性名称、属性值给设备赋值。
                        try {
                            memoryDevice.setAttributeByIoTName(propertyKey, JSON.toJSONString(propertyParams.getJSONObject(propertyKey).get("value")));
                            memoryDevice.setUpdateTime(propertyParams.getJSONObject(propertyKey).getString("time"));
                            isNeedSendMsg = true;
                        } catch (UnsupportedAttributeNameException e) {
                            //记录不支持字段异常信息，目前只处理设备需要的映射属性名称，其他属性做以记录
                            logger.warn("消息属性异常信息：{}", e.getMessage());
                        }
                    }
                } else {
                    // 状态属性和位置属性 参数数据在data中
                    propertyParams = propertyMsgJson.getJSONObject("data");
                    for (String propertyKey : propertyParams.keySet()) {
                        // 判断iot消息的的属性名称、属性值给设备赋值。
                        try {
                            if (!"time".equals(propertyKey)) {
                                memoryDevice.setAttributeByIoTName(propertyKey, propertyParams.getString(propertyKey));
                            } else {
                                memoryDevice.setUpdateTime(propertyParams.getString(propertyKey));
                            }
                            isNeedSendMsg = true;
                        } catch (UnsupportedAttributeNameException e) {
                            //记录不支持字段异常信息，目前只处理设备需要的映射属性名称，其他属性做以记录
                            logger.warn("消息属性异常信息：{}", e.getMessage());
                        }
                    }
                    memoryDevice.setUpdateTime(propertyParams.getString("time"));
                }
                if (isNeedSendMsg) {
                    kafkaProducer.send(topicDevicePropertyTopic, memoryDevice.toCPSMessage());
                }
            } else {
////                FIXME-看着心烦 因为除了车 还有很多设备在线
//                if(iotUuid.startsWith("syxHSBqS8g"))
//                    logger.error("无{}对应的设备元数据！", iotUuid);
                logger.error("无{}对应的设备元数据！", iotUuid);
            }
        } catch (Exception e) {
            throw new SendMessageException("设备属性转发异常：{}" + e.getMessage(), e);
        }
    }

    /**
     * 将OneNet设备事件上报消息转发至CPS
     *
     * @param iotEventMessage 消息内容
     * @throws SendMessageException 消息发送异常
     */
    @Override
    public void sendDeviceEventToCPS(String iotEventMessage) throws SendMessageException {
        try {
            JSONObject eventMsgJson = JSONObject.parseObject(iotEventMessage);
            boolean isNeedSendMsg = false;

            // 获取元数据需要根据设备iotUuid查询到设备元数据
            String iotUuid = eventMsgJson.getString("productId") + "_" + eventMsgJson.getString("deviceName");
            DeviceMeta deviceMeta = deviceService.getDeviceMetaByIotUUID(iotUuid);
            if (deviceMeta != null) {
                // 创建设备实体
                MemoryDevice memoryDevice = new MemoryDevice(deviceMeta);

                // 消息的数据值
                JSONObject msgParams = eventMsgJson.getJSONObject("data").getJSONObject("params");
                if (msgParams != null) {
                    for (String msgKey : msgParams.keySet()) {
                        // 判断iot消息的的属性名称是否存在于元数据中。
                        try {
                            // 拼装事件时间
                            JSONObject msgValJson = msgParams.getJSONObject(msgKey).getJSONObject("value");
                            msgValJson.put("alarmTime", msgParams.getJSONObject(msgKey).getString("time"));

                            memoryDevice.setAffairByIoTName(msgKey, msgValJson.toJSONString());
                            memoryDevice.setUpdateTime(msgParams.getJSONObject(msgKey).getString("time"));
                            isNeedSendMsg = true;
                        } catch (UnsupportedAffairNameException e) {
                            //记录不支持字段异常信息，目前只处理设备需要的映射属性名称，其他属性做以记录
                            logger.warn("消息属性异常信息：{}", e.getMessage());
                        }
                    }
                }
                if (isNeedSendMsg) {
                    kafkaProducer.send(topicDeviceEventTopic, memoryDevice.toCPSMessage());
                }
            } else {
////                FIXME-看着心烦 因为除了车 还有很多设备在线
//                if(iotUuid.startsWith("syxHSBqS8g"))
//                    logger.error("无{}对应的设备元数据！", iotUuid);

                logger.error("无{}对应的设备元数据！", iotUuid);
            }
        } catch (Exception e) {
            throw new SendMessageException("设备事件转发异常：{}" + e.getMessage(), e);
        }
    }

    /**
     * 扫描Iot设备清单生成设备元数据
     */
    @Override
    public void scanIoTDeviceToSaveDeviceMeta() throws DeviceQueryException, IOException {

        try {
            int productSize = 1000, productOffset = 0, productLimit = 1000;
            // 当产品列表数据不等于指定limit值时，结束本次循环
            while (productSize == productLimit) {

                // 获取所有的产品信息
                String productTypeResult = queryIoTProductTypeList(String.valueOf(productOffset), String.valueOf(productLimit));
                // 解析产品数据，获取产品信息集合
                JSONObject productTypeJSON = JSONObject.parseObject(productTypeResult);
                JSONArray productTypeArray = productTypeJSON.getJSONArray("list");

                productSize = productTypeArray.size();
                productOffset += productLimit;

                // 循环产品信息集合
                for (int i = 0; i < productTypeArray.size(); i++) {

                    JSONObject productTypeObj = productTypeArray.getJSONObject(i);
                    // 获取产品id product_id
                    String product_id = productTypeObj.getString("product_id");
                    // 根据产品id获取获取关联的设备配置文件，生成元数据实体,存储元数据
                    JSONObject iotDeviceConfigJSON = JSONObject.parseObject(readJsonFile(iotDeviceConfigFilename));
                    // 根据产品id判断配置文件中是否存在该产品下的设备信息
                    JSONObject deviceConfigJSON = iotDeviceConfigJSON.getJSONObject(product_id);
                    if (deviceConfigJSON != null && !deviceConfigJSON.isEmpty()) {

                        int deviceSize = 100, deviceOffset = 0, deviceLimit = 100;
                        // 循环遍历设备数据
                        while (deviceSize == deviceLimit) {

                            // 根据产品id获取设备信息
                            String iotDevices = queryIoTDeviceList(product_id, String.valueOf(deviceOffset), String.valueOf(deviceLimit));
                            JSONObject iotDevicesJSON = JSONObject.parseObject(iotDevices);
                            JSONArray iotDeviceArray = iotDevicesJSON.getJSONArray("list");

                            deviceSize = iotDeviceArray.size();
                            deviceOffset += deviceLimit;

                            // 循环设备列表
//                            for (int j = 0; j < iotDeviceArray.size(); j++) {
                            for (int j = iotDeviceArray.size() - 1; j >= 0; j--) {

                                JSONObject iotDevice = iotDeviceArray.getJSONObject(j);
                                // 根据产品id_设备名称组成iotuuid判断元数据库是否存在
                                String iotUuid = iotDevice.getString("product_id") + "_" + iotDevice.getString("device_name");

                                // 封装DeviceMeta元数据，后期根据根据xml文件获取
                                DeviceMeta deviceMeta = new DeviceMeta();
                                deviceMeta.setName(iotDevice.getString("device_name"));
                                deviceMeta.setIotUuid(iotUuid);
                                // 元数据设备类型赋值
                                deviceMeta.setDeviceType(DeviceTypeEnum.valueOf(deviceConfigJSON.getString("deviceType")));
                                deviceMeta.setListenerClassName("cps.runtime.api.listener.impl.DeviceDefaultEventListener");
                                // 元数据设备属性赋值
                                JSONArray attributeArray = deviceConfigJSON.getJSONArray("attributes");
                                for (int attr = 0; attr < attributeArray.size(); attr++) {
                                    JSONObject attributeJSON = attributeArray.getJSONObject(attr);
                                    deviceMeta.putAttributeMeta(attributeJSON.getString("attributeName"),
                                            new DeviceAttributeMeta(attributeJSON.getString("attributeName")
                                                    , attributeJSON.getString("iotAttributeName")
                                                    , attributeJSON.getString("dataType")
                                                    , attributeJSON.getString("aliasName")));
                                }
                                // 元数据设备事件赋值
                                JSONArray affairArray = deviceConfigJSON.getJSONArray("affairs");
                                for (int aff = 0; aff < affairArray.size(); aff++) {
                                    JSONObject affairJSON = affairArray.getJSONObject(aff);
                                    deviceMeta.putAffairMeta(affairJSON.getString("affairName"),
                                            new DeviceAffairMeta(affairJSON.getString("affairName")
                                                    , affairJSON.getString("iotAffairName")
                                                    , affairJSON.getString("dataType")
                                                    , affairJSON.getString("aliasName")));
                                }
                                // 元数据设备操作赋值
                                JSONArray actionArray = deviceConfigJSON.getJSONArray("actions");
                                for (int act = 0; act < actionArray.size(); act++) {
                                    JSONObject actionJSON = actionArray.getJSONObject(act);
                                    deviceMeta.putActionMeta(actionJSON.getString("actionName")
                                            , new DeviceActionMeta(actionJSON.getString("actionName")
                                                    , actionJSON.getString("iotActionName")
                                                    , actionJSON.getString("dataType")
                                                    , actionJSON.getString("aliasName")));

                                }
                                deviceService.addOrUpdateDeviceMeta(deviceMeta);
                            }
                        }
                    }
                }
            }
        } catch (PlatformQueryException e) {
            throw new DeviceQueryException("平台信息查询异常:" + e.getMessage(), e);
        } catch (DeviceException e) {
            throw new DeviceQueryException("设备元数据操作异常:" + e.getMessage(), e);
        }

    }

    /**
     * 创建Iot设备
     *
     * @param productId  产品id
     * @param deviceName 设备名称
     * @param desc       设备描述
     * @return String
     * @throws DeviceCreateException 设备创建异常
     */
    @Override
    public String createIoTDevice(String productId, String deviceName, String desc) throws DeviceCreateException {
        IotClient client = getIotClient();
        //创建iot设备请求
        CreateDeviceRequest devRequest = new CreateDeviceRequest();
        devRequest.setProductId(productId);
        devRequest.setDeviceName(deviceName);
        devRequest.setDesc(desc);
        //声明设备名称集合用于添加设备至指定项目
        List<String> deviceNameList = new ArrayList<>();
        deviceNameList.add(deviceName);

        //发送请求
        CreateDeviceResponse devResponse = null;
        try {
            //创建设备
            devResponse = client.sendRequest(devRequest);

            //添加设备到项目中
            String addToProMsg = this.addDeviceToProject(client, productId, deviceNameList);
            logger.debug("添加设备到项目结果：{}", addToProMsg);

        } catch (IotClientException | IotServerException | DeviceCreateException e) {
            throw new DeviceCreateException("设备创建异常：" + e.getMessage(), e);
        }

        //释放资源
        try {
            client.close();
        } catch (IOException e) {
            throw new DeviceCreateException("客户端资源释放异常：" + e.getMessage(), e);
        }
        return JSON.toJSONString(devResponse);
    }

    /**
     * @param productId 产品id
     * @param devices   设备集合[{ "name": "no001" , "desc": "iot application" }]
     * @return String
     * @throws DeviceCreateException 设备创建异常
     */
    @Override
    public String batchCreateIoTDevices(String productId, List<ConcurrentHashMap<String, String>> devices) throws
            DeviceCreateException {
        IotClient client = getIotClient();
        //批量创建iot设备请求
        BatchCreateDevicesRequest devRequest = new BatchCreateDevicesRequest();
        //创建iot设备集合
        List<Device> deviceList = new ArrayList<>();
        //声明设备名称集合用于添加设备至指定项目
        List<String> deviceNameList = new ArrayList<>();

        for (ConcurrentHashMap<String, String> device : devices) {
            Device deviceTemp = new Device();
            deviceTemp.setName(device.get("name"));
            deviceTemp.setDesc(device.get("desc"));
            deviceList.add(deviceTemp);
            //存放设备名称
            deviceNameList.add(device.get("name"));
        }
        if (deviceList.size() > 0) {
            //封装请求参数
            devRequest.setProductId(productId);
            devRequest.setDevices(deviceList);
            //发送请求
            BatchCreateDevicesResponse devResponse = null;
            try {
                //批量创建设备
                devResponse = client.sendRequest(devRequest);

                //添加设备到项目中
                String addToProMsg = this.addDeviceToProject(client, productId, deviceNameList);
                logger.debug("添加设备到项目结果：{}", addToProMsg);

            } catch (IotClientException | IotServerException | DeviceCreateException e) {
                throw new DeviceCreateException("设备批量创建异常：" + e.getMessage(), e);
            }

            //释放资源
            try {
                client.close();
            } catch (IOException e) {
                throw new DeviceCreateException("客户端资源释放异常：" + e.getMessage(), e);
            }
            return JSON.toJSONString(devResponse);
        } else {
            throw new DeviceCreateException("创建的设备集合为空！");
        }
    }

    /**
     * 将设备添加至指定项目中
     *
     * @param client    iot客户端
     * @param productId 产品id
     * @param devices   设备名称集合
     * @return String
     * @throws DeviceCreateException 设备创建异常
     */
    private String addDeviceToProject(IotClient client, String productId, List<String> devices) throws
            DeviceCreateException {
        if (client != null && StringUtils.isNotBlank(productId) && devices != null) {
            //创建添加设备到项目的请求
            AddDeviceRequest addDeviceReq = new AddDeviceRequest();
            addDeviceReq.setProjectId(projectId);
            addDeviceReq.setProductId(productId);
            addDeviceReq.setDevices(devices);

            //发送请求
            AddDeviceResponse addDeviceResp = null;
            try {
                addDeviceResp = client.sendRequest(addDeviceReq);
            } catch (IotClientException | IotServerException e) {
                throw new DeviceCreateException("设备添加至项目异常：" + e.getMessage(), e);
            }
            return JSON.toJSONString(addDeviceResp);
        } else {
            throw new DeviceCreateException("添加设备至项目的参数不能为空！");
        }
    }

    /**
     * OneNet的分组创建（未外放，自己内部使用）
     *
     * @param groupName 分组名称
     * @param groupDesc 分组描述
     * @return String
     * @throws DeviceCreateException 设备创建异常
     */
    public String createIoTDeviceGroup(String groupName, String groupDesc) throws DeviceCreateException {
        if (StringUtils.isNotBlank(groupName) && StringUtils.isNotBlank(groupDesc)) {
            IotClient client = getIotClient();
            CreateGroupRequest groupReq = new CreateGroupRequest();
            groupReq.setProjectId(projectId);
            groupReq.setName(groupName);
            groupReq.setDesc(groupDesc);

            //发送请求
            CreateGroupResponse groupResp = null;
            try {
                groupResp = client.sendRequest(groupReq);
            } catch (IotClientException | IotServerException e) {
                throw new DeviceCreateException("项目创建分组异常：" + e.getMessage(), e);
            }
            return JSON.toJSONString(groupResp);
        } else {
            throw new DeviceCreateException("分组创建的参数不能为空！");
        }
    }

    /**
     * OneNet设备添加至分组（内部使用。未外放）
     *
     * @param groupId   分组id
     * @param productId 产品id
     * @param devices   设备名称集合
     * @return String
     * @throws DeviceCreateException 设备创建异常
     */
    public String addGroupDevice(String groupId, String productId, List<String> devices) throws
            DeviceCreateException {
        if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(productId) && devices != null && devices.size() > 0) {
            IotClient client = getIotClient();
            AddGroupDeviceRequest addGroupDevReq = new AddGroupDeviceRequest();
            addGroupDevReq.setProjectId(projectId);
            addGroupDevReq.setProductId(productId);
            addGroupDevReq.setGroupId(groupId);
            addGroupDevReq.setDevices(devices);
            //发送请求
            AddGroupDeviceResponse addGroupDevResp = null;
            try {
                addGroupDevResp = client.sendRequest(addGroupDevReq);
            } catch (IotClientException | IotServerException e) {
                throw new DeviceCreateException("分组添加设备异常：" + e.getMessage(), e);
            }
            return JSON.toJSONString(addGroupDevResp);
        } else {
            throw new DeviceCreateException("分组添加设备的参数不能为空！");
        }
    }

    /**
     * 读取json文件
     *
     * @param fileName 文件路径
     * @return 文件内容
     */
    public String readJsonFile(String fileName) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(IoTOneNetBridge.class.getResourceAsStream(fileName), StandardCharsets.UTF_8);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
