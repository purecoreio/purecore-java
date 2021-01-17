package io.purecore.api.execution;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.purecore.api.instance.Instance;

import java.util.ArrayList;
import java.util.List;

public class ExecutionTemplate {

    public String id;
    public String string;
    public boolean requireOnline;
    public List<Instance> instances;
    public float delay;

    public ExecutionTemplate(JsonObject object){
        this.id=object.get("id").getAsString();
        this.string=object.get("string").getAsString();
        this.requireOnline=object.get("requireOnline").getAsBoolean();
        this.instances=new ArrayList<>();
        for (JsonElement instance : object.get("instances").getAsJsonArray()) {
            this.instances.add(new Instance(instance.getAsJsonObject()));
        }
        this.delay=object.get("delay").getAsFloat();
    }

    public boolean requiresOnline() {
        return requireOnline;
    }

    public float getDelay() {
        return delay;
    }
}
