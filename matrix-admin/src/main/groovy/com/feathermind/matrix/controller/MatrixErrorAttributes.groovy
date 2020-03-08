package com.feathermind.matrix.controller

import com.feathermind.matrix.util.MatrixException
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest

/**
 * ErrorAttributes方式比ResponseEntityExceptionHandler信息更全面
 *
 * @see org.springframework.boot.web.servlet.error.ErrorAttributes<br>
 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
 */
@Component
class MatrixErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

        Throwable throwable = getError(webRequest);
        Throwable cause = throwable.getCause();
        if (cause != null) {
            Map causeErrorAttributes = new HashMap<>();
            causeErrorAttributes.put("exception", cause.getClass().getName());
            causeErrorAttributes.put("message", cause.getMessage());
            errorAttributes.put("cause", causeErrorAttributes);
        }
        if (throwable instanceof MatrixException) {
            MatrixException me = (MatrixException) throwable
            errorAttributes.put('errorCode', me.errorCode)
        }
        return errorAttributes;
    }
}
