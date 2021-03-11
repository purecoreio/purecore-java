package io.purecore.api.versioning;

import com.google.gson.JsonObject;

public class Version {

    public String id;
    public String repo;
    public long releaseDate;
    public VersionCompatibility compatibility;
    public String tag;
    public String fileName;
    public VersionStability stability;

    public Version(JsonObject object){
        this.id=object.get("id").getAsString();
        this.repo=object.get("repo").getAsString();
        this.releaseDate=object.get("releaseDate").getAsLong();
        this.compatibility=new VersionCompatibility(object.get("compatibility").getAsJsonObject());
        this.tag=object.get("tag").getAsString();
        this.fileName=object.get("fileName").getAsString();
        int stability = object.get("stability").getAsInt();
        switch (stability){
            case 0:
                this.stability=VersionStability.Release;
                break;
            case 1:
                this.stability=VersionStability.Prerelease;
                break;
            default:
                this.stability=VersionStability.Unknown;
        }
    }

    public static float getRepresentativePart(String version) throws InvalidVersionFormatException {
        String[] split = version.split("\\.");
        if(split.length<=1) throw new InvalidVersionFormatException("Missing subversion");
        int valid = split[0].length();
        String versionNoDot = version.replaceAll("\\.","");
        int inv = versionNoDot.length() - valid;
        if(inv <= 0)  throw new InvalidVersionFormatException("Impossible to get representative version");
        int noDotInt = Integer.parseInt(versionNoDot);
        return (float) noDotInt / (float) Math.pow(10,inv);
    }

    public boolean isCompatible(Game game, GameSoftware gameSoftware, String gameVersion) throws InvalidVersionFormatException {
        if(game==this.compatibility.getGame() && this.compatibility.getSoftwares().contains(gameSoftware)){
            if(Version.getRepresentativePart(this.compatibility.getSinceVersion())<=Version.getRepresentativePart(gameVersion)){
                if(this.compatibility.getUntilVersion()==null){
                    return true;
                } else return this.compatibility.getUntilVersion() != null && Version.getRepresentativePart(this.compatibility.getUntilVersion()) >= Version.getRepresentativePart(gameVersion);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isCompatible(Game game, GameSoftware gameSoftware) throws InvalidVersionFormatException {
        if(game==this.compatibility.getGame() && this.compatibility.getSoftwares().contains(gameSoftware)){
            return true;
        } else {
            return false;
        }
    }

    public String getDownloadURL(){
        return "https://github.com/purecoreio/" + this.getTag() + "/releases/download/" + this.getTag() + "/" + this.getFileName();
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public String getId() {
        return id;
    }

    public String getRepo() {
        return repo;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTag() {
        return tag;
    }

    public VersionCompatibility getCompatibility() {
        return compatibility;
    }

    public VersionStability getStability() {
        return stability;
    }
}
