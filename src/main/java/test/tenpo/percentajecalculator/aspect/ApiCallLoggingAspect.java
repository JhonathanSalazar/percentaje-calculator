package test.tenpo.percentajecalculator.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import test.tenpo.percentajecalculator.service.CallHistoryService;

import java.util.Map;

@Aspect
@Component
public class ApiCallLoggingAspect {

    private final CallHistoryService callHistoryService;
    private final ObjectMapper objectMapper;

    public ApiCallLoggingAspect(CallHistoryService callHistoryService, ObjectMapper objectMapper) {
        this.callHistoryService = callHistoryService;
        this.objectMapper = objectMapper;
    }

    @Around("execution(* test.tenpo.percentajecalculator.controller.PercentageController.*(..))")
    public Object logPercentageControllerCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        return logApiCall(joinPoint);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public Object logExceptionHandlerCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        return logApiCall(joinPoint);
    }

    private Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String endpoint = request.getRequestURI();
        
        if (endpoint.contains("/api/v1/call-history")) {
            return joinPoint.proceed();
        }
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        String parameters = objectMapper.writeValueAsString(parameterMap);
        
        Object result = joinPoint.proceed();
        String response = objectMapper.writeValueAsString(result);
        
        callHistoryService.logApiCall(endpoint, parameters, response);

        return result;
    }
}