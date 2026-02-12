package org.example.commands;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "open_stream", description = "open a stream")
public class OpenStream implements Callable<Integer> {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    public OpenStream(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public Integer call() throws IOException {
        return this.handler.getConnection(this.connection_id).open_stream();
    }
}
