package io.purecore.api.event;

public class ConnectionsClosingErrorEvent extends ErrorEvent {

    String error;

    public ConnectionsClosingErrorEvent(String error){
        this.error=error;
    }

    public String getError() {
        return error;
    }

}
