package me.oneqxz.riseloader.rise.run;

import javafx.application.Platform;
import javafx.scene.Node;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.components.impl.LaunchDebug;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
import me.oneqxz.riseloader.rise.RiseInfo;
import me.oneqxz.riseloader.settings.Settings;
import me.oneqxz.riseloader.utils.OSUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.*;

public class RunClient {

    private static final Logger log = LogManager.getLogger("ClientLaunch");

    public static void run(Node launchNode)
    {
        String osNatives = OSUtils.getOS() == OSUtils.OS.WINDOWS ? "windows" : "linux";
        File rootFolder = OSUtils.getRiseFolder().toFile();

        List<URL> urls = new ArrayList<>();
        try {
            File[] jars = new File(rootFolder, "rise\\" + Settings.getSettings().getString("rise.version", "release")).listFiles(pathname -> pathname.getName().endsWith(".jar"));
            for (int i = 0; i < Objects.requireNonNull(jars).length; i++)
                urls.add(jars[i].toURI().toURL());

            File[] natives = new File(rootFolder, "natives\\" + osNatives).listFiles(pathname -> (pathname.getName().endsWith(OSUtils.getOS() == OSUtils.OS.WINDOWS ? ".dll" : ".so")));
            for(int i = 0; i < Objects.requireNonNull(natives).length; i++)
                urls.add(natives[i].toURI().toURL());

        } catch (Exception x) {
            x.printStackTrace();
            MainScene.closeSelf();
            Platform.runLater(() -> new ErrorBox().show(x, false));
        }
        try
        {
            log.info("Version: "+Settings.getSettings().getString("rise.version")+", Client running...");

            StringBuilder classpath = new StringBuilder();
            classpath.append("\"");
            for (URL rl : urls) {
                if (classpath.length() > 1 && urls.indexOf(rl) != urls.size())
                    classpath.append(";");

                classpath.append(new File(rl.getPath()).getAbsolutePath());
            }
            classpath.append("\"");

            String startCommand = Settings.getSettings().getBoolean("others.javaoptimize", true) ? RiseInfo.getInstance().getOptimizedStartupCommand().getCommand() : RiseInfo.getInstance().getNormalStartupCommand().getCommand();
            startCommand = startCommand.replace("{JAVA}", rootFolder.getAbsolutePath() + "\\java\\" + OSUtils.getOS().name().toLowerCase() + "\\bin\\java" + (OSUtils.getOS() == OSUtils.OS.WINDOWS ? ".exe" : ""));
            startCommand = startCommand.replace("{MEMORY}", Settings.getSettings().getInt("preferences.memory") + "M");
            startCommand = startCommand.replace("{NATIVES}", rootFolder.getAbsolutePath() + "\\natives\\" + osNatives);
            startCommand = startCommand.replace("{CP}", classpath.toString());
            startCommand = startCommand.replace("{UUID}", UUID.randomUUID().toString());
            startCommand = startCommand.replace("{USERNAME}", "R" + new Random().nextInt(99999));
            startCommand = startCommand.replace("{SCREEN_RESOLUTION}", (Settings.getSettings().getBoolean("preferences.resolution.fullscreen", false)) ? "-fullscreen" : "-width " + String.valueOf(Settings.getSettings().getInt("preferences.resolution.width", 854)) + " -height " + String.valueOf(Settings.getSettings().getInt("preferences.resolution.height", 480)));
            startCommand = startCommand.replace("{SYS_TIME}", String.valueOf(System.currentTimeMillis()));
            startCommand = startCommand.replace("{FOLDER]", OSUtils.getRiseFolder().toFile().getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(startCommand.split(" "));
            pb.redirectErrorStream(true);

            File runDir = new File(rootFolder, "run");
            if(!runDir.exists())
                runDir.mkdirs();

            pb.directory(runDir);

            Map<String, String> env = pb.environment();
            env.put("CLASSPATH", classpath.toString());

            env.put("fml.ignoreInvalidMinecraftCertificates", "true");
            env.put("fml.ignorePatchDiscrepancies", "true");
            env.put("org.lwjgl.librarypath", rootFolder.getAbsolutePath() + "\\natives\\" + osNatives);
            env.put("net.java.games.input.librarypath", rootFolder.getAbsolutePath() + "\\natives\\" + osNatives);
            env.put("java.library.path", rootFolder.getAbsolutePath() + "\\natives\\" + osNatives);

            env.put("_JAVA_OPTS", "");
            env.put("_JAVA_OPTIONS", "");
            env.put("JAVA_OPTS", "");
            env.put("JAVA_OPTIONS", "");

            Process process = pb.start();
            LaunchDebug launchDebug = new LaunchDebug(process, startCommand);

            Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));
            Platform.runLater(() ->
            {
                try {
                    MainScene.removeChildren(launchNode);
                    MainScene.hideSelf();
                    launchDebug.show();
                } catch (IOException e) {
                    new ErrorBox().show(e);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Platform.runLater(() ->
            {
                MainScene.closeSelf();
                new ErrorBox().show(e);
            });
        }
    }

}
