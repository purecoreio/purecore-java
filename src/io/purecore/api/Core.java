package io.purecore.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.purecore.api.call.Call;
import io.purecore.api.event.Handler;
import io.purecore.api.instance.Instance;
import io.purecore.api.key.Key;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class Core {

    protected static Key key;
    protected static OkHttpClient client = new OkHttpClient();

    protected static Socket socket;
    protected static boolean connected = false;
    protected static ArrayList<Handler> handlers = new ArrayList<>();

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
        Core.handlers.add(handler);
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
    public static void emit(String key, JsonElement data){
        Core.socket.emit(key,new JSONObject(data));
    }
    public static void emit(String key, HashMap<String,String> data){
        Core.emit(key, new Gson().toJson(data));
    }

    public Instance getInstance(){
        return new Instance();
    }

    private static void connect(){
        URI uri = URI.create("https://socket.purecore.io/instances/");
        Core.socket = IO.socket(uri);
        Core.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Core.socket.emit("handshake",Core.key.getHash());
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
        });
        Core.socket.connect();
    }

    public static Call call(String endpoint){
        return new Call(endpoint, Core.key, Core.client);
    }

}
