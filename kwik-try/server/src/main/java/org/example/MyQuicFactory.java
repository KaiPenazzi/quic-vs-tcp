package org.example;

import tech.kwik.core.QuicConnection;
import tech.kwik.core.log.Logger;
import tech.kwik.core.server.ApplicationProtocolConnection;
import tech.kwik.core.server.ApplicationProtocolConnectionFactory;

public class MyQuicFactory implements ApplicationProtocolConnectionFactory {
    private final Logger log;
    private MyGlobelState globelState;

    MyQuicFactory(Logger logger) {
        this.log = logger;
        this.globelState = new MyGlobelState();
    }

    @Override
    public int maxConcurrentPeerInitiatedUnidirectionalStreams() {
        return 10;
    }

    @Override
    public int maxConcurrentPeerInitiatedBidirectionalStreams() {
        return 10;
    }

    @Override
    public ApplicationProtocolConnection createConnection(String protocol, QuicConnection quicConnection) {
        return new MyQuicConnection(quicConnection, this.log, this.globelState);
    }
}
