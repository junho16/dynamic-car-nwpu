package cps.api.entity;

/**
 * 操作名称不支持异常
 */
public class UnsupportedActionNameException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public UnsupportedActionNameException() {
        super();
    }

    //用详细信息指定一个异常
    public UnsupportedActionNameException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public UnsupportedActionNameException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public UnsupportedActionNameException(Throwable cause) {
        super(cause);
    }
}
