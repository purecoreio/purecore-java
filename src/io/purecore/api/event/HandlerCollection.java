package io.purecore.api.event;

import java.util.ArrayList;
import java.util.List;

public class HandlerCollection {

    public List<Handler> handlers;

    public HandlerCollection(){
        this.handlers=new ArrayList<>();
    }

    public void addHandler(Handler handler){
        if(!handlers.contains(handler)){
            this.handlers.add(handler);
        }
    }

    public HandlerCollection(List<Handler> handlers){
        this.handlers=handlers;
    }

    public void emitConnectionCreatedEvent(ConnectionCreatedEvent event){
        for (Handler handler:handlers) {
            handler.onConnectionCreated(event);
        }
    }

    public void emitConnectionCreatedErrorEvent(ConnectionCreationErrorEvent event){
        for (Handler handler:handlers) {
            handler.onConnectionCreationError(event);
        }
    }

    public void emitConnectionClosedEvent(ConnectionsClosedEvent event){
        for (Handler handler:handlers) {
            handler.onConnectionsClosed(event);
        }
    }

    public void emitConnectionClosedErrorEvent(ConnectionsClosingErrorEvent event){
        for (Handler handler:handlers) {
            handler.onConnectionsClosingError(event);
        }
    }

    public void emitServerConnectionsClosedEvent(ServerConnectionsClosedEvent event){
        for (Handler handler:handlers) {
            handler.onServerConnectionsClosed(event);
        }
    }

    public void emitServerConnectionsClosedErrorEvent(ServerConnectionsClosingErrorEvent event){
        for (Handler handler:handlers) {
            handler.onServerConnectionsClosingError(event);
        }
    }

    public void emitServerConnectionsPushedEvent(ServerConnectionsPushedEvent event){
        for (Handler handler:handlers) {
            handler.onConnectionsPushed(event);
        }
    }

    public void emitServerConnectionsPushedErrorEvent(ServerConnectionsPushingErrorEvent event){
        for (Handler handler:handlers) {
            handler.onConnectionsPushingError(event);
        }
    }

    public void emitSocketReadyEvent(SocketReadyEvent event){
        for (Handler handler:handlers) {
            handler.onSocketReady(event);
        }
    }

    public void emitSocketUnavailableEvent(SocketUnavailableEvent event){
        for (Handler handler:handlers) {
            handler.onSocketUnavailable(event);
        }
    }

}
