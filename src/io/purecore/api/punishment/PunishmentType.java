package io.purecore.api.punishment;

public enum PunishmentType {

    Kick(0),
    Ban(1),
    Warn(2),
    Mute(3),
    BanIP(4);

    private int val;
    PunishmentType(int val){
        this.val=val;
    }

    public int getVal() {
        return val;
    }
}
