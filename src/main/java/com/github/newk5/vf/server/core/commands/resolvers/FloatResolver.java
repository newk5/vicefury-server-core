package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public class FloatResolver extends BaseTypeResolver {

    public FloatResolver() {
        super();
        type = Float.class;
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve Float input '" + input + "' to Float");
        }
        try {
            return Float.valueOf(input);
        } catch (Exception e) {
            throw new InvalidParameterTypeException("Failed to resolve Float input '" + input + "' to Float");
        }
    }
}