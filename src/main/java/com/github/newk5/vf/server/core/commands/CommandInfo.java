package com.github.newk5.vf.server.core.commands;

import java.util.List;
import java.util.stream.Collectors;

public class CommandInfo {

    private CommandEntry entry;

    protected CommandInfo(CommandEntry entry) {
        this.entry = entry;
    }

    public String getName() {
        return entry.getName();
    }

    public boolean requiresAuth() {
        return entry.isRequiresAuth();
    }

    public int getParamCount() {
        return entry.getParamCount();
    }

    public String getSyntax() {
        return entry.getCommandAnn().syntax();
    }

    public boolean lastParamIsOptional() {
        return entry.getCommandAnn().lastParamIsOptional();
    }

    public boolean allParamsAreOptional() {
        return entry.getCommandAnn().allParamsAreOptional();
    }

    public List<Class> getParamTypes() {
        return entry.getParams().stream().map(CommandParam::getType).collect(Collectors.toList());
    }

}
