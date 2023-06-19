package com.github.newk5.vf.server.core.commands;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.commands.annotations.Command;
import com.github.newk5.vf.server.core.commands.annotations.CommandHandler;
import com.github.newk5.vf.server.core.commands.resolvers.*;
import com.github.newk5.vf.server.core.controllers.CommandController;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.exceptions.CommandParamCountException;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;
import com.github.newk5.vf.server.core.utils.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandRegistry {

    private List<CommandEntry> allCommands = new ArrayList<>();
    private Map<String, Integer> cmdIndexCache = new HashMap<>();
    private Map<Class, BaseTypeResolver> typeResolvers = new HashMap<>();
    private Map<String, BaseTypeResolver> namedResolvers = new HashMap<>();
    private Map<String, CommandController> commandControllers = new HashMap<>();
    public boolean logExceptionsWhenCmdsAreUsedWithoutParams = false;

    int index = 0;

    public void initializeResolvers() {
        BooleanResolver bools = new BooleanResolver();
        FloatResolver floats = new FloatResolver();
        DoubleResolver doubles = new DoubleResolver();
        IntegerResolver integers = new IntegerResolver();
        LongResolver longs = new LongResolver();
        registerTypeResolver(Boolean.class, bools)
                .registerTypeResolver(boolean.class, bools)
                .registerTypeResolver(Double.class, doubles)
                .registerTypeResolver(double.class, doubles)
                .registerTypeResolver(Float.class, floats)
                .registerTypeResolver(float.class, floats)
                .registerTypeResolver(Integer.class, integers)
                .registerTypeResolver(int.class, integers)
                .registerTypeResolver(Long.class, longs)
                .registerTypeResolver(long.class, longs)
                .registerTypeResolver(String.class, new StringResolver())
                .registerTypeResolver(NPC.class, new NPCResolver())
                .registerTypeResolver(Player.class, new PlayerResolver())
                .registerTypeResolver(Vehicle.class, new VehicleResolver())
                .registerTypeResolver(GameObject.class, new GameObjectResolver());
    }

    public CommandRegistry registerTypeResolver(Class c, BaseTypeResolver r) {
        typeResolvers.put(c, r);
        return this;
    }

    public CommandRegistry registerNamedResolver(String name, BaseTypeResolver r) {
        namedResolvers.put(name, r);
        return this;
    }

    public BaseTypeResolver getTypeResolver(Class c) {
        return typeResolvers.get(c);
    }

    public <T> T getCommandController(String name) {
        return (T) commandControllers.get(name);
    }

    public void clearCommandControllers() {
        index = 0;
        commandControllers.clear();
        cmdIndexCache.clear();
        allCommands.clear();
    }

    public BaseTypeResolver getNamedResolver(String name) {
        return namedResolvers.get(name);
    }

    public void registerController(Class controller) {
        try {
            CommandController c = (CommandController) controller.getConstructors()[0].newInstance(InternalServerEvents.server);
            CommandHandler cc = (CommandHandler) controller.getAnnotation(CommandHandler.class);
            if (!cc.name().equals("")) {
                commandControllers.put(cc.name(), c);
            } else {
                commandControllers.put(controller.getName() + "" + System.nanoTime(), c);
            }

            Stream.of(c.getClass().getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Command.class)).forEach(m -> {
                if (firstParamIsPlayer(m)) {
                    CommandEntry cmd = registerMethod(c, cc, m);
                    cmd.setIndex(index);
                    if (!cmdIndexCache.containsKey(cmd.getName())) {
                        cmdIndexCache.put(cmd.getName(), index);
                    } else {
                        String cmdController = cmd.getCommandController() != null ? cmd.getCommandController().getClass().getName() : "";

                        Log.warn("DUPLICATE COMMANDS FOR COMMAND: %s FOUND, SKIPPED: %s %s", cmd.getName(), cmdController, cmd.getName());
                    }

                    cmd.getAlias().forEach(cmdAlias -> {

                        if (!cmdIndexCache.containsKey(cmdAlias)) {
                            cmdIndexCache.put(cmdAlias, index);
                        } else {
                            String cmdController = cmd.getCommandController() != null ? cmd.getCommandController().getClass().getName() : "";

                            Log.warn("DUPLICATE COMMANDS FOR COMMAND: %s FOUND, SKIPPED: %s %s", cmd.getName(), cmdController, cmd.getName());
                        }
                    });
                    index++;
                }
                else Log.warn("IGNORED COMMAND: %s.%s MARKED WITH COMMAND ANNOTATION, BUT WITHOUT PLAYER PARAMETER", m.getDeclaringClass().getName(), m.getName());
            });
        }
        catch (Exception e) { Log.exception(e); }
    }

    private boolean firstParamIsPlayer(Method m) {
        if (m.getParameterCount() > 0) {
            return m.getParameters()[0].getType() == Player.class;
        }
        return false;
    }

    private CommandEntry registerMethod(CommandController c, CommandHandler cc, Method m) {
        Command cmdAn = m.getAnnotation(Command.class);
        List<String> aliases = Stream.of(cmdAn.alias()).filter(s -> !s.trim().equals("")).map(String::toLowerCase).collect(Collectors.toList());
        CommandEntry cmd = new CommandEntry(!cmdAn.name().equals("") ? cmdAn.name().toLowerCase() : m.getName().toLowerCase(), aliases, cc.requiresAuthentication() ? true : cmdAn.requiresAuthentication(), m.getParameterCount() - 1);
        cmd.setCommandAnn(cmdAn);
        cmd.setCommandController(c);
        cmd.setControllerAnn(cc);
        cmd.setMethod(m);
        List<CommandParam> params = new ArrayList<>();

        Parameter lastParam = m.getParameters()[m.getParameterCount() - 1];
        Parameter firstParam = m.getParameters()[0];

        Stream.of(m.getParameters()).filter(p -> p != firstParam).forEach(param -> {
            boolean isLastParam = lastParam != null && lastParam.equals(param);
            CommandParam p = new CommandParam(cmd, this, false, param.getType(), isLastParam);
            p.setParam(param);

            p.setOptional(cmdAn.allParamsAreOptional() ? true : isLastParam && cmdAn.lastParamIsOptional());
            params.add(p);
        });
        cmd.setParams(params);
        allCommands.add(cmd);
        return cmd;
    }

    public void process(Player p, String input) {
        int totalParams = input.split(" ").length - 1;
        String cmdName = input.split(" ")[0].toLowerCase();
        Integer index = cmdIndexCache.get(cmdName);

        CommandEntry cmd = index != null ? allCommands.get(index) : null;
        if (cmd != null) {
            try {
                if (cmd.getControllerAnn().requiresAuthentication()) {
                    if (p == null || !p.isAuthenticated()) {
                        cmd.getCommandController().onAuthCheckFailed(p, input.split(" ")[0], input);
                        return;
                    }
                }
                if (cmd.getCommandAnn().requiresAuthentication()) {
                    if (p == null || !p.isAuthenticated()) {
                        cmd.getCommandController().onAuthCheckFailed(p, input.split(" ")[0], input);
                        return;
                    }
                }

                if (cmd.getCommandController().hasAccess(p, input.split(" ")[0], input)) {
                    Object[] player = new Object[]{p};
                    Object[] params = cmd.parseParams(cmdName, input);

                    Object[] merged = Arrays.copyOf(player, player.length + params.length);
                    System.arraycopy(params, 0, merged, player.length, params.length);
                    cmd.runCommand(merged);
                }
            }
            catch (Exception e) {
                if (e instanceof CommandParamCountException && totalParams == 0) {
                    if (logExceptionsWhenCmdsAreUsedWithoutParams) {
                        Log.exception(e);
                    }
                } else {
                    Log.exception(e);
                }

                if (e instanceof CommandParamCountException || e instanceof InvalidParameterTypeException) {
                    cmd.getCommandController().onCommandSyntaxIncorrect(p, input.split(" ")[0], input, cmd.getCommandAnn().syntax());
                }
                cmd.getCommandController().onException(p, e, input);
            }
        }
    }
}