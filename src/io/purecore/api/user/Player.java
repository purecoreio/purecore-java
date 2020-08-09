package io.purecore.api.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.purecore.api.Core;
import io.purecore.api.exception.ApiException;
import io.purecore.api.exception.CallException;
import io.purecore.api.punishment.Offence;
import io.purecore.api.punishment.Punishment;
import io.purecore.api.punishment.Report;
import io.purecore.api.request.ObjectRequest;
import io.purecore.api.voting.VotingSite;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class Player extends Core {

    private Core core;
    private String coreid;
    private String username;
    private UUID uuid;
    private boolean verified;

    public Player(Core core, String username, UUID uuid, boolean verified)
    {
        super(core.getKey());
        this.core=core;
        this.username = username;
        this.uuid = uuid;
        this.verified = verified;

    }

    public Player(Core core, String username) throws ApiException, IOException, CallException, JSONException {

        super(core.getKey());
        this.core=core;
        this.username = username;

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", username);

        JsonObject playerResult = new ObjectRequest(core, ObjectRequest.Call.PLAYER_FROM_USERNAME, params).getResult();

        this.coreid = playerResult.get("coreid").getAsString();
        this.uuid = UUID.fromString(playerResult.get("uuid").getAsString());
        this.verified = playerResult.get("verified").getAsBoolean();

    }

    public Player(JsonObject json){
        this.coreid = json.get("coreid").getAsString();
        this.uuid = UUID.fromString(json.get("uuid").getAsString());
        this.verified = json.get("verified").getAsBoolean();
        this.username = json.get("username").getAsString();
    }

    public Punishment punish(Player player, List<Offence> offenceList) throws ApiException, IOException, CallException, JSONException {
        return new Punishment(this.core, player, this, offenceList);
    }

    public boolean vote(VotingSite site) throws ApiException, IOException, CallException, JSONException {

        LinkedHashMap<String,String> params = new LinkedHashMap<>();
        if(site.uuid==null){
            params.put("siteName",site.technicalName);
        } else {
            params.put("site",site.uuid);
        }

        params.put("player",this.coreid);
        ObjectRequest request = new ObjectRequest(this.core, ObjectRequest.Call.VOTE_WITH_SITE, params);
        request.getResult();
        return true;
    }

    public Report report(Player player, List<Offence> offenceList, String content, boolean anon) throws ApiException, IOException, CallException, JSONException {

        LinkedHashMap<String,String> params = new LinkedHashMap<>();
        params.put("player",player.coreid);
        params.put("reporter",this.coreid);
        params.put("content",content);
        String anonStr = "false";
        if(anon){
            anonStr="true";
        }
        params.put("anon",anonStr);


        List<String> offenceIdList = new ArrayList<>();
        for (Offence offence:offenceList) {
            if(!offenceIdList.contains(offence.getId())){
                offenceIdList.add(offence.getId());
            }
        }

        Gson gson = new Gson();
        params.put("offences", gson.toJson(offenceIdList));
        ObjectRequest request = new ObjectRequest(this.core, ObjectRequest.Call.REPORT, params);
        return new Report(this.core, request.getResult());
    }

    public String getCoreid() {
        return coreid;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUUID(){
        return uuid;
    }

}
