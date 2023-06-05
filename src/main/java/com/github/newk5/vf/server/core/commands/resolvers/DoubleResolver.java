package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public class DoubleResolver extends BaseTypeResolver {

    public DoubleResolver() {
        super();
        type = Double.class;
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve Double input '" + input + "' to Double");
        }
        try {
            return Double.valueOf(input);
        } catch (Exception e) {
            throw new InvalidParameterTypeException("Failed to resolve Double input '" + input + "' to Double");
        }
    }
}