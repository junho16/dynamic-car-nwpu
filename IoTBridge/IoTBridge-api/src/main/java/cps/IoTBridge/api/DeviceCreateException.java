package cps.IoTBridge.api;

/**
 * 设备创建异常
 */
public class DeviceCreateException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public DeviceCreateException() {
        super();
    }

    //用详细信息指定一个异常
    public DeviceCreateException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public DeviceCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public DeviceCreateException(Throwable cause) {
        super(cause);
    }
}
