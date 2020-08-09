package io.purecore.api.event;

public class ServerConnectionsClosingErrorEvent extends ErrorEvent {

    public String error;

    public ServerConnectionsClosingErrorEvent(String error){
        this.error=error;
    }

    public String getError() {
        return error;
    }
}
