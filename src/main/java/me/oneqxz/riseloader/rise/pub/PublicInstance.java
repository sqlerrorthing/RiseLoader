package me.oneqxz.riseloader.rise.pub;

import me.oneqxz.riseloader.rise.pub.interfaces.IPublic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PublicInstance {

    private static PublicInstance instance;
    private static final Logger log = LogManager.getLogger("PublicInstance");

    private IPublic scripts;

    private PublicInstance()
    {
        log.info("Getting public scripts...");
        this.scripts = new Scripts().updateData();

        log.info("All done! scripts: " + this.scripts.getData().length);
    }

    public static PublicInstance getInstance()
    {
        return instance == null ? instance = new PublicInstance() : instance;
    }


    public IPublic getScripts() {
        return scripts;
    }
}
