package cps.IoTBridge.OneNet.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * mqtt的配置类
 */
@Component
public class MqttProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    //用户自定义合法的UTF-8字符串，可为空
    @Value("${oneNet.mqttConfig.client_id}")
    private String clientID;

    //服务地址
    @Value("${oneNet.mqttConfig.server_uri}")
    private String serverURI;

    //消息队列mq名称
    @Value("${oneNet.mqttConfig.mqtt_user_name}")
    private String userName;

    //消息队列mq access_key
    @Value("${oneNet.mqttConfig.access_key}")
    private String accessKey;

    //版本号，无需修改
    @Value("${oneNet.mqttConfig.version}")
    private String version;

    //通过消息队列mq实例名称访问mq
    @Value("${oneNet.mqttConfig.resource_name}")
    private String resourceName;

    //签名方法，支持md5、sha1、sha256
    @Value("${oneNet.mqttConfig.signature_method}")
    private String signatureMethod;

    //密码过期时间(目前为100天)
    @Value("${oneNet.mqttConfig.expiration_time}")
    private Integer expirationTime;

    //消息队列mq topic队列名
    @Value("${oneNet.mqttConfig.mqtt_topic}")
    private String mqttTopic;

    //消息队列mq sub订阅名
    @Value("${oneNet.mqttConfig.mqtt_sub}")
    private String mqttSub;

    //消息是否需要手动回复ack
    @Value("${oneNet.mqttConfig.manual_acks}")
    private Boolean manualAcks;

    //clean session 必须设置 true
    @Value("${oneNet.mqttConfig.clean_session}")
    private Boolean cleanSession;

    //自动重连
    @Value("${oneNet.mqttConfig.auto_reconnect}")
    private Boolean autoReconnect;

    //证数路径
    @Value("${oneNet.mqttConfig.ca_crt_filepath}")
    private String caCrtFilePath;

    //超时时间
    @Value("${oneNet.mqttConfig.timeout}")
    private Integer timeout;

    //心跳
    @Value("${oneNet.mqttConfig.keepalive}")
    private Integer keepalive;

    public String getClientID() {
        return clientID;
    }

    public String getServerURI() {
        return serverURI;
    }

    public String getUserName() {
        return userName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getVersion() {
        return version;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public Integer getExpirationTime() {
        return expirationTime;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public String getMqttSub() {
        return mqttSub;
    }

    public Boolean getManualAcks() {
        return manualAcks;
    }

    public Boolean getCleanSession() {
        return cleanSession;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public Integer getKeepalive() {
        return keepalive;
    }

    public String getCaCrtFilePath() {
        return caCrtFilePath;
    }

    public Boolean getAutoReconnect() {
        return autoReconnect;
    }

    @Override
    public String toString() {
        return "MqttProperties{" +
                "clientID='" + clientID + '\'' +
                ", serverURI='" + serverURI + '\'' +
                ", userName='" + userName + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", version='" + version + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", signatureMethod='" + signatureMethod + '\'' +
                ", expirationTime=" + expirationTime +
                ", mqttTopic='" + mqttTopic + '\'' +
                ", mqttSub='" + mqttSub + '\'' +
                ", manualAcks=" + manualAcks +
                ", cleanSession=" + cleanSession +
                ", caCrtFilePath=" + caCrtFilePath +
                ", timeout=" + timeout +
                ", keepalive=" + keepalive +
                '}';
    }
}
