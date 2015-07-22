package com.company;

import java.util.List;

/**
 * Created by hendriknieuwenhuis on 01/07/15.
 */
public class Command {

    private String command;

    // misschien bytes arrays!
    private List<String> feedback;

    private boolean keepAdding = false;
    private StringBuilder builder;

    public Command() {
        this.builder = new StringBuilder();
    }

    // Gives true when command is set. Returns false
    // when command is not set yet.
    public boolean setCommand(String value) {

        if (value.equals(Properties.COMMAND_LIST_BEGIN)) {
            // set iets om te blijven adden!
            keepAdding = true;
            return false;
        } else if (value.equals(Properties.COMMAND_LIST_END)) {
            // set adden uit!
            keepAdding = false;
            // send command!
            this.command = builder.toString();
            return true;
        } else {

            // add shit bij string builder!
            builder.append(value);
            // if not keepAdding
            System.out.printf("%s: %s\n", getClass().getName(), builder.toString());
            if (!keepAdding) {
                this.command = this.builder.toString();
                return true;
            }
            return false;

        }
    }

    public String getCommandString() {
        return command;
    }



}
