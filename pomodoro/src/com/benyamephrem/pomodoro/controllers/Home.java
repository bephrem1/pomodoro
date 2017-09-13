package com.benyamephrem.pomodoro.controllers;

import com.benyamephrem.pomodoro.model.Attempt;
import com.benyamephrem.pomodoro.model.AttemptKind;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Home {

    @FXML private VBox container;

    @FXML private Label title;
    @FXML private TextArea message;

    private Attempt mCurrentAttempt;
    private StringProperty mTimerText;
    private Timeline mTimeline;
    private AudioClip mApplause;

    public Home(){
        mTimerText = new SimpleStringProperty();
        setTimerText(0);
        mApplause = new AudioClip(getClass().getResource("/sounds/applause.mp3").toExternalForm());
    }

    private void prepareAttempt(AttemptKind kind){
        reset();
        mCurrentAttempt = new Attempt(kind, "");
        addAttemptStyle(kind);
        title.setText(kind.getDisplayName());
        setTimerText(mCurrentAttempt.getRemainingSeconds());

        mTimeline = new Timeline();
        mTimeline.setCycleCount(kind.getTotalSeconds());

        //Set how big timeline is and what the timeline will do each frame
        mTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            mCurrentAttempt.tick();
            setTimerText(mCurrentAttempt.getRemainingSeconds());
        }));

        //Set what to do when the timeline is done
        mTimeline.setOnFinished(event -> {
            saveCurrentAttempt();
            mApplause.play();
            prepareAttempt(mCurrentAttempt.getKind() == AttemptKind.FOCUS ? AttemptKind.BREAK : AttemptKind.FOCUS);
        });
    }

    private void saveCurrentAttempt() {
        mCurrentAttempt.setMessage(message.getText());
        mCurrentAttempt.save();
    }

    private void reset() {
        clearAttemptStyles();

        if(mTimeline != null && mTimeline.getStatus() == Animation.Status.RUNNING){
            mTimeline.stop();
        }
    }

    private void addAttemptStyle(AttemptKind kind) {
        container.getStyleClass().add(kind.toString().toLowerCase());
    }

    //Clears the styles added to container.getStyleClass() because it
    //is a list and needs to be cleared before addAttemptStyle() is called
    //again
    private void clearAttemptStyles(){
        container.getStyleClass().remove("playing");
        for(AttemptKind kind : AttemptKind.values()){
            container.getStyleClass().remove(kind.toString().toLowerCase());
        }
    }

    public StringProperty timerTextProperty() {
        return mTimerText;
    }

    public String getTimerText() {
        return mTimerText.get();
    }

    public void setTimerText(String timerText) {
        mTimerText.set(timerText);
    }

    public void setTimerText(int remainingSeconds) {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        setTimerText(String.format("%02d:%02d", minutes, seconds));
    }

    public void handleRestart(ActionEvent actionEvent) {
        prepareAttempt(AttemptKind.FOCUS);
        playTimer();
    }


    public void handlePlay(ActionEvent actionEvent) {
        if(mCurrentAttempt == null){
            handleRestart(actionEvent);
        } else{
            playTimer();
        }
    }


    public void playTimer(){
        container.getStyleClass().add("playing");
        mTimeline.play();
    }

    public void handlePause(ActionEvent actionEvent) {
        pauseTimer();
    }

    public void pauseTimer(){
        container.getStyleClass().remove("playing");
        mTimeline.pause();
    }
}
