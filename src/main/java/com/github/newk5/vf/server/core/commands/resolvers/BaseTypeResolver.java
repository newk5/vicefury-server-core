package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public abstract class BaseTypeResolver {

    public Class type;

    public BaseTypeResolver() {
    }

    public Object resolve(String input) throws InvalidParameterTypeException {
        return null;
    }
}
