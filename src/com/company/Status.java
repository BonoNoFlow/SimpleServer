package com.company;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by hendriknieuwenhuis on 31/05/15.
 */
public class Status extends Observable implements Serializable {

    private String playbackState;

    private int currentSong;

    public Status() {
        super();
    }

    public Status(int currentSong) {
        super();
        this.currentSong = currentSong;
    }

    public String getPlaybackState() {
        return playbackState;
    }

    public void setPlaybackState(String playbackState) {
        if (this.playbackState != playbackState) {
            setChanged();
        }
        this.playbackState = playbackState;
        notifyObservers(this.playbackState);
        System.out.printf("%s: %s\n", getClass().getName(), this.playbackState);
    }



    public int getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(int currentSong) {
        if (this.currentSong != currentSong) {
            setChanged();
        }
        this.currentSong = currentSong;
        notifyObservers(Properties.CHANGED_PLAYER);
        System.out.printf("%s: %s\n", getClass().getName(), "Idle should be notified!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        if (currentSong != status.currentSong) return false;
        return !(playbackState != null ? !playbackState.equals(status.playbackState) : status.playbackState != null);

    }

    @Override
    public int hashCode() {
        int result = playbackState != null ? playbackState.hashCode() : 0;
        result = 31 * result + currentSong;
        return result;
    }

    @Override
    public String toString() {
        return "Status{" +
                "playbackState='" + playbackState + '\'' +
                ", currentSong=" + currentSong +
                '}';
    }
}
