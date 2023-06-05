package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.client.ChannelController;
import com.github.newk5.vf.server.core.events.ServerEventHandler;
import com.github.newk5.vf.server.core.commands.annotations.CommandController;
import com.github.newk5.vf.server.core.controllers.client.ClientChannelController;
import com.github.newk5.vf.server.core.events.BaseServerEvents;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.atteo.classindex.ClassIndex;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.fusesource.jansi.AnsiConsole;
import org.tinylog.Logger;

public class PluginLoader {

    public static InternalServerEvents baseEvents;
    public static String launchPath;

    public PluginLoader() {
    }

    public PluginLoader(InternalServerEvents events, String libPath) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, URISyntaxException {

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().getParent().getParent().getParent().toString();
        System.setProperty("tinylog.directory", s);
        launchPath = s;

        baseEvents = events;
        events.clearData();
        AnsiConsole.systemInstall();
        long start = System.currentTimeMillis();
        Server server = new Server();
        Logger.info(("LOADING LIB AT: " + libPath));


        loadEvents(events, server);
        loadCommandControllers(server);
        loadClientControllers(server);

        System.load(libPath);

        long end = System.currentTimeMillis();
        System.out.println("");
        String r = (end - start) + "ms";
        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Java plugin initialized (" + r + ")").reset());

    }

    public void loadClientControllers(Server server) {
        Iterable clientChannelControllers = ClassIndex.getAnnotated(ChannelController.class);
        Iterator it = clientChannelControllers.iterator();

        while (it.hasNext()) {
            try {
                Class c = (Class) it.next();
                ClientChannelController ev = (ClientChannelController) c.getConstructors()[0].newInstance(server);
                InternalServerEvents.channelControllers.put(ev.getClass().getAnnotation(ChannelController.class).value(), ev);
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.error(ex);
            }
        }
    }

    public void loadCommandControllers(Server server) {
        server.commandRegistry.initilizeResolvers();
        for (Class<?> c : ClassIndex.getAnnotated(CommandController.class)) {
            server.commandRegistry.registerController(c);
        }
    }

    public void loadEvents(InternalServerEvents events, Server server) {
        Iterable eventClasses = ClassIndex.getAnnotated(ServerEventHandler.class);
        Iterator eventClassesIt = eventClasses.iterator();

        while (eventClassesIt.hasNext()) {
            try {
                Class c = (Class) eventClassesIt.next();
                BaseServerEvents ev = (BaseServerEvents) c.getConstructors()[0].newInstance();
                ev.server = server;
                InternalServerEvents.server = server;
                events.addEventHandler(c.getName(), ev);
              
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.error(ex);
            }
        }
    }
}
