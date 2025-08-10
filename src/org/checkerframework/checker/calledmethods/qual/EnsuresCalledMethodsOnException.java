package org.checkerframework.checker.calledmethods.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InheritedAnnotation;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Repeatable(EnsuresCalledMethodsOnException.List.class)
@Retention(RetentionPolicy.RUNTIME)
@InheritedAnnotation
public @interface EnsuresCalledMethodsOnException {
  String[] value();
  
  String[] methods();
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
  @InheritedAnnotation
  public static @interface List {
    EnsuresCalledMethodsOnException[] value();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/checker/calledmethods/qual/EnsuresCalledMethodsOnException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */