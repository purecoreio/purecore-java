package io.purecore.api.execution;

import com.google.gson.JsonObject;

public class SimplifiedExecution {

    public String id;
    public String string;
    public ExecutionContext context;
    public ExecutionTemplate template;

    public SimplifiedExecution(String id, String string, ExecutionContext context, ExecutionTemplate template){
        this.id=id;
        this.string=string;
        this.context=context;
        this.template=template;
    }

    public SimplifiedExecution(JsonObject object){
        this.id=object.get("id").getAsString();
        this.string=object.get("string").getAsString();
        this.template=null;
        if(!object.get("context").isJsonNull()){
            this.context=new ExecutionContext(object.get("context").getAsJsonObject());
        }
        this.template=null;
        if(!object.get("template").isJsonNull()){
            this.template=new ExecutionTemplate(object.get("template").getAsJsonObject());
        }
    }

    public ExecutionContext getContext() {
        return this.context;
    }

    public boolean hasTemplate(){
        return this.template!=null;
    }

    public ExecutionTemplate getTemplate() {
        return this.template;
    }

    public String getId() {
        return id;
    }
}
