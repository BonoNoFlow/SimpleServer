package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 14/07/15.
 */
public class MusicIndex {

    private final String FOLDER = "folder:";
    private final String FILE = "file:";
    private final String MP3 = ".mp3";

    private String home;

    private List<String> files;

    private List<String> songs;

    /*
    Index creates an new List of Strings of the
    content in the home directory.
     */
    public void index() throws IOException {
        files = new ArrayList<>();
        songs = new ArrayList<>();

        Path path = FileSystems.getDefault().getPath(home);

        listFiles(path);

    }


    private void listFiles(Path path) throws IOException {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {

            for (Path entry : stream) {

                if (Files.isDirectory(entry)) {

                    files.add(FOLDER + entry.toString());

                    listFiles(entry);

                } else if (entry.toString().endsWith(MP3)) {

                    files.add(FILE + entry.toString());
                    songs.add(entry.toString());
                }

            }
            stream.close();
        }
    }

    public List<String> getSongs() {
        return songs;
    }

    public void print() {
        for (String s : files) {
            System.out.printf("%s\n", s);
        }
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public List<String> getFiles() {
        return files;
    }
}
