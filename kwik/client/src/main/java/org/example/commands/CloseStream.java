package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "closeStream", description = "close stream on connection")
public class CloseStream implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    @Parameters(index = "1", description = "Stream ID")
    public int stream_id;

    public CloseStream(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        this.handler.getConnection(this.connection_id).close_stream(this.stream_id);
    }
}
