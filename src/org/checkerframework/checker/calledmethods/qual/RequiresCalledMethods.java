package org.checkerframework.checker.calledmethods.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.PreconditionAnnotation;
import org.checkerframework.framework.qual.QualifierArgument;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@PreconditionAnnotation(qualifier = CalledMethods.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Repeatable(RequiresCalledMethods.List.class)
public @interface RequiresCalledMethods {
  String[] value();
  
  @QualifierArgument("value")
  String[] methods();
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @PreconditionAnnotation(qualifier = CalledMethods.class)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
  public static @interface List {
    RequiresCalledMethods[] value();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/checker/calledmethods/qual/RequiresCalledMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */