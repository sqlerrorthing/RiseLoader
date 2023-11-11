package me.oneqxz.riseloader.elua;

import javafx.application.Platform;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.utils.OSUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Elua {

    private static Elua elua;
    private File eluaFile;

    private Elua()
    {
        eluaFile = new File(OSUtils.getRiseFolder().toFile(), "elua");
    }


    public void acceptElua()
    {
        if(eluaFile.isDirectory())
        {
            try {
                FileUtils.deleteDirectory(eluaFile);
            }
            catch (Exception e)
            {
                Platform.runLater(() -> new ErrorBox().show(e));
                e.printStackTrace();
            }
        }

        if(eluaFile.exists())
            return;

        try {
            eluaFile.createNewFile();
        }
        catch (Exception e)
        {
            Platform.runLater(() -> new ErrorBox().show(e));
            e.printStackTrace();
        }
    }

    public boolean isEluaAccepted()
    {
        return eluaFile.exists() && !eluaFile.isDirectory();
    }

    public static Elua getElua()
    {
        return elua == null ? elua = new Elua() : elua;
    }
}
