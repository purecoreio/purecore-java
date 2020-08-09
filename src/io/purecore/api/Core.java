package io.purecore.api;

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

public class Core {

    public enum Mode {
        UNKNOWN,
        LISTENER,
        TALKER
    }

    private Mode mode;
    private final String key;

    private static Instance instance;
    private static String lastKey;

    private static Socket socket;
    private static boolean socketAvailable;

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

    public static boolean isSocketAvailable() {
        return socketAvailable;
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
        socket=getSocket();
        setupSocket();
    }

    public static Socket getSocket() {
        return socket;
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
        if(!this.getKey().equals(lastKey)){
            this.lastKey=key;
            this.instance= new Instance(this);
        }
        return instance;
    }

    public void setupSocket() {
        if(socket==null){
            socketAvailable=false;
            try{
                socket = IO.socket("http://localhost:3000");
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        socket.emit("handshakeRequest",getKey());
                    }
                }).on("handshake", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        socketAvailable=true;
                        handlerManager.emitSocketReadyEvent(new SocketReadyEvent());
                    }
                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        socketAvailable=false;
                        handlerManager.emitSocketUnavailableEvent(new SocketUnavailableEvent());
                    }
                });
                socket.connect();
            } catch (URISyntaxException exception){
                // ignore
            }
        }
    }
}
