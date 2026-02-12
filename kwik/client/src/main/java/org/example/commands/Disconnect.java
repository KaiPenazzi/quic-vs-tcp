package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "disconnect", description = "closes connection by connection id")
public class Disconnect implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    @Option(names = { "--code" }, description = "error code")
    public long errorCode;

    @Option(names = { "--msg" }, description = "msg")
    public String msg;

    public Disconnect(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        this.handler.getConnection(this.connection_id).close(this.errorCode, this.msg);
    }
}
