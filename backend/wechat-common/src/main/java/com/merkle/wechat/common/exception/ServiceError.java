package com.merkle.wechat.common.exception;

import java.util.Collection;

/**
 * 
 * @author tyao
 *
 */
public class ServiceError extends BaseException {

    private static final long serialVersionUID = -3802017920462752017L;

    public ServiceError(String message, int code, Object[] params) {
        super(message, code, params);
    }

    public ServiceError(String message, int code) {
        super(message, code);
    }

    public ServiceError(String message, Throwable cause, int code, Object[] params) {
        super(message, cause, code, params);
    }

    public ServiceError(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public ServiceError(String message) {
        super(message);
    }

    public ServiceError(String message, int code, Collection<?> details) {
        super(message, code, details);
    }

    public ServiceError(String message, int code, Object[] params, Collection<?> details) {
        super(message, code, params, details);
    }

    public ServiceError(String message, Throwable cause, int code, Collection<?> details) {
        super(message, cause, code, details);
    }

    public ServiceError(String message, Throwable cause, int code, Object[] params, Collection<?> details) {
        super(message, cause, code, params, details);
    }

    public ServiceError(String message, Throwable cause, Collection<?> details) {
        super(message, cause);
        setDetails(details);
    }

    public ServiceError(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceError(Throwable cause) {
        super(cause);
    }
}