package org.me.productapi.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.me.productapi.config.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.me.productapi.config.RequestUtil.getCurretRequest;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        var currentRequest = getCurretRequest();

        requestTemplate
                .header("Authorization", currentRequest.getHeader("Authorization"))
                .header("transactionid", currentRequest.getHeader("transactionid"));
    }
}
