package me.oneqxz.riseloader.fxml.animations.personalization;

import animatefx.animation.AnimateFXInterpolator;
import animatefx.animation.AnimationFX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class RotateMinus180 extends AnimationFX {

    public RotateMinus180(Node n)
    {
        super(n);
    }

    @Override
    protected AnimationFX resetNode() {
        getNode().rotateProperty().setValue(0);

        return this;
    }

    @Override
    protected void initTimeline() {
        setTimeline(new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(getNode().rotateProperty(), getNode().rotateProperty().getValue(), AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getNode().rotateProperty(), getNode().rotateProperty().getValue() - 180, AnimateFXInterpolator.EASE)
                )
        ));
    }
}
