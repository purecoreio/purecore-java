package io.purecore.api.event;

public interface Handler {

    // socket events

    /**
     * executed when the socket handshake has been performed
     */
    void onSocketReady(SocketReadyEvent event);

    /**
     * executed when the socket connection has been closed
     */
    void onSocketUnavailable(SocketUnavailableEvent event);

    // individual player connections creation events

    /**
     * executed when a connection is created for an individual player
     */
    void onConnectionCreated(ConnectionCreatedEvent event);

    /**
     * executed when there was an error while creating an individual connection
     */
    void onConnectionCreationError(ConnectionCreationErrorEvent event);

    // individual player connections closing events

    /**
     * executed when all the opened connections for that player are closed
     */
    void onConnectionsClosed(ConnectionsClosedEvent event);
    /**
     * executed when there was an error while closing the player's opened connections
     */
    void onConnectionsClosingError(ConnectionsClosingErrorEvent event);

    // initial connection pushing events

    /**
     * executed when the instance has just started and is pushing online players
     */
    void onConnectionsPushed(ServerConnectionsPushedEvent event);
    /**
     * executed when there was an error while creating multiple connections at once
     */
    void onConnectionsPushingError(ServerConnectionsPushingErrorEvent event);

    // final connection closing events

    /**
     * executed when the instance is shutting down and is closing all connections
     */
    void onServerConnectionsClosed(ServerConnectionsClosedEvent event);
    /**
     * executed when there was an error closing multiple connections from multiple players at once
     */
    void onServerConnectionsClosingError(ServerConnectionsClosingErrorEvent event);

}
