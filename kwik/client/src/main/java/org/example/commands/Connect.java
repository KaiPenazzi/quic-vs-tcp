package org.example.commands;

import org.example.MyConnectionHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "connect", description = "connect to sever")
public class Connect implements Runnable {
    private final MyConnectionHandler handler;

    @Parameters(index = "0", description = "URL")
    public String url;

    public Connect(MyConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            this.handler.createConnection(this.url);
        } catch (Exception e) {
            System.out.println("could not connect to: " + this.url);
            System.out.println(e);
        }
    }
}
