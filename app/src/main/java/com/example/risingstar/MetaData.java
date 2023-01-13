package com.example.risingstar;

import java.io.Serializable;

public class MetaData implements Serializable {
    private Location cameraLocation;
    private Vertex mVertex;

    public MetaData(Location location, Vertex vertex) {
        cameraLocation = location;
        mVertex = vertex;
    }

    public Location getCameraLocation() { return cameraLocation; }

    public Vertex getVertex() { return mVertex; }
}
