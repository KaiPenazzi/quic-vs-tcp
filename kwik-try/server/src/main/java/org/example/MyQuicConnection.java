package org.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.lang.Thread;

import tech.kwik.core.QuicConnection;
import tech.kwik.core.QuicStream;
import tech.kwik.core.log.Logger;
import tech.kwik.core.server.ApplicationProtocolConnection;

public class MyQuicConnection implements ApplicationProtocolConnection {
    private final QuicConnection quicConnection;
    private final Logger log;
    private String ConnectionState;
    private MyGlobelState globelState;

    public MyQuicConnection(QuicConnection quicConnection, Logger logger, MyGlobelState state) {
        this.quicConnection = quicConnection;
        this.quicConnection.setConnectionListener(new MyConnectionListener(logger));
        this.log = logger;
        this.ConnectionState = "";
        this.globelState = state;
    }

    @Override
    public void acceptPeerInitiatedStream(QuicStream stream) {
        new Thread(() -> handleClient(stream)).start();
    }

    private void handleClient(QuicStream stream) {
        if (stream.isUnidirectional()) {
            try (InputStream in = stream.getInputStream()) {

                byte[] buf = new byte[4096];
                int len;

                while ((len = in.read(buf)) != -1) {
                    if (len == 0)
                        continue;

                    String msg = new String(buf, 0, len, StandardCharsets.UTF_8);
                    log.info(this.hashCode() + ": stream ID: " + stream.getStreamId() + ": " + msg);
                }

            } catch (Exception e) {
                log.error("stream failed", e);
            }
        }
        if (stream.isBidirectional()) {
            try (InputStream in = stream.getInputStream()) {

                byte[] buf = in.readAllBytes();
                String msg = new String(buf, StandardCharsets.UTF_8);
                log.info(this.hashCode() + ": stream ID: " + stream.getStreamId() + ": " + msg);

                String[] parts = msg.split(" ");

                switch (parts[0]) {
                    case "get":
                        this.send("state: " + this.ConnectionState, stream);
                        break;

                    case "set":
                        try {
                            this.ConnectionState = parts[1];
                            this.send("state: " + this.ConnectionState, stream);
                        } catch (IndexOutOfBoundsException e) {
                            this.send("value is missing: 'set <value>' ", stream);
                        }
                        break;
                    case "gget":
                        this.send("global state: " + this.globelState.state, stream);
                        break;

                    case "gset":
                        try {
                            this.globelState.state = parts[1];
                            this.send("global state: " + this.globelState.state, stream);
                        } catch (IndexOutOfBoundsException e) {
                            this.send("value is missing: 'gset <value>' ", stream);
                        }
                        break;
                    default:
                        this.send("echo: " + msg, stream);
                }

            } catch (Exception e) {
                log.error("stream failed", e);
            }
        }

    }

    private void send(String msg, QuicStream stream) {
        try (OutputStream out = stream.getOutputStream()) {
            out.write(msg.getBytes());
            out.close();
        } catch (Exception e) {
            log.error("could not send msg: ", e);
        }

    }
}
