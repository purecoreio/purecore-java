package io.purecore.api.versioning;

public enum VersionStability {
    Unknown(-1),
    Release(0),
    Prerelease(1);

    private int val;

    VersionStability(int val)
    {
        this.val = val;
    }

    public int getParam(){
        return this.val;
    }
}
