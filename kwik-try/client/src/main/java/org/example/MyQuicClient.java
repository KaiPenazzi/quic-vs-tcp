package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.kwik.core.QuicClientConnection;
import tech.kwik.core.QuicStream;

public class MyQuicClient {

    private final URI uri;
    private QuicClientConnection connection;

    public MyQuicClient(String url) {
        this.uri = URI.create(url);
        System.out.println(this.uri.toString());
    }

    public void connect() throws Exception {
        connection = QuicClientConnection.newBuilder()
                .noServerCertificateCheck()
                .uri(this.uri)
                .applicationProtocol("quic")
                .build();

        connection.connect();
    }

    public String sendResponse(String msg) {
        return "";
    }

    public void sendUnidirectional(String msg) {

    }

    public void close() {

    }
}
