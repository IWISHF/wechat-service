package com.merkle.wechat.common.exception;

import java.util.Collection;

/**
 * 
 * @author tyao
 *
 */
public class ServiceWarn extends BaseException {
    private static final long serialVersionUID = 1L;

    public ServiceWarn(String message, int code, Object[] params) {
        super(message, code, params);
    }

    public ServiceWarn(String message, int code) {
        super(message, code);
    }

    public ServiceWarn(String message, Throwable cause, int code, Object[] params) {
        super(message, cause, code, params);
    }

    public ServiceWarn(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public ServiceWarn(String message) {
        super(message);
    }

    public ServiceWarn(String message, int code, Collection<?> details) {
        super(message, code, details);
    }

    public ServiceWarn(String message, int code, Object[] params, Collection<?> details) {
        super(message, code, params, details);
    }

    public ServiceWarn(String message, Throwable cause, int code, Collection<?> details) {
        super(message, cause, code, details);
    }

    public ServiceWarn(String message, Throwable cause, int code, Object[] params, Collection<?> details) {
        super(message, cause, code, params, details);
    }

    public ServiceWarn(String message, Throwable cause, Collection<?> details) {
        super(message, cause);
        setDetails(details);
    }

    public ServiceWarn(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceWarn(Throwable cause) {
        super(cause);
    }

    public ServiceWarn(int code) {
        super(code);
    }

    public ServiceWarn(String message, String code) {
        super(message, Integer.valueOf(code));
    }

}
