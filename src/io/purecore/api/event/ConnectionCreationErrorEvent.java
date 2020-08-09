package io.purecore.api.event;

public class ConnectionCreationErrorEvent extends ErrorEvent {

    public String error;

    public ConnectionCreationErrorEvent(String error){
        this.error=error;
    }

    public String getError() {
        return error;
    }
}
