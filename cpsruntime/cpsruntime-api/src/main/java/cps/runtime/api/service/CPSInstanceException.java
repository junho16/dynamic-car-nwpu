package cps.runtime.api.service;

/**
 * CPS信息异常
 */
public class CPSInstanceException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public CPSInstanceException() {
        super();
    }

    //用详细信息指定一个异常
    public CPSInstanceException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public CPSInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public CPSInstanceException(Throwable cause) {
        super(cause);
    }
}
