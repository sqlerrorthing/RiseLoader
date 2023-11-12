package me.oneqxz.riseloader.fxml.rpc;

import me.oneqxz.riseloader.RiseUI;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;

public class DiscordRichPresence {

    private static DiscordRichPresence instance;
    private String state;

    private DiscordRichPresence()
    {
        updateState("Initialization");
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {

        }).build();

        DiscordRPC.discordInitialize("1173162604055773184", handlers, true);

        net.arikia.dev.drpc.DiscordRichPresence drpc = new net.arikia.dev.drpc.DiscordRichPresence.Builder("RiseLoader v" + RiseUI.version.getVersion())
                .setBigImage("logo", "RiseLoader v" + RiseUI.version.getVersion())
                .setStartTimestamps((System.currentTimeMillis() / 1000L))
                .build();

        new Thread(() ->
        {
            for(;;)
            {
                drpc.details = state;
                updatePresence(
                        drpc
                );
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void updatePresence(net.arikia.dev.drpc.DiscordRichPresence drpc)
    {
        DiscordRPC.discordUpdatePresence(drpc);
    }

    public void updateState(String state)
    {
        this.state = state;
    }

    public void shutdown()
    {
        DiscordRPC.discordShutdown();
        DiscordRPC.discordClearPresence();
        instance = null;
    }

    public static DiscordRichPresence getInstance()
    {
        return instance == null ? instance = new DiscordRichPresence() : instance;
    }

    public static boolean isRegistered()
    {
        return instance != null;
    }
}
