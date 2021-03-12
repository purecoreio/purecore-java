package io.purecore.api.punishment;

import io.purecore.api.instance.Instance;
import io.purecore.api.user.PlatformProfile;

import java.util.ArrayList;
import java.util.Date;

public class Punishment {

    public String id;
    public PunishmentSystem system;
    public PunishmentType type;
    public String localId;
    public PlatformProfile staff;
    public ArrayList<PlatformProfile> affected;
    public String reason;
    public Instance executedOn;
    public Date since;
    public Date until;
    public Date closed;
    public boolean hasClosedAppeals;
    public boolean hasOpenedAppeals;

}
