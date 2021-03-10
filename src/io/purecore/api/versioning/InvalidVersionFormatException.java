package io.purecore.api.versioning;

public class InvalidVersionFormatException extends Exception {
    InvalidVersionFormatException(String s){
        super(s);
    }
}
