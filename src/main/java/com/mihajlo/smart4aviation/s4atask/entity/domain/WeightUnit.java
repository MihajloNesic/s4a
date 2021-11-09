package com.mihajlo.smart4aviation.s4atask.entity.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WeightUnit {

    @JsonProperty("kg")
    KILOGRAM("kg"),

    @JsonProperty("lb")
    POUND("lb");

    private final String symbol;

    WeightUnit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static double convert(double weight, WeightUnit fromUnit, WeightUnit toUnit) {
        if (fromUnit.equals(KILOGRAM) && toUnit.equals(POUND)) {
            return weight * 2.20462262185;
        }
        else if (fromUnit.equals(POUND) && toUnit.equals(KILOGRAM)) {
            return weight * 0.45359237;
        }
        return weight;
    }
}
