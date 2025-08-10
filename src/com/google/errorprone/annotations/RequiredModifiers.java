package com.google.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.lang.model.element.Modifier;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE})
public @interface RequiredModifiers {
  @Deprecated
  Modifier[] value() default {};
  
  Modifier[] modifier() default {};
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/errorprone/annotations/RequiredModifiers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */