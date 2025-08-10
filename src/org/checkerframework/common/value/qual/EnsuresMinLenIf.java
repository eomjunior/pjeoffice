package org.checkerframework.common.value.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ConditionalPostconditionAnnotation;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.checkerframework.framework.qual.QualifierArgument;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@ConditionalPostconditionAnnotation(qualifier = MinLen.class)
@InheritedAnnotation
@Repeatable(EnsuresMinLenIf.List.class)
public @interface EnsuresMinLenIf {
  boolean result();
  
  String[] expression();
  
  @QualifierArgument("value")
  int targetValue() default 0;
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
  @ConditionalPostconditionAnnotation(qualifier = MinLen.class)
  @InheritedAnnotation
  public static @interface List {
    EnsuresMinLenIf[] value();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/common/value/qual/EnsuresMinLenIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */