package me.oneqxz.riseloader.fxml.animations.personalization;

import animatefx.animation.AnimateFXInterpolator;
import animatefx.animation.AnimationFX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class TranslateY extends AnimationFX {

    private final int translateY;

    public TranslateY(Node node, int translateY)
    {
        this.translateY = translateY;
        setNode(node);
    }

    @Override
    protected AnimationFX resetNode() {
        getNode().translateYProperty().setValue(0);
        return this;
    }

    @Override
    protected void initTimeline() {
        setTimeline(new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(getNode().translateYProperty(), getNode().translateYProperty().getValue(), AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getNode().translateYProperty(), getNode().translateYProperty().getValue() + translateY, AnimateFXInterpolator.EASE)
                )
        ));
    }
}
