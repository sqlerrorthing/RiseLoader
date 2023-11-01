package me.oneqxz.riseloader.fxml.controllers;

import javafx.scene.Parent;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public abstract class Controller {

    protected Parent root;
    protected Stage stage;

    public void init(Parent root, Stage stage)
    {
        this.root = root;
        this.stage = stage;
        this.init();
    }

    protected abstract void init();

    protected void onlyNumbers(TextInputControl text)
    {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                text.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    protected void rangedInput(TextInputControl text, int min, int max)
    {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            try
            {
                if(newValue.isEmpty())
                {
                    return;
                }

                int i = Integer.parseInt(newValue);
                if(i < min)
                    text.setText(String.valueOf(min));
                else if(i > max)
                    text.setText(String.valueOf(max));
            }
            catch (Exception e)
            {

            }
        });
    }

    protected void callPatternMath(TextInputControl text, String regex, CallRangeMath runnable)
    {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            runnable.call(Pattern.compile(regex).matcher(newValue).matches());
        });
    }

    public interface CallRangeMath {
        void call(boolean isMath);
    }
}
