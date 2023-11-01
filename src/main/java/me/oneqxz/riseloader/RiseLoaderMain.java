package me.oneqxz.riseloader;

import me.oneqxz.riseloader.utils.OSUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RiseLoaderMain {

    private static final Logger log = LogManager.getLogger("RiseLoader");

    public static void main(String[] args) {
        log.info("Starting RiseLoader...");

        if(!OSUtils.getRiseFolder().toFile().exists())
            OSUtils.getRiseFolder().toFile().mkdirs();

        RiseUI.main(new String[0]);
    }

}
