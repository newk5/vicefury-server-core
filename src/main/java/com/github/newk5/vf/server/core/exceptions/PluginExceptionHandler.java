package com.github.newk5.vf.server.core.exceptions;

import org.tinylog.Logger;

public class PluginExceptionHandler {
   public void throwEx(Throwable e) {
      Logger.error(e);
      e.printStackTrace();
   }
   public void notifyExitCode(int exitCode, String message){
       Logger.error(message+": "+exitCode);
       System.out.println(message+": "+exitCode);
   }
}
