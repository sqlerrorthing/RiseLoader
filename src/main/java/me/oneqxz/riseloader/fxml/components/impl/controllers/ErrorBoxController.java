package me.oneqxz.riseloader.fxml.components.impl.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import me.oneqxz.riseloader.fxml.controllers.Controller;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ErrorBoxController extends Controller {

    private Throwable exception;

    public ErrorBoxController(Throwable e)
    {
        this.exception = e;
    }

    Label message;
    TextArea stacktrace;

    @Override
    protected void init() {
        message = (Label) root.lookup("#errorMessage");
        stacktrace = (TextArea) root.lookup("#stacktrace");

        message.setText(exception.getMessage());
        stacktrace.setText(printStacktrace(exception).toString());
    }

    private StringBuilder printStacktrace(Throwable throwable)
    {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTraceString = sw.toString();

        StringBuilder sb = new StringBuilder();
        String[] stackTraceLines = stackTraceString.split(System.lineSeparator());

        sb.append(stackTraceLines[0]).append("\n");
        for(int i = 1; i < stackTraceLines.length; i++)
        {
            String str = stackTraceLines[i];
            sb.append(str);

            if(i < stackTraceLines.length - 1)
                sb.append("\n");
        }

        return sb;
    }

}
