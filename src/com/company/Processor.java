package com.company;



import java.util.*;


/**
 * Created by hendriknieuwenhuis on 29/06/15.
 */
public class Processor {

    //private MockPlayer player;

    private Player player;

    private Status status;

    private MusicIndex musicIndex;

    public Processor(Status status, MusicIndex musicIndex) {
        this.status = status;
        this.musicIndex = musicIndex;
    }

    public boolean playCommand(SimpleServer simpleServer) {
        System.out.printf("%s: %s from %s\n", getClass().getName(), simpleServer.getCommand().getCommandString(), Thread.currentThread().getName());

        switch (simpleServer.getCommand().getCommandString()) {
            case Properties.IDLE:
                new Idle(this, simpleServer);
                return true;
            case Properties.CURRENTSONG:
                /*
                Send a list containing the song info.
                 */
                /*
                List<String> list = new ArrayList<>();
                Song song = player.getPlaylist().getPlaylist().get(status.getCurrentSong());
                list.add(song.toString());

                list.add("OK");
                for (String s : list) {
                    System.out.println(s);
                }
                simpleServer.printer(list);
                */
                return true;
            case Properties.PAUSE:
                player.pause();
                return true;
            case Properties.PLAY:
                player.play();
                return true;
            case Properties.STOP:
                player.stop();
                return true;
            case Properties.LIST:
                simpleServer.printer(musicIndex.getFiles());
                return true;
            default:
                return false;
        }
    }

    /*
    public MockPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MockPlayer player) {
        this.player = player;
    }
    */

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
