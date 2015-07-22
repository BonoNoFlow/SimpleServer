package com.company;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

/**
 * Created by hendriknieuwenhuis on 13/06/15.
 */
public class MockPlayer extends Observable {

    private Status status;
    private MockPlaylist playlist;

    private boolean running;
    private boolean play;
    private RunPlayer runPlayer;

    public MockPlayer(Status status) {
        super();
        this.status = status;
        playlist = new MockPlaylist();
    }

    public boolean play() {
        this.notifyObservers();
        return true;
    }

    public boolean pause() {
        this.notifyObservers();
        return true;
    }

    public boolean stop() {
        this.notifyObservers();
        return true;
    }

    public boolean next() {
        return true;
    }

    public boolean previous() {
        return true;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public MockPlaylist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(MockPlaylist playlist) {
        this.playlist = playlist;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public Runnable getRunPlayer() {
        this.setPlay(true);
        return runPlayer = new RunPlayer();
    }


    private class RunPlayer implements Runnable {

        @Override
        public void run() {

            setRunning(true);

            while (isRunning()) {
                int time = playlist.getPlaylist().get(status.getCurrentSong()).getTime();

                System.out.printf("%s: %s\n",getClass().getName(), playlist.getPlaylist().get(status.getCurrentSong()).toString());


                while (isPlay()) {
                    time--;
                    if (time == 0) {
                        int current = status.getCurrentSong();
                        if (current < playlist.getPlaylist().size()) {
                            status.setCurrentSong(current + 1);
                        } else {
                            status.setCurrentSong(1);
                        }
                        break;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}
