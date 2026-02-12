package org.example;

import java.util.ArrayList;
import java.util.List;

public class MyConnectionHandler {
    private List<MyConnection> clients;

    public MyConnectionHandler() {
        this.clients = new ArrayList<MyConnection>();
    }

    public void createConnection(String url) throws Exception {
        MyConnection client = new MyConnection(url);
        client.connect();
        this.clients.add(client);
    }

    public String sendResponse(int connection_id, String msg) {
        return this.clients.get(connection_id).sendResponse(msg);
    }

    public MyConnection getConnection(int connection_id) {
        return this.clients.get(connection_id);
    }

    public void closeAll() {
        for (MyConnection client : clients) {
            client.close();
        }
    }
}
