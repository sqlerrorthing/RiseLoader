package me.oneqxz.riseloader.fxml.animations.personalization;

import animatefx.animation.AnimateFXInterpolator;
import animatefx.animation.AnimationFX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class TranslateScale extends AnimationFX {

    private final double size;

    public TranslateScale(Node node, double size)
    {
        this.size = size;
        setNode(node);
    }

    @Override
    protected AnimationFX resetNode() {
        getNode().scaleXProperty().setValue(1);
        getNode().scaleYProperty().setValue(1);
        return this;
    }

    @Override
    protected void initTimeline() {
        setTimeline(new Timeline(
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getNode().scaleXProperty(), size, AnimateFXInterpolator.EASE),
                        new KeyValue(getNode().scaleYProperty(), size, AnimateFXInterpolator.EASE)
                )
        ));
    }

}
