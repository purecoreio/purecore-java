package io.purecore.api.call;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.purecore.api.key.Key;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Call {

    private final String base = "https://api.purecore.io/rest/3";

    private String hash;
    private String endpoint;
    private OkHttpClient client;
    private HashMap<String,String> params;

    public Call(String path, Key key, OkHttpClient client){
        this.endpoint=path;
        this.hash=key.getHash();
        this.client=client;
    }

    public Call addParam(Param param, String value){
        params.put(param.getParam(),value);
        return this;
    }

    public JsonElement commit() throws IOException, ApiException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(this.base + this.endpoint)).newBuilder();
        urlBuilder.addQueryParameter(Param.Key.getParam(), this.hash);
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Response response = this.client.newCall(request).execute();
        if (response.body() != null) {
            JsonElement body = new Gson().fromJson(response.body().string(), JsonElement.class);
            if(response.code()==200){
                return body;
            } else {
                if(body.getAsJsonObject().has("error")){
                    throw new ApiException(body.getAsJsonObject().get("error").getAsString());
                } else {
                    throw new ApiException("Unknown error");
                }
            }
        } else {
            throw new ApiException("Empty body");
        }
    }


}
