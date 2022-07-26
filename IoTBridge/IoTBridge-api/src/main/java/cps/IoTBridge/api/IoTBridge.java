package cps.IoTBridge.api;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于定义平台连接Iot平台的桥接接口，该接口重点用于获取平台的基础信息，设备清单信息，具体的设备操作接口先不放这里
 *
 * @author chenke
 */
public interface IoTBridge {

    /**
     * 获取Iot平台信息
     *
     * @return IoTDeviceMeta
     * @throws PlatformQueryException 平台信息查询异常
     */
    String queryIoTMeta() throws PlatformQueryException;

    /**
     * 获取指定设备的iot元数据
     *
     * @param iotDevId 物联网设备的唯一id号
     * @return IoTDeviceMeta
     * @throws DeviceQueryException 设备信息查询异常
     */
    IoTDeviceMeta queryIoTDeviceMeta(String iotDevId) throws DeviceQueryException;

    /**
     * 获取平台产品类型清单
     *
     * @param offset 请求起始位置
     * @param limit  每次请求记录数
     * @return String
     * @throws PlatformQueryException 平台信息查询异常
     */
    String queryIoTProductTypeList(String offset, String limit) throws PlatformQueryException;

    /**
     * 获取平台设备列表
     *
     * @param offset 请求起始位置
     * @param limit  每次请求记录数
     * @return String
     * @throws DeviceQueryException 设备信息查询异常
     */
    String queryIoTDeviceList(String productId, String offset, String limit) throws DeviceQueryException;

    /**
     * 获取平台设备详情信息
     *
     * @param iotUuid 物联网设备的唯一id号
     * @return String
     * @throws DeviceQueryException 设备信息查询异常
     */
    String queryIoTDeviceDetail(String iotUuid) throws DeviceQueryException;

    /**
     * 向设备推送设备属性设置操作报文
     *
     * @param iotUuid 物联网设备的唯一id号
     * @param actionName 操作标识
     * @param params  设备操作报文
     * @return String
     * @throws DeviceActionException 设备动作异常
     */
    String sendActionToIoT(String iotUuid, String actionName, ConcurrentHashMap<String, Object> params) throws DeviceActionException;

    /**
     * 将OneNet设备属性上报消息转发至CPS
     *
     * @param iotMessage 消息内容
     * @throws SendMessageException 消息发送异常
     */
    void sendDevicePropertyToCPS(String iotMessage) throws SendMessageException;

    /**
     * 将OneNet设备事件上报消息转发至CPS
     *
     * @param iotMessage 消息内容
     * @throws SendMessageException 消息发送异常
     */
    void sendDeviceEventToCPS(String iotMessage) throws SendMessageException;

    /**
     * 扫描Iot设备清单生成设备元数据
     */
    void scanIoTDeviceToSaveDeviceMeta() throws DeviceQueryException, IOException;

    /**
     * 创建iot设备
     *
     * @param productId  产品id
     * @param deviceName 设备名称
     * @param desc       设备描述
     * @return String
     * @throws DeviceCreateException 设备创建异常
     */
    String createIoTDevice(String productId, String deviceName, String desc) throws DeviceCreateException;

    /**
     * 批量创建iot设备
     *
     * @param productId 产品id
     * @param devices   设备集合
     * @return String
     * @throws DeviceCreateException 设备创建异常
     */
    String batchCreateIoTDevices(String productId, List<ConcurrentHashMap<String, String>> devices) throws DeviceCreateException;

//    /**
//     * 获取设备属性报文 //TODO API异常，后期处理
//     *
//     * @param productId  产品类型ID
//     * @param deviceName 设备名称
//     * @param params     设备属性报文
//     * @return String
//     * @throws Exception 异常
//     */
//    String queryIoTDevicePropertyDetail(String productId, String deviceName, String params);
}
