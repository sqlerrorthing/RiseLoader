package me.oneqxz.riseloader.fxml.animations.personalization;

import animatefx.animation.AnimateFXInterpolator;
import animatefx.animation.AnimationFX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class TranslateDown extends AnimationFX {

    public TranslateDown(Node node) {
        super(node);
    }

    @Override
    protected AnimationFX resetNode() {
        return this;
    }

    @Override
    protected void initTimeline() {
        setTimeline(new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(getNode().translateYProperty(), getNode().translateYProperty().getValue() - 0, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getNode().translateYProperty(), getNode().translateYProperty().getValue() + 20, AnimateFXInterpolator.EASE)
                )
        ));
    }
}
