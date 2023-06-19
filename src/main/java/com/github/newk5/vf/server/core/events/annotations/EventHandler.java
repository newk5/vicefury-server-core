package com.github.newk5.vf.server.core.events.annotations;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@IndexAnnotated
public @interface EventHandler {
    
    int position() default 0;
}