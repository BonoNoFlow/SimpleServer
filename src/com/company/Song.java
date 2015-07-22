package com.company;

/**
 * Created by hendriknieuwenhuis on 16/06/15.
 */
public class Song {

    private String artist;
    private String album;
    private String title;
    private int time;

    public Song(String artist, String album, String title, int time) {
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.time = time;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Song{" +
                "artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}
