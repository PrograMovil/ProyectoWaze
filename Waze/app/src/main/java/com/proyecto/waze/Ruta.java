package com.proyecto.waze;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by SheshoVega on 27/05/2017.
 */

public class Ruta {
//    public Distance distance;
//    public Duration duration;
    String endAddress;
    LatLng endLocation;
    String startAddress;
    LatLng startLocation;

    List<LatLng> points;

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }
}