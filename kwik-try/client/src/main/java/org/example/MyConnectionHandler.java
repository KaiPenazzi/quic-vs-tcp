package org.example;

import java.util.ArrayList;
import java.util.List;

public class MyConnectionHandler {
    private List<MyQuicClient> clients;

    public MyConnectionHandler() {
        this.clients = new ArrayList<MyQuicClient>();
    }

    public void createConnection(String url) throws Exception {
        MyQuicClient client = new MyQuicClient(url);
        client.connect();
        this.clients.add(client);
    }

    public String sendResponse(int connection_id, String msg) {
        return this.clients.get(connection_id).sendResponse(msg);
    }

    public MyQuicClient getConnection(int connection_id) {
        return this.clients.get(connection_id);
    }

    public void closeAll() {
        for (MyQuicClient client : clients) {
            client.close();
        }
    }
}
