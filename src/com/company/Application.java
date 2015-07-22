package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hendriknieuwenhuis on 20/06/15.
 */
public class Application {

    private String[] args;

    private Status status;
    private MockPlayer player;

    private Processor processor;

    private int port = 4444;
    private int maxConnections = 0;

    private boolean running;

    public Application(String[] args) {
        this.args = args;
    }

    public void runApplication() {
        // do stuff with args!

        // init status!
        this.initStatus();

        this.player = new MockPlayer(status);



        //this.processor = new Processor(status);
        //this.processor.setPlayer(this.player);

        //new Thread(processor.getPlayer().getRunPlayer()).start();

        this.server();

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

    /*
    The server listens for incoming connections to
    control the player.
     */
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
}
