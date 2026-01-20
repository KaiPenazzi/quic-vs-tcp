package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "setState", description = "setze connection state")
public class GetState implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    public GetState(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        String msg = this.handler.sendResponse(this.connection_id, "get");
        System.out.println("get: " + msg);
    }
}
