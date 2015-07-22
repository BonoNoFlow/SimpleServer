package com.company;

import javafx.application.*;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hendriknieuwenhuis on 08/07/15.
 */
public class ServerApplication extends javafx.application.Application {

    private final String PATH = "resources/";

    private String HOME = FileSystemView.getFileSystemView().getRoots()[0].toString();

    private String[] args;

    private Status status;

    private MusicIndex musicIndex;

    private Processor processor;

    private Player player;
    private Playlist playlist;

    private int port = 4444;
    private int maxConnections = 0;

    private boolean running;

    public ServerApplication() {
        super();
    }

    public ServerApplication(String[] args) {
        super();
        this.args = args;
    }

    @Override
    public void init() {
        // init status!
        //this.initStatus();




        //System.out.printf("%s\n", "init done");

        musicIndex = new MusicIndex();
        musicIndex.setHome("/Volumes/elements/mp3/");
        try {
            musicIndex.index();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Playlist createPlaylist() {
        Playlist playlist = new Playlist();
        System.out.println(musicIndex.getSongs().get(5));
        playlist.add(new SongUrl(musicIndex.getSongs().get(15)));
        playlist.add(new SongUrl(musicIndex.getSongs().get(26)));
        playlist.add(new SongUrl(musicIndex.getSongs().get(37)));
        return playlist;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        playlist = createPlaylist();


        player = new Player();
        player.setStatus(new Status(3));
        player.setPlaylist(playlist);


        this.processor = new Processor(player.getStatus(), musicIndex);
        this.processor.setPlayer(this.player);



        player.start();



        new Server(processor);

    }

    /*
    Load the last saved status of the application, when
    there is no last save a prefixed status is initialized.
     */
    private void initStatus() {
        status = new Status();
        status.setPlaybackState("play");
        status.setCurrentSong(1);
    }


    private void server() {

        //int i = 0;
        running = true;
        try {
            ServerSocket listener = new ServerSocket(port);
            Socket server;

            //while ((i++ < maxConnections) || (maxConnections == 0)) {
            while (running) {


                server = listener.accept();


                new Thread(new SimpleServer(processor, server)).start();


            }

        } catch (IOException e) {
            System.out.printf("IOException on socket listen: %s\n", e);
            e.printStackTrace();
        }

        closing();

    }

    public void endServer() {
        running = false;
    }

    private void closing() {

    }

    public static void main(String[] args) throws IOException {

        ServerApplication.launch(args);
    }

}
