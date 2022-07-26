package cps.data.api.service;


import com.alibaba.fastjson.JSONObject;

/**
 * 消息日志服务
 */
public interface MessageLogService {

    /**
     * 保存设备消息日志
     *
     * @param deviceMsgLog
     */
    public void saveDeviceMsgLog(String deviceMsgLog);

    /**
     * 保存cp消息日志
     *
     * @param cpMsgLog
     */
    public void saveCPMsgLog(String cpMsgLog);

    /**
     * 保存cps消息日志
     *
     * @param cpsMsgLog
     */
    public void saveCPSMsgLog(String cpsMsgLog);

}
