package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "setGState", description = "setze global state")
public class SetGlobalState implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    @Parameters(index = "1", description = "value")
    public int value;

    public SetGlobalState(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        this.handler.sendResponse(this.connection_id, "gset " + this.value);
    }
}
