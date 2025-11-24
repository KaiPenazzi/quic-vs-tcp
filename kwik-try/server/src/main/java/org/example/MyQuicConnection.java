package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import tech.kwik.core.QuicConnection;
import tech.kwik.core.QuicStream;
import tech.kwik.core.log.Logger;
import tech.kwik.core.server.ApplicationProtocolConnection;

public class MyQuicConnection implements ApplicationProtocolConnection {
    private final QuicConnection quicConnection;
    private final Logger log;

    public MyQuicConnection(QuicConnection quicConnection, Logger logger) {
        this.quicConnection = quicConnection;
        this.log = logger;
    }

    @Override
    public void acceptPeerInitiatedStream(QuicStream stream) {
        InputStream input = stream.getInputStream();
        try {
            byte[] buffer = input.readAllBytes();
            String receivedData = new String(buffer, StandardCharsets.UTF_8);
            this.log.info(stream.getStreamId() + ": " + receivedData);
        } catch (IOException e) {
            this.log.error("could not read message");
        }

        try {
            OutputStream output = stream.getOutputStream();
            output.write("hi from server".getBytes());
            output.close();
        } catch (IOException e) {
            this.log.error("could not send message");
        }

    }
}
