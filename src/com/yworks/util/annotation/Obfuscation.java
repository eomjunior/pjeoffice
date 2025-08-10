package com.yworks.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Obfuscation {
  boolean applyToMembers() default true;
  
  boolean exclude() default true;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/annotation/Obfuscation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */