package io.purecore.api.event;

import io.purecore.api.connection.Connection;

import java.util.List;

public class ServerConnectionsClosedEvent extends Event {

    List<Connection> connectionList;

    public ServerConnectionsClosedEvent(List<Connection> connectionList){
        this.connectionList=connectionList;
    }

    public List<Connection> getClosedConnections() {
        return connectionList;
    }
}
