package com.company;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by hendriknieuwenhuis on 11/05/15.
 *
 * The <code>SimpleServer</code> is the thread
 * that handles the incoming connections from
 * a client.
 */
public class SimpleServer implements Runnable {

    // The socket connection with the client.
    private Socket server;

    private Object lock = new Object();

    // The Command object holding the command
    // given by the client.
    private Command command;

    private Processor processor;

    private PrintStream out;

    public SimpleServer(Processor processor, Socket server) {
        super();
        this.server = server;
        this.processor = processor;
    }

    @Override
    public void run() {

        try {
            InputStream in = server.getInputStream();
            out = new PrintStream(server.getOutputStream());

            StringBuilder line = new StringBuilder();

            command = new Command();

            int read;

            while ((read = in.read()) != -1) {

                if (read == 10) {

                    line.append((char) read);

                    boolean commandSet = command.setCommand(line.toString());

                    if (!commandSet) {

                        continue;

                    } else if (commandSet) {

                        // execute the command!
                        if (processor.playCommand(this)) {
                            //System.out.println("SimpleServer: executing command");
                            break;
                        }

                        //   has to be exception
                        // !!!! ---- !!!! ---- !!!!
                        break;

                    } else {
                        //System.out.println("What the fuck!");
                        break;
                    }
                }
                line.append((char) read);
            }

            System.out.printf("%s: closing client %s, %d threads active.\n",getClass().getName(), Thread.currentThread().getName(),  Thread.activeCount());

            server.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printer(String print) {
        out.print(print + "\n");
    }

    public void printer(List<String> list) {
        list.add("OK");
        for (String s : list) {
            s += "\n";
            out.print(s);
        }
    }

    public Object getLock() {
        return lock;
    }

    public Command getCommand() {
        return command;
    }

}
