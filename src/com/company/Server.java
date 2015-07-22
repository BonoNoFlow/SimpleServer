package com.company;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hendriknieuwenhuis on 09/07/15.
 */
public class Server {

    private int port = 4444;
    private int maxConnections = 0;

    private boolean running;

    private Processor processor;

    public Server(Processor processor) {
        this.processor = processor;
        new Thread(new ServerThread()).start();
    }

    /*
    The serverThread listens for incoming connections to
    control the player.
     */
    private class ServerThread implements Runnable {

        @Override
        public void run() {

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

            //closing();


        }
    }
}
