package io.purecore.api.instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.purecore.api.Core;
import io.purecore.api.call.ApiException;
import io.purecore.api.call.Param;
import io.purecore.api.execution.SimplifiedExecution;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Instance {

    public String name;
    public String id;
    public boolean online;
    public int type;

    public Instance(){
        this.name=null;
        this.id=null;
        this.online=false;
        this.type=-1;
    }

    public Instance(JsonObject object){
        this.name=object.get("name").getAsString();
        this.id=object.get("id").getAsString();
        this.online=object.get("online").getAsBoolean();
        this.type=object.get("type").getAsInt();
    }

    public void connect(String platformId, String platformUsername, String ip) throws IOException, ApiException, JSONException {
        if(Core.isConnected()){
            HashMap<String, String> data = new HashMap<>();
            data.put(Param.PlatformId.getParam(),platformId);
            data.put(Param.PlatformName.getParam(), platformUsername);
            data.put(Param.Ip.getParam(), ip);
            Core.emit("playerLogin", data);
        } else {
            Core.call("/connections/open/")
                    .addParam(Param.PlatformId,platformId)
                    .addParam(Param.PlatformName,platformUsername)
                    .addParam(Param.Ip,ip)
                    .commit();
        }
    }

    public void disconnect(String platformId) throws IOException, ApiException, JSONException {
        if(Core.isConnected()){
            HashMap<String, String> data = new HashMap<>();
            data.put(Param.PlatformId.getParam(),platformId);
            Core.emit("playerLogout", data);
        } else {
            Core.call("/connections/open/")
                    .addParam(Param.PlatformId,platformId).commit();
        }
    }

    public ArrayList<SimplifiedExecution> getPendingExecutions() throws IOException, ApiException {
        JsonArray data = Core.call("/instance/get/executions/simplified/").commit().getAsJsonArray();
        ArrayList<SimplifiedExecution> executions = new ArrayList<>();
        for (JsonElement execution:data) {
            executions.add(new SimplifiedExecution(execution.getAsJsonObject()));
        }
        return executions;
    }

    public ArrayList<SimplifiedExecution> getPendingExecutions(SimplifiedExecution since) throws IOException, ApiException {
        JsonArray data = Core.call("/instance/get/executions/simplified/").addParam(Param.CommandExecution,since.getId()).commit().getAsJsonArray();
        ArrayList<SimplifiedExecution> executions = new ArrayList<>();
        for (JsonElement execution:data) {
            executions.add(new SimplifiedExecution(execution.getAsJsonObject()));
        }
        return executions;
    }

    public Instance update() throws IOException, ApiException {
        Instance instance = new Instance(Core.call("/instance/get/").commit().getAsJsonObject());
        this.id=instance.id;
        this.type=instance.type;
        this.online=instance.online;
        this.name=instance.name;
        return instance;
    }

}
