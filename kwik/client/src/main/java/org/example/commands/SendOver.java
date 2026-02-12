package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "sendOver", description = "sends msg over selected connection and selected stream")
public class SendOver implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "Connection ID")
    public int connection_id;

    @Parameters(index = "1", description = "Stream ID")
    public int stream_id;

    @Parameters(index = "2", description = "msg")
    public String msg;

    public SendOver(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        this.handler.getConnection(this.connection_id).send_msg_over(this.stream_id, this.msg);
    }
}
