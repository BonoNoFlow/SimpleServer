package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hendriknieuwenhuis on 15/06/15.
 */
public class MockController {

    private final String PLAY = "play";
    private final String STOP = "stop";
    private final String PAUSE = "pause";
    private final String NEXT = "next";
    private final String PREVIOUS = "previous";
    private final String IDLE = "idle";
    private final String CURRENTSONG = "currentsong";

    private MockPlayer player;
    private MockPlaylist playlist;
    private SimpleServer simpleServer;


    private RunPlayer runPlayer;

    private boolean running;
    private boolean play;



    public boolean playerCommand(String command) {
        System.out.println("MockController: Begin playerCommand(String command): " + command);
        switch (command) {
            case PLAY:
                return setPlay(player.play());

            case STOP:
                setPlay(player.stop());
                /*
                Let the runPlayer thread wait!
                 */
                if (runPlayer != null) {
                    while (!isPlay()) {
                        try {
                            runPlayer.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            case PAUSE:
                return setPlay(player.pause());

            case NEXT:
                return setPlay(player.next());

            case PREVIOUS:
                return setPlay(player.previous());

            case IDLE:
                //new Idle(simpleServer);
                return true;
            case CURRENTSONG:
                System.out.println("MockController: currentsong");
                /*
                Send a list containing the song info.
                 */
                List<String> list = new ArrayList<>();
                Song song = playlist.getPlaylist().get(player.getStatus().getCurrentSong());
                list.add(song.toString());

                list.add("OK");
                for (String s : list) {
                    System.out.println(s);
                }
                simpleServer.printer(list);
                return true;
            default:
                return false;
        }
    }

    private class RunPlayer implements Runnable {

        @Override
        public void run() {
            running = true;
            while (running) {
                int time = playlist.getPlaylist().get(player.getStatus().getCurrentSong()).getTime();

                System.out.println("MockController - RunPlayer: " + playlist.getPlaylist().get(player.getStatus().getCurrentSong()).toString());


                while (isPlay()) {
                    time--;
                    if (time == 0) {
                        int current = player.getStatus().getCurrentSong();
                        if (current < playlist.getPlaylist().size()) {
                            player.getStatus().setCurrentSong(current + 1);
                        } else {
                            player.getStatus().setCurrentSong(1);
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



    public boolean setPlay(boolean play) {
        this.play = play;
        return play;
    }

    public boolean isPlay() {
        return play;
    }

    public Runnable getRunPlayer() {
        this.setPlay(true);
        return runPlayer = new RunPlayer();
    }

    public MockPlaylist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(MockPlaylist playlist) {
        this.playlist = playlist;
    }

    public MockPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MockPlayer player) {
        this.player = player;
    }

    public void setSimpleServer(SimpleServer simpleServer) {
        this.simpleServer = simpleServer;
    }


}
