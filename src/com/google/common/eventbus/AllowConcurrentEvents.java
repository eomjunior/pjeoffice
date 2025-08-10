package com.google.common.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ElementTypesAreNonnullByDefault
public @interface AllowConcurrentEvents {}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/eventbus/AllowConcurrentEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */