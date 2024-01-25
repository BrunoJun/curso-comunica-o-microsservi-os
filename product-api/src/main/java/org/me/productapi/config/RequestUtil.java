package org.me.productapi.config;

import jakarta.servlet.http.HttpServletRequest;
import org.me.productapi.config.exception.ValidationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtil {

    public static HttpServletRequest getCurretRequest(){

        try {

            return ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();
        } catch (Exception e){

            e.printStackTrace();
            throw new ValidationException("The current request could not be processed");
        }
    }
}
