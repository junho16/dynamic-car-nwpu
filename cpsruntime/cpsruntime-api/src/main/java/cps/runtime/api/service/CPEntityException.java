package cps.runtime.api.service;

/**
 * CP信息异常
 */
public class CPEntityException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public CPEntityException() {
        super();
    }

    //用详细信息指定一个异常
    public CPEntityException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public CPEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public CPEntityException(Throwable cause) {
        super(cause);
    }
}
