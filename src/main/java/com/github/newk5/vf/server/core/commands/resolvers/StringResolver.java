package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public class StringResolver extends BaseTypeResolver {

    public StringResolver() {
        super();
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null || input.equals("")) {
            throw new InvalidParameterTypeException("Failed to resolve String input '" + input + "' to String");
        }
        return input + "";
    }
}