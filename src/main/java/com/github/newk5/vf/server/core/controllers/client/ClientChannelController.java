package com.github.newk5.vf.server.core.controllers.client;

import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.player.Player;


public abstract class ClientChannelController {

    protected Server server;

    public ClientChannelController(Server server) {
        this.server = server;
    }

    public void receiveData(Player p, String data) {

    }
}
