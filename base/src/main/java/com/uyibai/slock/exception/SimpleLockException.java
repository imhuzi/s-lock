package com.uyibai.slock.exception;

/**
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date : 2022/1/11
 */
public class SimpleLockException extends Exception {

    private static final long serialVersionUID = 1L;

    final Integer code;

    final Object result;


    public SimpleLockException(String message, Throwable cause) {
        super(message, cause);
        this.code = null;
        this.result = null;
    }

    public SimpleLockException(String message) {
        super(message);
        this.code = null;
        this.result = null;
    }

    public SimpleLockException(String message, int httpCode) {
        super(message + "(" + httpCode + ")");
        this.code = httpCode;
        this.result = null;
    }

    public int getCode() {
        return code;
    }

    public boolean isHttpError(int httpCode) {
        return (this.code != null && httpCode == this.code);
    }

    public boolean isNetError() {
        return result == null;
    }
}
