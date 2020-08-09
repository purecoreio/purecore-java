package io.purecore.api.event;

public class ServerConnectionsPushingErrorEvent extends ErrorEvent {

    public String error;

    public ServerConnectionsPushingErrorEvent(String error){
        this.error=error;
    }

    public String getError() {
        return error;
    }
}
