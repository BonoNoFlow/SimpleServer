package com.company;

import java.util.HashMap;

/**
 * Created by hendriknieuwenhuis on 16/06/15.
 */
public class MockPlaylist {

    private HashMap<Integer, Song> playlist = new HashMap<Integer, Song>();


    public MockPlaylist() {
        playlist.put(1, new Song("Boo Lighters", "Pussy", "Last Song", 23));
        playlist.put(2, new Song("Hittlers Poncho", "Juden Raus", "Hello Darling", 34));
        playlist.put(3, new Song("ComputerWerk", "Digital Lebensraum", "You and Me", 27));

    }

    public HashMap<Integer, Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(HashMap<Integer, Song> playlist) {
        this.playlist = playlist;
    }

}
