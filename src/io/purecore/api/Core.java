package io.purecore.api;

import io.purecore.api.exception.ApiException;
import io.purecore.api.exception.CallException;
import io.purecore.api.instance.Instance;
import io.purecore.api.key.Key;

import java.io.IOException;

public class Core {

    public enum Mode {
        UNKNOWN,
        LISTENER,
        TALKER
    }

    private Mode mode;
    private String key;

    public Core(String key, Mode mode) {
        this.key=key;
        this.mode=mode;
    }

    public Core(String key) {
        this.key=key;
        this.mode=Mode.UNKNOWN;
    }

    public Core() {
        this.mode=Mode.UNKNOWN;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public Core(Key key) {
        this.key=key.getHash();
    }

    public String getKey() {
        return key;
    }

    public Key getKeyLegacy(){

        return new Key(this.key);

    }

    public Instance getInstance() throws ApiException, IOException, CallException {
        return new Instance(this);
    }
}
