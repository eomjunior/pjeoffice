package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface RestrictedApi {
  String explanation();
  
  String link() default "";
  
  String allowedOnPath() default "";
  
  Class<? extends Annotation>[] allowlistAnnotations() default {};
  
  Class<? extends Annotation>[] allowlistWithWarningAnnotations() default {};
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/errorprone/annotations/RestrictedApi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */