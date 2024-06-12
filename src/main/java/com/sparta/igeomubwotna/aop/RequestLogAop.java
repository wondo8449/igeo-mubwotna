package com.sparta.igeomubwotna.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Slf4j
@Component
public class RequestLogAop {

    @Pointcut("execution(* com.sparta.igeomubwotna..*Controller.*(..))")
    public void controller() {
    }

    @Around("controller()")
    public Object loggingBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, Object> params = new HashMap<>();

        try {
            String decodedURI = URLDecoder.decode(request.getRequestURI(), "UTF-8");

            params.put("request_uri", decodedURI);
            params.put("http_method", request.getMethod());
        } catch (Exception e) {
            log.error("LoggerAspect error", e);
        }

        log.info("Request URL : {}", params.get("request_uri"));
        log.info("HTTP Method : {}", params.get("http_method"));

        Object result = joinPoint.proceed();

        return result;
    }

}
