package org.example;

import tech.kwik.core.ConnectionEstablishedEvent;
import tech.kwik.core.ConnectionListener;
import tech.kwik.core.ConnectionTerminatedEvent;
import tech.kwik.core.log.Logger;

class MyConnectionListener implements ConnectionListener {
    private Logger log;

    public MyConnectionListener(Logger logger) {
        this.log = logger;
    }

    @Override
    public void connected(ConnectionEstablishedEvent event) {
        log.info("connection established");
    }

    @Override
    public void disconnected(ConnectionTerminatedEvent event) {
        log.info("connection termiated: " + event.errorDescription());
    }
}
