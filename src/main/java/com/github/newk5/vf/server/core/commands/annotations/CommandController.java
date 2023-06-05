package com.github.newk5.vf.server.core.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atteo.classindex.IndexAnnotated;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@IndexAnnotated
public @interface CommandController {

    public String name() default "";
    public boolean requiresAuthentication() default false;
}
