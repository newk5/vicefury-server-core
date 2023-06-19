package com.github.newk5.vf.server.core.commands.resolvers;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

public class GameObjectResolver extends BaseTypeResolver {

    public GameObjectResolver() {
        super();
        type = GameObject.class;
    }

    @Override
    public Object resolve(String input) throws InvalidParameterTypeException {
        if (input == null) {
            throw new InvalidParameterTypeException("Failed to resolve GameObject input '" + input + "' to GameObject");
        }
        try {
            Integer id = Integer.valueOf(input);

            return InternalServerEvents.server.getObject(id);
        } catch (Exception e) {

        }
        throw new InvalidParameterTypeException("Failed to resolve GameObject input '" + input + "' to GameObject");
    }
}
