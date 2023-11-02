package me.oneqxz.riseloader.rise.run;

import javafx.application.Platform;
import javafx.scene.Node;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.fxml.components.impl.LaunchDebug;
import me.oneqxz.riseloader.fxml.scenes.MainScene;
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
        String mainClass = "net.minecraft.client.main.Main";


        String osNatives = OSUtils.getOS() == OSUtils.OS.WINDOWS ? "windows" : "linux";
        File rootFolder = OSUtils.getRiseFolder().toFile();

        List<URL> urls = new ArrayList<>();
        try {
            File[] jars = new File(rootFolder, "rise").listFiles(pathname -> pathname.getName().endsWith(".jar"));
            for (int i = 0; i < Objects.requireNonNull(jars).length; i++)
                urls.add(jars[i].toURI().toURL());

            File[] natives = new File(rootFolder, "natives\\" + osNatives).listFiles(pathname -> (pathname.getName().endsWith(OSUtils.getOS() == OSUtils.OS.WINDOWS ? ".dll" : ".so")));
            for(int i = 0; i < Objects.requireNonNull(natives).length; i++)
                urls.add(natives[i].toURI().toURL());

        } catch (Exception x) {
            x.printStackTrace();
            MainScene.closeSelf();
            new ErrorBox().show(x);
        }
        try
        {
            log.info("Client running...");

            StringBuilder classpath = new StringBuilder();
            classpath.append("\"");
            for (URL rl : urls) {
                if (classpath.length() > 1 && urls.indexOf(rl) != urls.size())
                    classpath.append(";");

                classpath.append(new File(rl.getPath()).getAbsolutePath());
            }
            classpath.append("\"");

            List<String> command = new ArrayList<>();
            command.add(rootFolder.getAbsolutePath() + "\\java\\" + OSUtils.getOS().name().toLowerCase() + "\\bin\\java" + (OSUtils.getOS() == OSUtils.OS.WINDOWS ? ".exe" : ""));
            command.add("-noverify");
            command.add("-Xmx" + Settings.getSettings().getInt("preferences.memory") + "M");
            command.add("-Xms" + Settings.getSettings().getInt("preferences.memory") + "M");
            command.add("-Djava.library.path=" + rootFolder.getAbsolutePath() + "\\natives\\" + osNatives);
            command.add("-Dfile.encoding=UTF-8");
            command.add("-Dsun.stdout.encoding=UTF-8");


            if(Settings.getSettings().getBoolean("others.javaoptimize", true))
            {
                command.add("-XX:+DisableAttachMechanism");
                command.add("-XX:+UseG1GC");
                command.add("-XX:+DisableExplicitGC");
                command.add("-XX:+UseNUMA");
                command.add("-XX:MaxTenuringThreshold=15");
                command.add("-XX:MaxGCPauseMillis=30");
                command.add("-XX:GCPauseIntervalMillis=150");
                command.add("-XX:-UseGCOverheadLimit");
                command.add("-XX:SurvivorRatio=8");
                command.add("-XX:TargetSurvivorRatio=90");
                command.add("-XX:MaxTenuringThreshold=15");
                command.add("-Dfml.ignorePatchDiscrepancies=true");
                command.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
                command.add("-XX:+UseCompressedOops");
                command.add("-XX:+OptimizeStringConcat");
                command.add("-XX:ReservedCodeCacheSize=2048m");
                command.add("-XX:+UseCodeCacheFlushing");
                command.add("-XX:SoftRefLRUPolicyMSPerMB=10000");
                command.add("-XX:ParallelGCThreads=10");
            }

            command.add("-classpath");
            command.add(classpath.toString());
            command.add(mainClass);

            command.add("-uuid");
            command.add(UUID.randomUUID().toString());

            command.add("-accessToken");
            command.add("yes");

            command.add("-version");
            command.add("1");

            command.add("-assetIndex");
            command.add("1.8");

            command.add("-username");
            command.add("R" + new Random().nextInt(99999));

            if(Settings.getSettings().getBoolean("preferences.resolution.fullscreen", false))
                command.add("-fullscreen");
            else
            {
                command.add("-width");
                command.add(String.valueOf(Settings.getSettings().getInt("preferences.resolution.width", 854)));
                command.add("-height");
                command.add(String.valueOf(Settings.getSettings().getInt("preferences.resolution.height", 480)));
            }

            ProcessBuilder pb = new ProcessBuilder(command);
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
            LaunchDebug launchDebug = new LaunchDebug(process);

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
            MainScene.closeSelf();
            new ErrorBox().show(e);
        }
    }

}
