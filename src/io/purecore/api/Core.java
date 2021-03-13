package io.purecore.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.purecore.api.call.ApiException;
import io.purecore.api.call.Call;
import io.purecore.api.call.Param;
import io.purecore.api.event.Handler;
import io.purecore.api.instance.Instance;
import io.purecore.api.key.Key;
import io.purecore.api.versioning.Game;
import io.purecore.api.versioning.GameSoftware;
import io.purecore.api.versioning.Version;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class Core {

    protected static Key key;
    protected static OkHttpClient client = new OkHttpClient();

    protected static Socket socket;
    protected static String clientVersion;
    protected static boolean connected = false;
    protected static ArrayList<Handler> handlers = new ArrayList<>();

    public static String getClientVersion(){
        return Core.clientVersion;
    }

    public static void setClientVersion(String version){
        Core.clientVersion=version;
    }

    public Core(String key){
        Core.key = new Key(key);
        if(Core.client==null){
            Core.client = new OkHttpClient();
        }
        if(Core.socket==null){
            Core.connect();
        }
    }

    public Core(String key, Handler handler){
        Core.key = new Key(key);
        if(Core.client==null){
            Core.client = new OkHttpClient();
        }
        if(Core.socket==null){
            Core.connect();
        }
        if(!Core.handlers.contains(handler)){
            Core.handlers.add(handler);
        }
    }

    protected static Key getKey(){
        return key;
    }

    public static boolean isConnected(){
        return Core.connected;
    }

    public static void emit(String key, String string){
        Core.socket.emit(key,string);
    }
    public static void emit(String key, JsonElement data) throws JSONException {
        Core.socket.emit(key,new JSONObject(new Gson().toJson(data)));
    }
    public static void emit(String key, HashMap<String,Object> data) throws JSONException {
        Core.emit(key, new Gson().toJsonTree(data));
    }

    public Instance getInstance(){
        return new Instance();
    }

    public Version getVersionCandidate(Game game, GameSoftware gameSoftware, String gameVersion) throws IOException, ApiException {
        JsonElement element = Core.call("/version/get/candidate/")
                .addParam(Param.GameVersion,gameVersion)
                .addParam(Param.Game, String.valueOf(game))
                .addParam(Param.GameSoftware, String.valueOf(gameSoftware))
                .commit();
        return new Version(element.getAsJsonObject());
    }

    private static void connect(){

        URI uri = URI.create("https://socket.purecore.io:443/");
        IO.Options options = new IO.Options();
        options.path="/instances/";

        Core.socket = IO.socket(uri,options);
        Core.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Handler handler:Core.handlers){
                    handler.onAuthenticating();
                }
                try {
                    // try sending version acknowledgement
                    HashMap<String, Object> data = new HashMap<>();
                    data.put(Param.Key.getParam(),Core.key.getHash());
                    data.put(Param.Version.getParam(), Core.getClientVersion());
                    Core.emit("handshake", data);
                } catch (JSONException e) {
                    // json failed, send standalone key
                    Core.emit("handshake", Core.key.getHash());
                }
            }
        }).on("connected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                // connected and authenticated
                for (Handler handler:Core.handlers){
                    handler.onConnected();
                }
                Core.connected=true;
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Handler handler:Core.handlers){
                    handler.onDisconnected();
                }
                Core.connected=false;
            }
        }).on("checkExecutions", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Handler handler:Core.handlers){
                    handler.onNewExecutions();
                }
            }
        }).on("checkVersion", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JsonElement body = new Gson().fromJson(String.valueOf(objects[0]), JsonElement.class);
                Version version = new Version(body.getAsJsonObject());
                for (Handler handler:Core.handlers){
                    handler.onNewVersion(version);
                }
            }
        }).on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Handler handler:Core.handlers){
                    handler.onConnecting();
                }
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Handler handler:Core.handlers){
                    handler.onConnectError();
                }
            }
        }).on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Handler handler:Core.handlers){
                    handler.onReconnecting();
                }
            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Handler handler:Core.handlers){
                    handler.onAuthenticating();
                }
                Core.socket.emit("handshake",Core.key.getHash());
            }
        });
        for (Handler handler:Core.handlers){
            handler.onSocketCreated();
        }
        Core.socket.connect();
    }

    public static Call call(String endpoint){
        return new Call(endpoint, Core.key, Core.client);
    }

}
