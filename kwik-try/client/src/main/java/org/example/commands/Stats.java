package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "stats", description = "get connection stats")
public class Stats implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    public Stats(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        System.out.println(this.handler.getConnection(this.connection_id).getStats());
    }
}
