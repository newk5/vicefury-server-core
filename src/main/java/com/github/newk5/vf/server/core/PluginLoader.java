package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.client.ChannelController;
import com.github.newk5.vf.server.core.commands.annotations.CommandController;
import com.github.newk5.vf.server.core.controllers.client.ClientChannelController;
import com.github.newk5.vf.server.core.events.BaseServerEvents;
import com.github.newk5.vf.server.core.events.ServerEventHandler;
import com.github.newk5.vf.server.core.utils.Log;
import org.atteo.classindex.ClassIndex;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class PluginLoader {

    public static InternalServerEvents baseEvents;
    public static String launchPath;
    

    public PluginLoader() { }

    public PluginLoader(InternalServerEvents events, String libPath) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, URISyntaxException {

        Path currentRelativePath = Paths.get("");
        launchPath = currentRelativePath.toAbsolutePath().getParent().getParent().getParent().toString();
        System.setProperty("tinylog.directory", launchPath);

        baseEvents = events;
        events.clearData();
        AnsiConsole.systemInstall();
        long start = System.currentTimeMillis();
        Server server = new Server();
        Log.info("LOADING LIB AT: %s", libPath);

        loadEvents(events, server);
        loadCommandControllers(server);
        loadClientControllers(server);

        System.load(libPath);
        Log.success("Java plugin initialized (%dms)", (System.currentTimeMillis() - start));
    }

    public void loadClientControllers(Server server) {
        Iterator it = ClassIndex.getAnnotated(ChannelController.class).iterator();

        while (it.hasNext()) {
            try {
                Class c = (Class) it.next();
                ClientChannelController ev = (ClientChannelController) c.getConstructors()[0].newInstance(server);
                InternalServerEvents.channelControllers.put(ev.getClass().getAnnotation(ChannelController.class).value(), ev);
            }
            catch (Exception e) { Log.exception(e); }
        }
    }

    public void loadCommandControllers(Server server) {
        server.commandRegistry.initializeResolvers();
        for (Class<?> c : ClassIndex.getAnnotated(CommandController.class)) {
            server.commandRegistry.registerController(c);
        }
    }

    public void loadEvents(InternalServerEvents events, Server server) {
        Iterator it = ClassIndex.getAnnotated(ServerEventHandler.class).iterator();

        while (it.hasNext()) {
            try {
                Class c = (Class) it.next();
                BaseServerEvents ev = (BaseServerEvents) c.getConstructors()[0].newInstance();
                ev.server = server;
                InternalServerEvents.server = server;
                events.addEventHandler(c.getName(), ev);
            }
            catch (Exception e) { Log.exception(e); }
        }
    }
}