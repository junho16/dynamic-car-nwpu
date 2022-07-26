package cps.IoTBridge.OneNet;

import cps.IoTBridge.api.DeviceActionException;
import cps.IoTBridge.OneNet.IoTOneNetBridge;

/**
 * 数据从北向南的接口
 * 监听控制指令的mq队列，将指令信息转发给iot
 * 在onenet中通过监听设备操作kafka，然后解码调用IoTOneNetBridge的方法转发给iot平台的mqtt
 * 该类的主要职责是将北向报文解析出该南向甩给哪个设备
 * @author chenke
 *
 */
public class ActionMQListener {
	private IoTOneNetBridge ioTOneNetBridge;
	/**
	 * 该方法用于监听bridge自有的操作消息kafka队列。
	 * 监听到报文完成后调用pushActionToDevice方法将监听信息转发到南向onenet平台
	 */
	public void MQListener() {
		// TODO 该方法需被实现
	}
	/**
	 * 调用IoTOneNetBridge中的动作方法，给设备发指令
	 * @return
	 * @throws DeviceActionException
	 */
	public String sendActionToIoT(String cpsMessage) throws DeviceActionException{
		
		return null;
	}

}
