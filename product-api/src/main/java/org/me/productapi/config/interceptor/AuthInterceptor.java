package org.me.productapi.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.me.productapi.module.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;


public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (isOption(request)){

            return true;
        }

        var authorization = request.getHeader("Authorization");
        jwtService.isAuthorized(authorization);

        return true;
    }

    private boolean isOption(HttpServletRequest request){

        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
