package com.company;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hendriknieuwenhuis on 02/06/15.
 *
 * Observes the <code>Status</code> object for
 * changes and passes that on to the client.
 *
 * The client that gives the <code>IDLE</code>
 * command will stay connected until the
 * status is changed.
 * The client then gets notified and this
 * object is deleted. The client has to
 * log back on with  the <code>IDLE</code>
 * command to listen for new changes.
 */
public class Idle implements Observer {

    private SimpleServer simpleServer;
    private Processor processor;

    private boolean waiting;

    private String changed;

    public Idle(Processor processor, SimpleServer simpleServer) {
        this.processor = processor;
        this.processor.getStatus().addObserver(this);
        this.simpleServer = simpleServer;
        waiting = true;
        monitor();
    }



    /*
    Set the thread that got the <code>IDLE</code>
    command to wait. After a change occured and
    the thread is notified and will print to the
    client what is changed and removes this object
    from the observers list.
     */
    private void monitor() {

        synchronized (simpleServer.getLock()) {
            while (waiting) {
                try {
                    System.out.printf("%s: Into the waiting game!\n", getClass().getName());
                    simpleServer.getLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        // Print the changed status to the client.
        List<String> reply = new ArrayList<>();
        reply.add(changed);
        simpleServer.printer(reply);


        // Remove this object from observing status. This object
        // ends! Otherwise it keeps getting called till garbage collecting.
        //
        // kan dit anders??? -----
        //
        this.processor.getPlayer().getStatus().deleteObserver(this);
    }


    // Notify the thread to continue the run!
    @Override
    public void update(Observable o, Object arg) {
        changed = (String) arg;
        System.out.printf("%s: %s\n",getClass().getName(), changed);
        synchronized (simpleServer.getLock()) {
            waiting = false;
            simpleServer.getLock().notify();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Idle idle = (Idle) o;

        if (waiting != idle.waiting) return false;
        if (!simpleServer.equals(idle.simpleServer)) return false;
        return !(changed != null ? !changed.equals(idle.changed) : idle.changed != null);

    }

    @Override
    public int hashCode() {
        int result = simpleServer.hashCode();
        result = 31 * result + (waiting ? 1 : 0);
        result = 31 * result + (changed != null ? changed.hashCode() : 0);
        return result;
    }
}
