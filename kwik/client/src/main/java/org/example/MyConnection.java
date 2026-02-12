package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import tech.kwik.core.QuicClientConnection;
import tech.kwik.core.QuicStream;

public class MyConnection {

    private final URI uri;
    private QuicClientConnection connection;

    private List<QuicStream> streams;

    public MyConnection(String url) {
        this.uri = URI.create("quic://" + url);
        streams = new ArrayList<QuicStream>();
    }

    public void connect() throws Exception {
        connection = QuicClientConnection.newBuilder()
                .noServerCertificateCheck()
                .uri(this.uri)
                .applicationProtocol("quic")
                .maxIdleTimeout(Duration.ofMinutes(5))
                .build();

        connection.connect();
    }

    public String sendResponse(String msg) {
        try {
            QuicStream stream = this.connection.createStream(true);
            OutputStream out = stream.getOutputStream();
            InputStream in = stream.getInputStream();
            out.write(msg.getBytes());
            out.close();
            byte[] buf = in.readAllBytes();
            return new String(buf, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "could not send Message: " + e.toString();
        }
    }

    public void sendUnidirectional(String msg) {
        try {
            QuicStream stream = this.connection.createStream(false);
            OutputStream out = stream.getOutputStream();
            out.write(msg.getBytes());
            out.close();
        } catch (IOException e) {
            System.out.println("could not send Message: " + e.toString());
        }
    }

    public void send_msg_over(int stream_id, String msg) {
        QuicStream stream = this.streams.get(stream_id);
        try {
            OutputStream out = stream.getOutputStream();
            out.write(msg.getBytes());
            out.flush();
        } catch (Exception e) {
            System.out.println("could not send msg: " + e.toString());
        }
    }

    public int open_stream() throws IOException {
        QuicStream stream = connection.createStream(false);
        this.streams.add(stream);
        return this.streams.size() - 1;
    }

    public void close_stream(int stream_id) {
        QuicStream stream = this.streams.get(stream_id);
        try {
            OutputStream out = stream.getOutputStream();
            out.close();
        } catch (IOException e) {
            System.out.println("could not close stream id: " + stream_id + "\n" + e.toString());
        }
    }

    public void close() {
        for (int i = 0; i < this.streams.size(); i++) {
            this.close_stream(i);
        }
        this.connection.close();
    }

    public void close(long errorCode, String msg) {
        for (int i = 0; i < this.streams.size(); i++) {
            this.close_stream(i);
        }
        this.connection.close(errorCode, msg);
    }

    public String getStats() {
        return this.connection.getStats().toString();
    }
}
