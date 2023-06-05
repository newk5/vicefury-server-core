package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;


public class NPCResolver extends BaseTypeResolver {

    public NPCResolver() {
        super();
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve NPC input '" + input + "' to NPC");
        }
        try {
            Integer id = Integer.valueOf(input);

            return InternalServerEvents.server.getNPC(id);

        } catch (Exception e) {

        }
        throw new InvalidParameterTypeException("Failed to resolve NPC input '" + input + "' to NPC");

    }

}
