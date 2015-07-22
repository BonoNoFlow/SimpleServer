package com.company;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Track;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Observable;

/**
 * Created by hendriknieuwenhuis on 08/07/15.
 */
public class Player extends Observable {

    private URL resource;
    private Media media;
    private MediaPlayer mediaPlayer;

    private ObservableMap<String, Object> currentSong;

    private boolean play;
    private boolean pause;
    private boolean stop;

    private Status status;
    private Playlist playlist;

    private Object lock = new Object();
    private boolean playerLock;
    private boolean mediaPlayerSet;

    public Player() {

    }

    public void start() {
        new Thread(new PlayMedia()).start();
    }

    /*
    Interpret the status of the player as the last saved status!
     */
    private void preparePlayer() {
        if (mediaPlayer == null || media != null) {
            mediaPlayer = new MediaPlayer(media);
        }

        if (status.getPlaybackState().equals(Properties.STOP)) {
            this.setStop(true);
        } else if (status.getPlaybackState().equals(Properties.PAUSE)) {
            this.setPause(true);
        } else if (status.getPlaybackState().equals(Properties.PLAY)){
            this.setStop(false);
            this.setPause(false);
        }

    }

    public void play() {
        // status.setPlaybackState(Properties.PLAY);
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)) {
            mediaPlayer.play();
        } else {
            start();
        }
    }

    public void stop() {
        setStop(true);
        mediaPlayer.stop();
    }

    public void pause() {
        //setPause(true);
        //status.setPlaybackState(Properties.PAUSE);
        mediaPlayer.pause();
    }

    private boolean isPause() {
        return pause;
    }

    private void setPause(boolean pause) {
        this.pause = pause;
    }

    private boolean isStop() {
        return stop;
    }

    private void setStop(boolean stop) {
        this.stop = stop;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    private void handleMetadata(String key, Object value) {
        if (key.equals("album")) {
            System.out.printf("Album: %s\n", value.toString());
        } else if (key.equals("artist")) {
            System.out.printf("Artist: %s\n", value.toString());
        } if (key.equals("title")) {
            System.out.printf("Title: %s\n", value.toString());
        } if (key.equals("year")) {
            System.out.printf("Year: %s\n", value.toString());

        }
    }

    /*
    The runnable that runs the actual playing of
    a audio file.
     */
    private class PlayMedia implements Runnable {

        @Override
        public void run() {

            /*
            The stop boolean is set false
            to the player plays or loads the current song
            and goes in pause mode.
             */
            setStop(false);

            /*
            Get the song key to play. The key
            comes from the status object variable
            currentSong.
            */
            //int i = status.getCurrentSong();


            while (!isStop()) {

                // check if we're not calling a NullPointerException.
                if (status.getCurrentSong() <= playlist.length()) {


                    // load the media.

                    media = new Media(Paths.get(playlist.getSongUrl(status.getCurrentSong()).getUrl()).toUri().toString());

                    // get the metadata of the media.
                    media.getMetadata().addListener(new MapChangeListener<String, Object>() {
                        @Override
                        public void onChanged(Change<? extends String, ?> change) {
                            if (change.wasAdded()) {
                                handleMetadata(change.getKey(), change.getValueAdded());
                            }
                        }
                    });

                    // initiate the media player object.
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setVolume(0.1);
                    mediaPlayer.setOnError(new ErrorPrinter());
                    mediaPlayer.setOnEndOfMedia(new EndSongNotifier());
                    mediaPlayer.setOnStopped(new StopSongNotifier());
                    mediaPlayer.setOnPaused(new PauseNotifier());
                    mediaPlayer.setOnPlaying(new PlayNotifier());

                    // dit moet hier weg
                    // of een if statement die de status checked!!!
                    mediaPlayerSet = true;
                    mediaPlayer.play();

                    System.out.printf("%s in %s plays %s\n", getClass().getName(), Thread.currentThread().getName(), playlist.getSongUrl(status.getCurrentSong()).getUrl());

                    playerLock = true;

                    /*
                    Pause the thread till a incomming command or the
                    songs end, notifies the thread to continue.
                     */
                    pauseThread();



                    mediaPlayerSet = false;
                    mediaPlayer.dispose();

                    /*
                    When the player is stopped the thread should
                    be closed. It breaks out of the while loop!
                    The status is set to stop and the current song
                    is set to one.
                     */
                    if (isStop()) {
                        status.setPlaybackState(Properties.STOP);
                        status.setCurrentSong(1);
                        break;
                    }



                    status.setCurrentSong((status.getCurrentSong() + 1));
                } else {
                    //setStop(true);
                    //status.setPlaybackState(Properties.STOP);
                    break;
                }

            }

        }

        private void pauseThread() {
            synchronized (lock) {
                while (playerLock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private class EndSongNotifier implements Runnable {

        @Override
        public void run() {
            playerLock = false;
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    private class StopSongNotifier implements Runnable {

        @Override
        public void run() {
            System.out.printf("%s: Stop notified\n", getClass().getName());
            status.setPlaybackState(Properties.STOP);
            status.setCurrentSong(1);
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    private class PauseNotifier implements Runnable {

        @Override
        public void run() {
            System.out.printf("%s: Pause notified\n", getClass().getName());
            status.setPlaybackState(Properties.PAUSE);
        }
    }
    private class PlayNotifier implements Runnable {

        @Override
        public void run() {
            System.out.printf("%s: Play notified\n", getClass().getName());
            status.setPlaybackState(Properties.PLAY);
        }
    }

    private class ErrorPrinter implements Runnable {

        @Override
        public void run() {
            String error = media.getError().getMessage();

            System.out.printf("Player error: %s\n", error);
        }
    }
}
