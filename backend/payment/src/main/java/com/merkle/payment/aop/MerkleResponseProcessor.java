package com.merkle.payment.aop;

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

import com.merkle.payment.annotation.MerkelResponseBody;

public class MerkleResponseProcessor extends RequestResponseBodyMethodProcessor {
    private final int SUCCESS_ERROR_CODE = 0;
    private final String SUCCESS_ERROR_MESSAGE = "ok";
    private final String ERROR_MESSAGE = "ERROR!";

    public MerkleResponseProcessor(final List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    public MerkleResponseProcessor(final List<HttpMessageConverter<?>> messageConverters,
            final ContentNegotiationManager contentNegotiationManager) {
        super(messageConverters, contentNegotiationManager);
    }

    @Override
    public boolean supportsReturnType(final MethodParameter returnType) {
        return returnType.hasMethodAnnotation(MerkelResponseBody.class);
    }

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
        return new MerkleResponse(errorCode, errorMessage, response);
    }
}
