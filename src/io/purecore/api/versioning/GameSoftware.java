package io.purecore.api.versioning;

public enum GameSoftware {

    Unknown(-1),
    Spigot(0),
    BungeeCord(1);

    private int val;

    GameSoftware(int val)
    {
        this.val = val;
    }

    public int getParam(){
        return this.val;
    }

}
