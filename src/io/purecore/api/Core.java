package io.purecore.api;

import com.google.gson.Gson;
import io.purecore.api.connection.Connection;
import io.purecore.api.event.*;
import io.purecore.api.exception.ApiException;
import io.purecore.api.exception.CallException;
import io.purecore.api.instance.Instance;
import io.purecore.api.key.Key;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Core {

    public enum Mode {
        UNKNOWN,
        LISTENER,
        TALKER
    }

    private Mode mode;
    private final String key;

    private static Map<String,Instance> instance;
    private static Map<String,String> lastKey;

    private static Map<String, Socket> socket;
    private static Map<String, Boolean> socketAvailable;

    private static HandlerCollection handlerManager = new HandlerCollection();

    public Core(String key, Mode mode, Handler handler) {
        this.key=key;
        this.mode=mode;
        handlerManager.addHandler(handler);
        setupSocket();
    }

    public Core(String key, Mode mode) {
        this.key=key;
        this.mode=mode;
        setupSocket();
    }

    public static HandlerCollection getHandlerManager() {
        return handlerManager;
    }

    public boolean isSocketAvailable() {
        return socketAvailable.get(getKey());
    }

    public Core(String key) {
        this.key=key;
        this.mode=Mode.UNKNOWN;
        setupSocket();
    }

    public Core() {
        this.key=null;
        this.mode=Mode.UNKNOWN;
        setupSocket();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public Core(Key key) {
        this.key=key.getHash();
        setupSocket();
    }

    public Core(Core core){
        this.key= core.getKeyLegacy().getHash();
        this.mode = core.getMode();
        setupSocket();
    }

    public Socket getSocket() {
        return socket.get(this.getKey());
    }

    public Core getCore(){
        return this;
    }

    public String getKey() {
        return key;
    }

    public Key getKeyLegacy(){
        return new Key(this.key);
    }

    public Instance getInstance() throws ApiException, IOException, CallException, JSONException {
        if(!this.getKey().equals(lastKey.get(getKey()))){
            lastKey.put(getKey(),key);
            instance.put(getKey(),new Instance(this));
        }
        return instance.get(getKey());
    }

    public void setupSocket() {
        Gson gson = new Gson();
        if(socket==null){
            socketAvailable.put(getKey(),false);
            try{
                socket.put(getKey(),IO.socket("https://socket.purecore.io:3000"));
                socket.get(getKey()).on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        socket.get(getKey()).emit("handshakeRequest",getKey());
                    }
                }).on("handshake", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        socketAvailable.put(getKey(),true);
                        handlerManager.emitSocketReadyEvent(new SocketReadyEvent());
                    }
                }).on("connectionCreated", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        handlerManager.emitConnectionCreatedEvent(new ConnectionCreatedEvent(new Connection(getCore(),null,null,null,null,null)));
                    }
                }).on("connectionCreationError", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        handlerManager.emitConnectionCreatedErrorEvent(new ConnectionCreationErrorEvent(Arrays.toString(args)));
                    }
                }).on("connectionsClosed", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        List<Connection> connectionList = new ArrayList<>();
                        handlerManager.emitConnectionClosedEvent(new ConnectionsClosedEvent(connectionList));
                    }
                }).on("connectionsClosingError", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        handlerManager.emitConnectionClosedErrorEvent(new ConnectionsClosingErrorEvent(Arrays.toString(args)));
                    }
                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        socketAvailable.put(getKey(),false);
                        handlerManager.emitSocketUnavailableEvent(new SocketUnavailableEvent());
                    }
                });
                socket.get(getKey()).connect();
            } catch (URISyntaxException exception){
                // ignore
            }
        }
    }
}
