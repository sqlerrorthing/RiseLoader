module me.oneqxz.riseloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.jetbrains.annotations;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.plugins;
    requires java.desktop;
    requires org.json;
    requires yamlconfiguration;
    requires java.management;
    requires org.apache.commons.io;
    requires org.apache.commons.compress;

    opens me.oneqxz.riseloader to javafx.fxml;
    exports me.oneqxz.riseloader;
}