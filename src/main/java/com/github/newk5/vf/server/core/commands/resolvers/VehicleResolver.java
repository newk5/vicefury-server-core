package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;


public class VehicleResolver extends BaseTypeResolver {

    public VehicleResolver() {
        super();
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve Vehicle input '" + input + "' to Vehicle");
        }
        try {
            Integer id = Integer.valueOf(input);

            return InternalServerEvents.server.getVehicle(id);

        } catch (Exception e) {

        }
        throw new InvalidParameterTypeException("Failed to resolve Vehicle input '" + input + "' to Vehicle");

    }

}
