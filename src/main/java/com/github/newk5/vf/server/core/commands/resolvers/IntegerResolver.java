package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public class IntegerResolver extends BaseTypeResolver {

    public IntegerResolver() {
        super();
        type = Integer.class;
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve Integer input '" + input + "' to Integer");
        }
        try {
            return Integer.valueOf(input);
        } catch (Exception e) {
            throw new InvalidParameterTypeException("Failed to resolve Integer input '" + input + "' to Integer");
        }
    }
}