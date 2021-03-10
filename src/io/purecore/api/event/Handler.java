package io.purecore.api.event;

import io.purecore.api.versioning.Version;

public interface Handler {

    void onConnected();
    void onConnecting();
    void onAuthenticating();
    void onReconnecting();
    void onConnectError();
    void onSocketCreated();
    void onDisconnected();
    void onNewExecutions();
    void onNewVersion(Version version);

}
