package cps.api.entity;

/**
 * 事件名称不支持异常
 */
public class UnsupportedAffairNameException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public UnsupportedAffairNameException() {
        super();
    }

    //用详细信息指定一个异常
    public UnsupportedAffairNameException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public UnsupportedAffairNameException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public UnsupportedAffairNameException(Throwable cause) {
        super(cause);
    }
}
