module me.oneqxz.riseloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.plugins;
    requires java.desktop;
    requires org.json;
    requires yamlconfiguration;
    requires java.management;

    opens me.oneqxz.riseloader to javafx.fxml;
    exports me.oneqxz.riseloader;
}