package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public class PlayerResolver extends BaseTypeResolver {

    public PlayerResolver() {
        super();
        type = Player.class;
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve Player input '" + input + "' to Player");
        }
        try {
            Integer id = Integer.valueOf(input);

            return InternalServerEvents.server.getPlayer(id);
        } catch (Exception e) {

        }
        throw new InvalidParameterTypeException("Failed to resolve Player input '" + input + "' to Player");
    }
}
