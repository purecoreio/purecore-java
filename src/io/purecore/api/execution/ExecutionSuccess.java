package io.purecore.api.execution;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.purecore.api.Core;
import io.purecore.api.call.ApiException;
import io.purecore.api.call.Param;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExecutionSuccess {

    public List<SimplifiedExecution> success;
    public List<SimplifiedExecution> fail;

    public ExecutionSuccess(JsonObject object){
        this.success=new ArrayList<>();
        for (JsonElement element: object.get("success").getAsJsonArray()){
            this.success.add(new SimplifiedExecution(element.getAsJsonObject()));
        }

        this.fail=new ArrayList<>();
        for (JsonElement element: object.get("fail").getAsJsonArray()){
            this.fail.add(new SimplifiedExecution(element.getAsJsonObject()));
        }
    }

    public ExecutionSuccess(List<SimplifiedExecution> executions) throws IOException, ApiException {
        List<String> ids = new ArrayList<>();
        for (SimplifiedExecution execution:executions){
            ids.add(execution.getId());
        }
        String jsonIds = new Gson().toJson(ids);
        JsonObject response = Core.call("/execution/batch/mark/").addParam(Param.SimplifiedCommandExecutions,jsonIds).commit().getAsJsonObject();
        this.success=new ArrayList<>();
        for (JsonElement element: response.get("success").getAsJsonArray()){
            this.success.add(new SimplifiedExecution(element.getAsJsonObject()));
        }

        this.fail=new ArrayList<>();
        for (JsonElement element: response.get("fail").getAsJsonArray()){
            this.fail.add(new SimplifiedExecution(element.getAsJsonObject()));
        }
    }

}
