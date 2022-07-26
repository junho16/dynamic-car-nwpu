package cps.IoTBridge.api;

/**
 * 设备信息查询异常
 */
public class DeviceQueryException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public DeviceQueryException() {
        super();
    }

    //用详细信息指定一个异常
    public DeviceQueryException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public DeviceQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public DeviceQueryException(Throwable cause) {
        super(cause);
    }
}
