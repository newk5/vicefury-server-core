package com.github.newk5.vf.server.core.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atteo.classindex.IndexAnnotated;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@IndexAnnotated
public @interface ChannelController {

    public String value() default "";
}
