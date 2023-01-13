package com.example.risingstar;

import java.util.ArrayList;
import java.util.Arrays;

public class Vertex {

    private Location RT, LT, RB, LB;

    public Vertex(Location rt, Location lt, Location rb, Location lb) {
        RT = rt;
        LT = lt;
        RB = rb;
        LB = lb;
    }

    public ArrayList<Location> getLocations() {
        return new ArrayList<>(Arrays.asList(RT, LT, RB, LB));
    }

    public Location getRT() { return RT; }

    public Location getLT() { return LT; }

    public Location getRB() { return RB; }

    public Location getLB() { return LB; }
}
