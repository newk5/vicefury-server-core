package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;


public class BooleanResolver extends BaseTypeResolver {
    
    public BooleanResolver() {
        super();
        type = Boolean.class;
    }
    
    @Override
    public Object resolve(String input)  throws InvalidParameterTypeException {
        if (input == null) {
            return false;
        }
        if (input.equals("no") || input.equals("n") || input.equals("false") || input.equals("0")) {
            return false;
        }
        if (input.equals("yes") || input.equals("y") || input.equals("yes") || input.equals("1")) {
            return true;
        }
        throw new InvalidParameterTypeException("Failed to resolve boolean input '"+input+"' to boolean");
    }
    
}
