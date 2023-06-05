package com.github.newk5.vf.server.core.commands;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.commands.annotations.Command;
import com.github.newk5.vf.server.core.commands.annotations.CommandController;
import com.github.newk5.vf.server.core.commands.resolvers.BaseTypeResolver;
import com.github.newk5.vf.server.core.commands.resolvers.BooleanResolver;
import com.github.newk5.vf.server.core.commands.resolvers.DoubleResolver;
import com.github.newk5.vf.server.core.commands.resolvers.FloatResolver;
import com.github.newk5.vf.server.core.commands.resolvers.IntegerResolver;
import com.github.newk5.vf.server.core.commands.resolvers.LongResolver;
import com.github.newk5.vf.server.core.commands.resolvers.NPCResolver;
import com.github.newk5.vf.server.core.commands.resolvers.PlayerResolver;
import com.github.newk5.vf.server.core.commands.resolvers.StringResolver;
import com.github.newk5.vf.server.core.commands.resolvers.VehicleResolver;
import com.github.newk5.vf.server.core.controllers.commands.ServerCommandController;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.exceptions.CommandParamCountException;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.tinylog.Logger;

public class CommandRegistry {

    private List<ServerCommand> allCommands = new ArrayList<>();
    private Map<String, Integer> cmdIndexCache = new HashMap<>();
    private Map<Class, BaseTypeResolver> typeResolvers = new HashMap<>();
    private Map<String, BaseTypeResolver> namedResolvers = new HashMap<>();
    private Map<String, ServerCommandController> commandControllers = new HashMap<>();
    public boolean logExceptionsWhenCmdsAreUsedWithoutParams = false;

    int index = 0;

    public void initilizeResolvers() {
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
                .registerTypeResolver(Vehicle.class, new VehicleResolver());
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
            ServerCommandController c = (ServerCommandController) controller.getConstructors()[0].newInstance(InternalServerEvents.server);
            CommandController cc = (CommandController) controller.getAnnotation(CommandController.class);
            if (!cc.name().equals("")) {
                commandControllers.put(cc.name(), c);
            } else {
                commandControllers.put(controller.getName() + "" + System.nanoTime(), c);
            }

            Stream.of(c.getClass().getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Command.class)).forEach(m -> {
                if (firstParamIsPlayer(m)) {
                    ServerCommand cmd = registerMethod(c, cc, m);
                    cmd.setIndex(index);
                    if (!cmdIndexCache.containsKey(cmd.getName())) {
                        cmdIndexCache.put(cmd.getName(), index);
                    } else {
                        String cmdController = cmd.getCommandController() != null ? cmd.getCommandController().getClass().getName() : "";
                        String msg = "WARNING: DUPLICATE COMMANDS FOR COMMAND: " + cmd.getName() + " FOUND, SKIPPED: " + cmdController + " " + cmd.getName();
                        warn(msg);
                        Logger.warn(msg);
                    }

                    cmd.getAlias().forEach(cmdAlias -> {

                        if (!cmdIndexCache.containsKey(cmdAlias)) {
                            cmdIndexCache.put(cmdAlias, index);
                        } else {
                            String cmdController = cmd.getCommandController() != null ? cmd.getCommandController().getClass().getName() : "";

                            String msg = "WARNING: DUPLICATE COMMANDS FOR COMMAND: " + cmd.getName() + " FOUND, SKIPPED: " + cmdController + " " + cmd.getName();
                            warn(msg);
                            Logger.warn(msg);
                        }
                    });
                    index++;
                } else {

                    String msg = "WARNING: IGNORED COMMAND: " + m.getDeclaringClass().getName() + "." + m.getName() + " MARKED WITH COMMAND ANNOTATION, BUT WITHOUT PLAYER PARAMETER ";

                    warn(msg);
                    Logger.warn(msg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(e);
        }
    }

    private void warn(String m) {
        System.out.println(ansi().fg(Ansi.Color.YELLOW).a(m).reset());
    }

    private void error(String m) {
        System.out.println(ansi().fg(Ansi.Color.RED).a(m).reset());
    }

    private boolean firstParamIsPlayer(Method m) {
        if (m.getParameterCount() > 0) {
            if (m.getParameters()[0].getType() == Player.class) {
                return true;
            }
        }
        return false;
    }

    private ServerCommand registerMethod(ServerCommandController c, CommandController cc, Method m) {
        Command cmdAn = m.getAnnotation(Command.class);
        List<String> aliases = Stream.of(cmdAn.alias()).filter(s -> !s.trim().equals("")).map(String::toLowerCase).collect(Collectors.toList());
        ServerCommand cmd = new ServerCommand(!cmdAn.name().equals("") ? cmdAn.name().toLowerCase() : m.getName().toLowerCase(), aliases, cc.requiresAuthentication() ? true : cmdAn.requiresAuthentication(), m.getParameterCount() - 1);
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

        ServerCommand cmd = index != null ? allCommands.get(index) : null;

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

            } catch (Exception e) {

                if (e instanceof CommandParamCountException && totalParams == 0) {
                    if (logExceptionsWhenCmdsAreUsedWithoutParams) {
                        e.printStackTrace();
                        Logger.error(e);
                    }
                } else {
                    e.printStackTrace();
                    Logger.error(e);
                }

                if (e instanceof CommandParamCountException || e instanceof InvalidParameterTypeException) {

                    cmd.getCommandController().onCommandSyntaxIncorrect(p, input.split(" ")[0], input, cmd.getCommandAnn().syntax());

                }
                cmd.getCommandController().onException(p, e, input);
            }
        }

    }
}
