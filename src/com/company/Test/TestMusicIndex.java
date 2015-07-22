package com.company.Test;

import com.company.MusicIndex;

import java.io.IOException;

/**
 * Created by hendriknieuwenhuis on 14/07/15.
 */
public class TestMusicIndex {

    public static void main(String[] args) {

        MusicIndex musicIndex = new MusicIndex();

        musicIndex.setHome("/Volumes/elements/mp3/");

        try {
            musicIndex.index();
        } catch (IOException e) {
            e.printStackTrace();
        }

        musicIndex.print();
    }
}
