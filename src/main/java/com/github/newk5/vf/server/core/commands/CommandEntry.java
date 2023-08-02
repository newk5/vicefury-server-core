package com.github.newk5.vf.server.core.commands;

import com.github.newk5.vf.server.core.commands.annotations.Command;
import com.github.newk5.vf.server.core.commands.annotations.CommandHandler;
import com.github.newk5.vf.server.core.controllers.CommandController;
import com.github.newk5.vf.server.core.exceptions.CommandParamCountException;
import com.github.newk5.vf.server.core.exceptions.CommandTypeResolverNotFound;
import com.github.newk5.vf.server.core.exceptions.InvalidParameterTypeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandEntry {

    private int index;
    private String name;
    private List<String> alias;
    private boolean requiresAuth;
    public int paramCount;
    private List<CommandParam> params;
    private CommandController commandController;
    private CommandHandler controllerAnn;
    private Command commandAnn;
    private Method method;

    public CommandEntry() {
    }

    public CommandEntry(String name, List<String> alias, boolean requiresAuth, int paramCount) {
        this.name = name;
        this.alias = alias;
        this.requiresAuth = requiresAuth;
        this.paramCount = paramCount;
    }

    public Object[] parseParams(String cmdNameUsed, String rawInput) throws CommandParamCountException, CommandTypeResolverNotFound, InvalidParameterTypeException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        rawInput = rawInput.trim();
        List<String> paramValues = Stream.of(rawInput.split(" ")).filter(s -> !s.equalsIgnoreCase(cmdNameUsed)).collect(Collectors.toList());

        boolean noOptionals = !commandAnn.allParamsAreOptional() && !commandAnn.lastParamIsOptional();

        if (noOptionals && paramValues.size() != paramCount) {
            throw new CommandParamCountException("Parameter count mismatch in command: " + commandController.getClass().getName() + "." + method.getName() + ", Expected number of parameters: " + paramCount + " Received: " + paramValues.size());
        }

        List<Object> convertedValues = new ArrayList<>();
        for (int i = 0; i < paramCount; i++) {
            CommandParam param = params.get(i);
            String value = null;
            if (param.isOptional()) {
                try {
                    value = paramValues.get(i);
                    convertedValues.add(param.resolveType(value));
                } catch (IndexOutOfBoundsException e) {
                    if (param.hasNamedResolver()) {
                        Object v = param.resolveType(value);
                        convertedValues.add(v);
                    } else {
                        convertedValues.add(null);
                    }
                }
            } else {
                try {
                    value = paramValues.get(i);
                    convertedValues.add(param.resolveType(value));
                } catch (Exception e) {
                    //just so we throw the exception
                    param.resolveType(null);
                }
            }

        }
        return convertedValues.toArray();
    }

    public void runCommand(Object[] params) throws CommandParamCountException, CommandTypeResolverNotFound, InvalidParameterTypeException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.invoke(commandController, params);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public void setCommandController(CommandController commandController) {
        this.commandController = commandController;
    }

    public CommandHandler getControllerAnn() {
        return controllerAnn;
    }

    public void setControllerAnn(CommandHandler controllerAnn) {
        this.controllerAnn = controllerAnn;
    }

    public Command getCommandAnn() {
        return commandAnn;
    }

    public void setCommandAnn(Command commandAnn) {
        this.commandAnn = commandAnn;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public boolean isRequiresAuth() {
        return requiresAuth;
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }

    public List<CommandParam> getParams() {
        return params;
    }

    public void setParams(List<CommandParam> params) {
        this.params = params;
    }
}
