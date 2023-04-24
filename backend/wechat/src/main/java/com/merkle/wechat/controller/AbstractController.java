package com.merkle.wechat.controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.merkle.wechat.aop.Result;
import com.merkle.wechat.common.exception.BaseException;
import com.merkle.wechat.common.exception.ServiceError;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.security.CustomAuthenticationToken;
import com.merkle.wechat.security.Role;
import com.merkle.wechat.security.UserInfo;
import com.merkle.wechat.security.mp.CustomMiniProgramAuthToken;

/**
 * If controller extends this class, it can use the custom exception handler
 *
 * @author tyao
 *
 */
public abstract class AbstractController {
    private @Autowired MessageSource messageSource;
    protected Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

    /**
     * Handle BaseException and BaseException's subclass
     *
     * @param ex
     * @return {@link Result}
     */
    @ExceptionHandler(BaseException.class)
    public @ResponseBody Result handleException(BaseException ex) {
        Optional<String> msgOptional = Optional.ofNullable(getMsg(String.valueOf(ex.getCode()), ex));
        String msg = msgOptional.orElse(getMsg(String.valueOf(ExceptionConstants.UN_KNOWN_ERROR)));

        if (ServiceError.class.isAssignableFrom(ex.getClass())) {
            logger.error(msg, ex);
        } else {
            logger.warn(msg, ex);
        }

        return new Result(ex.getCode(), msg);
    }

    /**
     * Handle All MethodArgumentNotValidException
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Result handle(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<ObjectError> allErrors = result.getAllErrors();
        Optional<ObjectError> first = allErrors.stream().findFirst();
        if (first.isPresent()) {
            ObjectError oe = first.get();
            String defaultMessage = oe.getDefaultMessage();
            try {
                Integer code = Integer.valueOf(defaultMessage);
                Optional<String> msgOptional = Optional.ofNullable(getMsg(String.valueOf(code)));
                String msg = msgOptional.orElse(getMsg(String.valueOf(ExceptionConstants.UN_KNOWN_ERROR)));
                if (oe instanceof FieldError) {
                    msg = ((FieldError) oe).getField() + " " + msg;
                }
                return new Result(code, msg);
            } catch (NumberFormatException e) {
                if (oe instanceof FieldError) {
                    defaultMessage = ((FieldError) oe).getField() + " " + defaultMessage;
                }
                return new Result(10000, defaultMessage);
            }

        }
        return new Result(-1, getMsg(String.valueOf(ExceptionConstants.UN_KNOWN_ERROR)));
    }

    /**
     * Handle All ValidationException for entity
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Result handle(ValidationException ex) {
        return new Result(ExceptionConstants.UN_KNOWN_ERROR, getMsg(String.valueOf(ExceptionConstants.UN_KNOWN_ERROR)));
    }

    /**
     * Handle All MissingServletRequestParameterException for entity
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Result handle(MissingServletRequestParameterException ex) {
    	logger.error(ex.getMessage(), ex);
        return new Result(ExceptionConstants.MISSING_PARAM_ERROR, ex.getMessage());
    }

    /**
     * Handle ALL Exception except the BaseException and BaseException's
     * subclass
     *
     * @param ex
     * @return {@link Result}
     */
    @ExceptionHandler(Exception.class)
    public @ResponseBody Result handleAllException(Exception ex) {

        logger.error(ex.getMessage(), ex);

        return new Result(ExceptionConstants.UN_KNOWN_ERROR, getMsg(String.valueOf(ExceptionConstants.UN_KNOWN_ERROR)));
    }

    public UserInfo retrieveTokenUserInfo() {
        return ((CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserInfo();
    }

    public boolean isSystemOperator() {
        return retrieveTokenUserInfo().getRole().equals(Role.OP.toString());
    }

    public String retrieveUserPbNoIds() {
        return retrieveTokenUserInfo().getPbNoIds();
    }

    public String retrieveUserOpenid() {
        CustomMiniProgramAuthToken token = (CustomMiniProgramAuthToken) SecurityContextHolder.getContext()
                .getAuthentication();
        return token.getUserInfo();
    }

    public String getMsg(String code, BaseException ex) {
        try {
            return messageSource.getMessage(code, ex.getParams(), Locale.ENGLISH);
        } catch (Exception e) {
            return null;
        }
    }

    public String getMsg(String code) {
        try {
            return messageSource.getMessage(code, null, Locale.ENGLISH);
        } catch (Exception e) {
            return null;
        }
    }

}
