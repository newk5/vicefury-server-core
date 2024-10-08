package com.github.newk5.vf.server.core.controllers;

import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.player.Player;

public abstract class CommandController {

    protected Server server;

    public CommandController(Server server) {
        this.server = server;
    }

    public void onAuthCheckFailed(Player p, String cmd, String input) {

    }

    public void onCommandSyntaxIncorrect(Player p, String cmd, String input, String correctSyntax) {

    }

    public boolean hasAccess(Player p, String cmd, String input) {
        return true;
    }

    public void onException(Player p, Exception e, String input) {

    }
}