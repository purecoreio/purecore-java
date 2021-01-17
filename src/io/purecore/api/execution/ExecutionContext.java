package io.purecore.api.execution;

import com.google.gson.JsonObject;

public class ExecutionContext {

    public String username;
    public String id;
    public String packageName;
    public float quantity;
    public float quantityDifference;

    public ExecutionContext(JsonObject object){
        this.username=object.get("username").getAsString();
        this.id=object.get("id").getAsString();
        this.packageName=object.get("packageName").getAsString();
        if(object.get("quantity").isJsonNull()){
            this.quantity=object.get("quantity").getAsFloat();
        }
        if(object.get("quantityDifference").isJsonNull()){
            this.quantityDifference=object.get("quantityDifference").getAsFloat();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getQuantityDifference() {
        return quantityDifference;
    }

    public String getPackageName() {
        return packageName;
    }
}
