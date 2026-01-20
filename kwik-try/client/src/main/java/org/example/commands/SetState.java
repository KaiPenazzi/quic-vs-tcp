package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "setState", description = "setze connection state")
public class SetState implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    @Parameters(index = "1", description = "value")
    public int value;

    public SetState(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        this.handler.sendResponse(this.connection_id, "set " + this.value);
    }
}
