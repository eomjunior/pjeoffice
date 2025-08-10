package org.checkerframework.checker.units.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface UnitsRelations {
  Class<?> value();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/checker/units/qual/UnitsRelations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */