package com.example.risingstar;

import java.io.Serializable;

public class DataInfo implements Serializable {

    private String fileName;
    private Location RT, LT, RB, LB;

    public DataInfo(String name, Location rt, Location lt, Location rb, Location lb) {
        fileName = name;
        RT = rt;
        LT = lt;
        RB = rb;
        LB = lb;
    }

    public String getFileName() { return fileName; }

    public Location getRT() { return RT; }

    public Location getLT() { return LT; }

    public Location getRB() { return RB; }

    public Location getLB() { return LB; }
}
