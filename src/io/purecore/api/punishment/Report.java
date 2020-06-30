package io.purecore.api.punishment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.purecore.api.Core;
import io.purecore.api.instance.Instance;
import io.purecore.api.user.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report extends Core {

    public String uuid;
    public Player reporter;
    public boolean anon;
    public String content;
    public List<Player> upvoters;
    public List<Player> downvoters;
    public Player closedBy;
    public Date createdOn;
    public Date closeOn;
    public Punishment punishment;
    public Instance instance;
    public Player reported;

    public Core core;

    public Report(Core core, String uuid){
        super(core.getKey());
        this.core=core;
        // todo get from id
    }

    public Report(Core core, JsonObject json){
        super(core.getKey());
        this.core=core;

        this.uuid=json.get("uuid").getAsString();

        this.reporter=new Player(json.get("reporter").getAsJsonObject());

        this.anon=json.get("anon").getAsBoolean();

        this.content=json.get("content").getAsString();

        this.upvoters=new ArrayList<>();
        for (JsonElement upvoter:json.get("upvoters").getAsJsonArray()) {
            this.upvoters.add(new Player(upvoter.getAsJsonObject()));
        }

        this.downvoters=new ArrayList<>();
        for (JsonElement downvoter:json.get("downvoters").getAsJsonArray()) {
            this.downvoters.add(new Player(downvoter.getAsJsonObject()));
        }

        if(!json.get("closedBy").isJsonNull()){
            this.closedBy=new Player(json.get("closedBy").getAsJsonObject());
        } else {
            this.closedBy=null;
        }

        this.createdOn=new Date(json.get("createdOn").getAsLong());

        if(!json.get("punishment").isJsonNull()){
            this.punishment=new Punishment(new Core()); // todo
        } else {
            this.punishment=null;
        }

        this.instance=new Instance(core,json.get("instance").getAsJsonObject());

        this.reported=new Player(json.get("reported").getAsJsonObject());
    }

}
