package io.purecore.api.event;

public interface Handler {

    void onConnected();
    void onDisconnected();
    void onNewExecutions();

}
