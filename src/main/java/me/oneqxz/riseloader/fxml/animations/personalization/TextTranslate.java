package me.oneqxz.riseloader.fxml.animations.personalization;

import animatefx.animation.AnimateFXInterpolator;
import animatefx.animation.AnimationFX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TextTranslate extends AnimationFX {

    private final String beforeText;
    private final String afterText;
    private final int translateY;

    public TextTranslate(Text node, String beforeText, String afterText, int translateY)
    {
        this.beforeText = beforeText;
        this.afterText = afterText;
        this.translateY = translateY;
        setNode(node);
    }

    @Override
    protected AnimationFX resetNode() {
        ((Text) getNode()).textProperty().setValue(beforeText);
        ((Text) getNode()).translateYProperty().setValue(translateY);
        ((Text) getNode()).opacityProperty().setValue(1);
        return this;
    }

    @Override
    protected void initTimeline() {
        setTimeline(new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(getNode().opacityProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(getNode().translateYProperty(), getNode().translateYProperty().getValue(), AnimateFXInterpolator.EASE)

                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(getNode().opacityProperty(), 0, AnimateFXInterpolator.EASE),
                        new KeyValue(((Text) getNode()).textProperty(), afterText, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getNode().translateYProperty(), getNode().translateYProperty().getValue() + translateY, AnimateFXInterpolator.EASE),
                        new KeyValue(getNode().opacityProperty(), 1, AnimateFXInterpolator.EASE)
                )
        ));
    }
}
