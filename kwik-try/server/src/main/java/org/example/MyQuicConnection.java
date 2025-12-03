package org.example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
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

    public MyQuicConnection(QuicConnection quicConnection, Logger logger) {
        this.quicConnection = quicConnection;
        this.log = logger;
    }

    @Override
    public void acceptPeerInitiatedStream(QuicStream stream) {
        new Thread(() -> handleClient(stream)).start();
    }

    private void handleClient(QuicStream stream) {
        try (InputStream in = stream.getInputStream()) {

            byte[] buf = new byte[4096];
            int len;

            while ((len = in.read(buf)) != -1) {
                if (len == 0)
                    continue;

                String msg = new String(buf, 0, len, StandardCharsets.UTF_8);
                log.info("server recv: " + msg);
            }

        } catch (Exception e) {
            log.error("stream failed", e);
        }
    }
}
