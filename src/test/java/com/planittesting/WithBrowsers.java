package com.planittesting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface WithBrowsers {
    WithBrowsers.Browser[] value();

    public static enum Browser{
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI,
        ALL
    }
}
