package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.client.annotations.ClientChannelHandler;
import com.github.newk5.vf.server.core.commands.annotations.CommandHandler;
import com.github.newk5.vf.server.core.controllers.BaseEventController;
import com.github.newk5.vf.server.core.controllers.ClientChannelController;
import com.github.newk5.vf.server.core.events.annotations.EventHandler;
import com.github.newk5.vf.server.core.utils.Log;
import org.atteo.classindex.ClassIndex;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Iterator;

public class PluginLoader {

    public static InternalServerEvents baseEvents;
    public static String launchPath;

    public PluginLoader() {
    }

    public PluginLoader(InternalServerEvents events, String libPath) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, URISyntaxException {

        launchPath = Paths.get("").toAbsolutePath().getParent().getParent().getParent().toString();
        System.setProperty("tinylog.directory", launchPath);

        baseEvents = events;
        events.clearData();
        AnsiConsole.systemInstall();
        long start = System.currentTimeMillis();
        Server server = new Server();
        Log.info("LOADING LIB AT: %s", libPath);

        loadEventControllers(server);
        loadCommandControllers(server);
        loadClientControllers(server);

        System.load(libPath);
        Log.success("Java plugin initialized (%dms)", (System.currentTimeMillis() - start));
    }

    public void loadEventControllers(Server server) {
        InternalServerEvents.server = server;
        Iterator<Class<?>> it = ClassIndex.getAnnotated(EventHandler.class).iterator();

        while (it.hasNext()) {
            try {
                Class<?> c = it.next();
                BaseEventController ev = (BaseEventController) c.getConstructors()[0].newInstance();
                ev.server = server;

                if (c.isAnnotationPresent(EventHandler.class)) {
                    EventHandler eh = c.getAnnotation(EventHandler.class);
                    ev.setPosition(eh.position());
                    ev.setControllerName(c.getName());
                }
                ev.setControllerName(c.getName());
                baseEvents.addEventHandler(c.getName(), ev);

            } catch (Exception e) {
                Log.exception(e);
            }
        }
        baseEvents.sortEventHandlers();
    }

    public void loadCommandControllers(Server server) {
        server.commandRegistry.initializeResolvers();
        for (Class<?> c : ClassIndex.getAnnotated(CommandHandler.class)) {
            server.commandRegistry.registerController(c);
        }
    }

    public void loadClientControllers(Server server) {
        Iterator<Class<?>> it = ClassIndex.getAnnotated(ClientChannelHandler.class).iterator();

        while (it.hasNext()) {
            try {
                Class<?> c = it.next();
                ClientChannelController ev = (ClientChannelController) c.getConstructors()[0].newInstance(server);
                InternalServerEvents.channelControllers.put(ev.getClass().getAnnotation(ClientChannelHandler.class).value(), ev);
            } catch (Exception e) {
                Log.exception(e);
            }
        }
    }
}
