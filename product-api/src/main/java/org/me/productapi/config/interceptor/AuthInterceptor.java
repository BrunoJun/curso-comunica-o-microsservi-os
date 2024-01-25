package org.me.productapi.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.me.productapi.config.exception.ValidationException;
import org.me.productapi.module.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;


public class AuthInterceptor implements HandlerInterceptor {

    private static final String TRANSACTION_ID = "transactionid";

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (isOption(request)){

            return true;
        }

        if (isEmpty(request.getHeader(TRANSACTION_ID))){

            throw new ValidationException("The transactionId is required");
        }

        var authorization = request.getHeader("Authorization");
        jwtService.isAuthorized(authorization);

        request.setAttribute("serviceid", UUID.randomUUID().toString());

        return true;
    }

    private boolean isOption(HttpServletRequest request){

        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
