package com.company;

import java.util.HashMap;

/**
 * Created by hendriknieuwenhuis on 08/07/15.
 */
public class Playlist {

    private HashMap<Integer, SongUrl> list;

    private int listing;

    private Object lock = new Object();

    /*
    Initializes a new, empty Playlist.
     */
    public Playlist() {
        list = new HashMap<>();
        listing = 0;
    }

    /*
    Add a new song as last.
     */
    public void add(SongUrl songUrl) {
        synchronized (lock) {
            listing = list.size() + 1;
            list.put(listing, songUrl);
        }
    }

    /*
    Add a new song at a specified place.
     */
    public void addIn(SongUrl songUrl, int place) {
        synchronized (lock) {

        }

    }

    public SongUrl getSongUrl(int key) {
        return list.get(key);
    }

    public int length() {
        return list.size();
    }
}
