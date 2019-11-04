package com.yt.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.PARAMETER})
public @interface MyRequestParam {
    String value() default "";
}
