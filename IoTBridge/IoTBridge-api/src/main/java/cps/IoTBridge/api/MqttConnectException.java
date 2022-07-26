package cps.IoTBridge.api;

/**
 * IOTMqtt客户端在创建和连接过程中出现的异常
 */
public class MqttConnectException extends Exception {
    private static final long serialVersionUID = 1L;

    //无参构造函数
    public MqttConnectException() {
        super();
    }

    //用详细信息指定一个异常
    public MqttConnectException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public MqttConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public MqttConnectException(Throwable cause) {
        super(cause);
    }
}
