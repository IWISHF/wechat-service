package com.merkle.wechat.aop;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.merkle.wechat.annotation.NeedWrap;

/**
 * Use to wrap the result to {"errorCode":xx, "errMessage":xx, "response":xx}
 * default trigger wrap operation. use @NotWrap annotation will not trigger wrap
 * operation
 * 
 * @author tyao
 *
 */
public class ResultWrapperHandlerMethodReturnValueHandler extends RequestResponseBodyMethodProcessor {
    private final int SUCCESS_ERROR_CODE = 0;
    private final String SUCCESS_ERROR_MESSAGE = "ok";
    private final String ERROR_MESSAGE = "ERROR!";

    public ResultWrapperHandlerMethodReturnValueHandler(final List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    public ResultWrapperHandlerMethodReturnValueHandler(final List<HttpMessageConverter<?>> messageConverters,
            final ContentNegotiationManager contentNegotiationManager) {
        super(messageConverters, contentNegotiationManager);
    }

    @Override
    public boolean supportsReturnType(final MethodParameter returnType) {
        return returnType.hasMethodAnnotation(NeedWrap.class);
    }

    /*
     * If user has permission, will return:
     * 
     * { "errorMessage": "ok", "errorCode": 0, "data": "Hello Home" }
     * 
     * If user has no permission and x-auth-token is wrong, will return:
     * 
     * {"errorMessage": "Access is denied", "errorCode": -1, "data": null }
     * 
     * If user send invalid token, will return like this:
     * 
     * { "errorMessage": "ERROR!", "errorCode": 401, "data": { "headers":{},
     * "body": { "timestamp": "2017-07-12 10:12:57", "status": 401, "error":
     * "Unauthorized",
     * "message":"JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted."
     * , "path": "/weconnect/channels" }, "statusCode": "UNAUTHORIZED",
     * "statusCodeValue": 401 } }
     * 
     * 
     */
    @Override
    public void handleReturnValue(final Object returnValue, final MethodParameter returnType,
            final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException {
        Object response = null;

        if (returnValue != null && (ResponseEntity.class).isAssignableFrom(returnValue.getClass())) {
            @SuppressWarnings("unchecked")
            ResponseEntity<HttpServletResponse> resp = (ResponseEntity<HttpServletResponse>) returnValue;
            if (!resp.getStatusCode().is2xxSuccessful()) {
                response = wrapper(returnValue, resp.getStatusCodeValue(), ERROR_MESSAGE);
            }
        } else {
            response = wrapper(returnValue, SUCCESS_ERROR_CODE, SUCCESS_ERROR_MESSAGE);
        }
        super.handleReturnValue(response, returnType, mavContainer, webRequest);

    }

    private Object wrapper(Object response, int errorCode, String errorMessage) {
        return new Result(errorCode, errorMessage, response);
    }
}
