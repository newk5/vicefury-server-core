package com.github.newk5.vf.server.core.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Command {

    String name() default "";

    String syntax() default "";

    String[] alias() default "";
    
    boolean lastParamIsOptional() default false;
    
    boolean allParamsAreOptional() default false;

    boolean requiresAuthentication() default false;
}