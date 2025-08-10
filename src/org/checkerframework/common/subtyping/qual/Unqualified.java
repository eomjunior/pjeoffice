package org.checkerframework.common.subtyping.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({})
@InvisibleQualifier
@SubtypeOf({})
public @interface Unqualified {}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/common/subtyping/qual/Unqualified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */