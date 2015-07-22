package com.company.Test;

import com.company.Player;
import com.company.Playlist;
import com.company.SongUrl;
import com.company.Status;
import javafx.embed.swing.JFXPanel;

/**
 * Created by hendriknieuwenhuis on 08/07/15.
 */
public class TestPlayer {

    private final String PATH = "resources/";

    private Player player;
    //private Playlist playlist;

    public TestPlayer() {
        new JFXPanel();

        player = new Player();
        player.setStatus(new Status());
        //playlist = createPlaylist();
        player.setPlaylist(createPlaylist());
    }

    private Playlist createPlaylist() {
        Playlist playlist = new Playlist();
        playlist.add(new SongUrl(PATH + "01-atheus-basic_reaction.mp3"));
        playlist.add(new SongUrl(PATH + "02-atheus-side_effex.mp3"));
        playlist.add(new SongUrl(PATH + "03-atheus-i_unedit.mp3"));
        return playlist;
    }

    public void printPlaylist() {
        System.out.printf("%d\n", player.getPlaylist().length());
        int j = 1;
        for (int i = 0; i < player.getPlaylist().length(); i++) {
            System.out.printf("%d. %s\n", j, player.getPlaylist().getSongUrl(j).getUrl());
            j++;
        }
    }

    public void start() {
        player.start();
    }

    public static void main(String[] args) {
        TestPlayer testPlayer = new TestPlayer();
        //testPlayer.printPlaylist();
        testPlayer.start();
    }
}
