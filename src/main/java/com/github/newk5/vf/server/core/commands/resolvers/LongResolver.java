package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public class LongResolver extends BaseTypeResolver {

    public LongResolver() {
        super();
        type = Long.class;
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve Long input '" + input + "' to Long");
        }
        try {
            return Long.valueOf(input);
        } catch (Exception e) {
            throw new InvalidParameterTypeException("Failed to resolve Long input '" + input + "' to Long");
        }
    }
}