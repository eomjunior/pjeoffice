package org.checkerframework.dataflow.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AssertMethod {
  Class<?> value() default AssertionError.class;
  
  int parameter() default 1;
  
  boolean isAssertFalse() default false;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/dataflow/qual/AssertMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */