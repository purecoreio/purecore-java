package io.purecore.api.event;

import io.purecore.api.connection.Connection;

public class ConnectionCreatedEvent extends Event {

    Connection connection;

    public ConnectionCreatedEvent(Connection connection){
        this.connection=connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
