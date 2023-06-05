package com.github.newk5.vf.server.core.commands;

import com.github.newk5.vf.server.core.commands.annotations.Resolve;
import com.github.newk5.vf.server.core.commands.resolvers.BaseTypeResolver;
import com.github.newk5.vf.server.core.exceptions.CommandTypeResolverNotFound;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;
import java.lang.reflect.Parameter;

public class CommandParam {

    private ServerCommand command;
    private CommandRegistry registry;
    private boolean optional;
    private boolean isLastParam;
    private Class type;
    private Parameter param;

    public CommandParam(ServerCommand command, CommandRegistry registry, boolean optional, Class type, boolean isLastParam) {
        this.command = command;
        this.registry = registry;
        this.optional = optional;
        this.type = type;
    }

    public Object resolveType(String input) throws CommandTypeResolverNotFound, InvalidParameterTypeException {
        BaseTypeResolver r = null;
        if (param.isAnnotationPresent(Resolve.class)) {
            Resolve rr = param.getAnnotation(Resolve.class);
            r = registry.getNamedResolver(rr.value());
            if (r == null) {
                throw new CommandTypeResolverNotFound("Failed to find Named Resolver '"+rr.value()+"' for datatype: " + type.getName() + ", if you want to be able to resolve this datatype, register a new namedResolver using the commandRegistry#registerNamedResolver method");

            }
        } else {
            r = registry.getTypeResolver(type);
        }
        if (r == null) {
            throw new CommandTypeResolverNotFound("Failed to find TypeResolver for datatype: " + type.getName() + ", if you want to be able to resolve this datatype, register a new type resolver for " + type.getName() + " using the commandRegistry#registerTypeResolver method");
        }
        return r.resolve(input);
    }
    
    public boolean hasNamedResolver(){
        return (param.isAnnotationPresent(Resolve.class));
    }

    public Parameter getParam() {
        return param;
    }

    public void setParam(Parameter param) {
        this.param = param;
    }

    public boolean isIsLastParam() {
        return isLastParam;
    }

    public void setIsLastParam(boolean isLastParam) {
        this.isLastParam = isLastParam;
    }

    public boolean matches(String m) {
        return true;
    }

    public ServerCommand getCommand() {
        return command;
    }

    public void setCommand(ServerCommand command) {
        this.command = command;
    }

    public CommandRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(CommandRegistry registry) {
        this.registry = registry;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

}
