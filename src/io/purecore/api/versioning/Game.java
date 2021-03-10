package io.purecore.api.versioning;

public enum Game {

    Unknown(-1),
    Minecraft(0),
    MinecraftBedrock(1),
    SpaceEngineers(2);

    private int val;

    Game(int val)
    {
        this.val = val;
    }

    public int getParam(){
        return this.val;
    }

}
