package com.microsoft.azure.msalwebsample.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // can use in method only.
public @interface Authorized {

  public boolean enabled() default true;
  public String[] hasRole() default "";

}