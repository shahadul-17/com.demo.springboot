package com.demo.springboot.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyTestInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(MyTestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("I AM FROM THE INTERCEPTOR...!!!");
        System.out.println("I AM FROM THE INTERCEPTOR...!!!");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("RESPONSE IS NOT PROCESSED...");
        System.out.println("RESPONSE IS NOT PROCESSED...");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("DONE...");

    }
}
