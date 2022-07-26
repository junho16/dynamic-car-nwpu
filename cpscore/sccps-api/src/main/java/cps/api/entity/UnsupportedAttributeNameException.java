package cps.api.entity;

/**
 * 属性名称不支持异常
 */
public class UnsupportedAttributeNameException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public UnsupportedAttributeNameException() {
        super();
    }

    //用详细信息指定一个异常
    public UnsupportedAttributeNameException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public UnsupportedAttributeNameException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public UnsupportedAttributeNameException(Throwable cause) {
        super(cause);
    }
}
