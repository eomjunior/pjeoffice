package io.reactivex.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
public @interface SchedulerSupport {
  public static final String NONE = "none";
  
  public static final String CUSTOM = "custom";
  
  public static final String COMPUTATION = "io.reactivex:computation";
  
  public static final String IO = "io.reactivex:io";
  
  public static final String NEW_THREAD = "io.reactivex:new-thread";
  
  public static final String TRAMPOLINE = "io.reactivex:trampoline";
  
  public static final String SINGLE = "io.reactivex:single";
  
  String value();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/annotations/SchedulerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */