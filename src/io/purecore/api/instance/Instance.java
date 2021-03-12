package io.purecore.api.instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.purecore.api.Core;
import io.purecore.api.call.ApiException;
import io.purecore.api.call.Call;
import io.purecore.api.call.Param;
import io.purecore.api.execution.SimplifiedExecution;
import io.purecore.api.punishment.PunishmentType;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

    public void punish(String punishmentSystem, PunishmentType type, String moderatorPlatformId, String subjectPlatformId, String reason, String localId, Date until) throws IOException, ApiException, JSONException {
        if(Core.isConnected()){
            HashMap<String, Object> data = new HashMap<>();
            data.put(Param.Instance.getParam(),this.id);
            data.put(Param.PunishmentSystem.getParam(),punishmentSystem);
            data.put(Param.PunishmentType.getParam(), type.getVal());
            data.put(Param.SubjectPlatformId.getParam(), subjectPlatformId);
            if(reason!=null) data.put(Param.Reason.getParam(), reason);
            if(localId!=null) data.put(Param.LocalId.getParam(), localId);
            if(until!=null) data.put(Param.Until.getParam(), until.getTime());
            if(moderatorPlatformId!=null) data.put(Param.PlatformId.getParam(), moderatorPlatformId);
            Core.emit("punish", data);
        } else {
            Call call = Core.call("/punishment/create/")
                    .addParam(Param.Instance,this.id)
                    .addParam(Param.PunishmentSystem,punishmentSystem)
                    .addParam(Param.PunishmentType,String.valueOf(type.getVal()))
                    .addParam(Param.SubjectPlatformId,subjectPlatformId);
            if(reason!=null) call.addParam(Param.Reason,reason);
            if(localId!=null) call.addParam(Param.LocalId,localId);
            if(until!=null) call.addParam(Param.Until,String.valueOf(until.getTime()));
            if(moderatorPlatformId!=null) call.addParam(Param.PlatformId,moderatorPlatformId);
            call.commit();
        }
    }

    public void unpunish(String punishmentSystem, PunishmentType punishmentType, String subjectPlatformId, String moderatorPlatformId, String localId) throws IOException, ApiException, JSONException {
        if(Core.isConnected()){
            HashMap<String, Object> data = new HashMap<>();
            data.put(Param.Instance.getParam(),this.id);
            data.put(Param.PunishmentSystem.getParam(),punishmentSystem);
            data.put(Param.PunishmentType.getParam(), punishmentType.getVal());
            data.put(Param.SubjectPlatformId.getParam(), subjectPlatformId);
            if(localId!=null) data.put(Param.LocalId.getParam(), localId);
            if(moderatorPlatformId!=null) data.put(Param.PlatformId.getParam(), moderatorPlatformId);
            Core.emit("unpunish", data);
        } else {
            Call call = Core.call("/punishment/close/")
                    .addParam(Param.Instance,this.id)
                    .addParam(Param.PunishmentSystem,punishmentSystem)
                    .addParam(Param.PunishmentType,String.valueOf(punishmentType.getVal()))
                    .addParam(Param.SubjectPlatformId,subjectPlatformId);
            if(localId!=null) call.addParam(Param.LocalId,localId);
            if(moderatorPlatformId!=null) call.addParam(Param.PlatformId,moderatorPlatformId);
            call.commit();
        }
    }

    public void connect(String platformId, String platformUsername, String ip) throws IOException, ApiException, JSONException {
        if(Core.isConnected()){
            HashMap<String, Object> data = new HashMap<>();
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
            HashMap<String, Object> data = new HashMap<>();
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
