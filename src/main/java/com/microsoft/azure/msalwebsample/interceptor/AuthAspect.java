package com.microsoft.azure.msalwebsample.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AuthAspect {

	@Autowired
   AuthorizationImpl authBean;
   
   @Before("@annotation(com.microsoft.azure.msalwebsample.interceptor.Authorized) && args(request,response,..)")
   public void before(JoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response){
	    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	    Method method = signature.getMethod();
	    Authorized authorized = method.getAnnotation(Authorized.class);
	    
       if (!(request instanceof HttpServletRequest)) {
           throw 
           new RuntimeException("request should be HttpServletRequesttype");
       }
       
       if(!authBean.authorize(request,response, authorized.hasRole())){
    	  throw new RuntimeException("Forbidone !!!");
       }
      
   } 
   
}