package io.purecore.api.versioning;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class VersionCompatibility {

    public Game game;
    public ArrayList<GameSoftware> softwares;
    public String sinceVersion;
    public String untilVersion;

    public VersionCompatibility(JsonObject object){
        int game = object.get("game").getAsInt();
        switch (game){
            case 0:
                this.game=Game.Minecraft;
                break;
            case 1:
                this.game=Game.MinecraftBedrock;
                break;
            case 2:
                this.game=Game.SpaceEngineers;
                break;
            default:
                this.game=Game.Unknown;
        }
        this.softwares=new ArrayList<>();
        for (JsonElement software : object.get("softwares").getAsJsonArray()) {
            switch (software.getAsInt()){
                case 0:
                    this.softwares.add(GameSoftware.Spigot);
                    break;
                case 1:
                    this.softwares.add(GameSoftware.BungeeCord);
                    break;
                default:
                    this.softwares.add(GameSoftware.Unknown);
            }
        }
        this.sinceVersion=object.get("sinceVersion").getAsString();
        if(object.get("untilVersion").isJsonNull()){
            this.untilVersion=null;
        } else {
            this.untilVersion=object.get("untilVersion").getAsString();
        }
    }

    public Game getGame() {
        return game;
    }

    public String getSinceVersion() {
        return sinceVersion;
    }

    public String getUntilVersion() {
        return untilVersion;
    }

    public ArrayList<GameSoftware> getSoftwares() {
        return softwares;
    }
}
