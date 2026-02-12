package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "getGState", description = "get global state")
public class GetGlobalState implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    public GetGlobalState(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        String msg = this.handler.sendResponse(this.connection_id, "gget");
        System.out.println(msg);
    }
}
