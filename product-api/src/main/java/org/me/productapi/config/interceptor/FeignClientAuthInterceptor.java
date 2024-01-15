package org.me.productapi.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.me.productapi.config.exception.ValidationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignClientAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        var currentRequest = getCurretRequest();

        requestTemplate
                .header("Authorization", currentRequest.getHeader("Authorization"));
    }

    private HttpServletRequest getCurretRequest(){

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
