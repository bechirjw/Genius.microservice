package com.genuis.ressources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatutRessource {
    Gratuit("Gratuit"),
    Payant("Payant");

    private final String value;

    StatutRessource(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StatutRessource fromValue(String value) {
        for (StatutRessource s : values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}

