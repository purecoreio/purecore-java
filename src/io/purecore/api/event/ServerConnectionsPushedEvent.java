package io.purecore.api.event;

import io.purecore.api.connection.Connection;

import java.util.List;

public class ServerConnectionsPushedEvent extends Event {

    public List<Connection> connectionList;

    public ServerConnectionsPushedEvent(List<Connection> connectionList){
        this.connectionList=connectionList;
    }

    public List<Connection> getPushedConnections() {
        return connectionList;
    }
}
