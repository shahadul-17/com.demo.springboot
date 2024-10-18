package com.demo.springboot.configurations;

import com.demo.springboot.interceptors.MyTestInterceptor;
import com.demo.springboot.ping.PingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private MyTestInterceptor interceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final var registration = registry.addInterceptor(interceptor);
        /*RequestMapping annotation = PingController.class.getAnnotation(RequestMapping.class);

        for (final var rootPath : annotation.value()) {
            registration.addPathPatterns(rootPath, rootPath + "/**");
        }*/
    }

}
