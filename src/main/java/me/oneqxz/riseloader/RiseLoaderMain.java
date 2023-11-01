package me.oneqxz.riseloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RiseLoaderMain {

    private static final Logger log = LogManager.getLogger("RiseLoader");

    public static void main(String[] args) {
        log.info("Starting RiseLoader...");
        RiseUI.main(new String[0]);
    }

}
