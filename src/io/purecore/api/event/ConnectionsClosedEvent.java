package io.purecore.api.event;

import io.purecore.api.connection.Connection;

import java.util.List;

public class ConnectionsClosedEvent extends Event {

    public List<Connection> connectionList;

    public ConnectionsClosedEvent(List<Connection> connectionList){
        this.connectionList=connectionList;
    }

    public List<Connection> getClosedConnections() {
        return connectionList;
    }
}
