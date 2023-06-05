package com.github.newk5.vf.server.core.exceptions;

import com.github.newk5.vf.server.core.utils.Log;

public class PluginExceptionHandler {

    public void throwEx(Throwable e) {
        Log.exception(e);
    }

    public void notifyExitCode(int exitCode, String message) {
        Log.error("%s: %d", message, exitCode);
    }
}