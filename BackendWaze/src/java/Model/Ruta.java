/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;

/**
 *
 * @author SheshoVega
 */
public class Ruta {
    public Distancia distance;
    public Duracion duration;
    String endAddress;
    LatLng endLocation;
    String startAddress;
    LatLng startLocation;

    List<LatLng> points;

    public Ruta() {
    }

    public Ruta(Distancia d, Duracion d2, String endAddress, LatLng endLocation, String startAddress, LatLng startLocation, List<LatLng> points) {
        this.distance = d;
        this.duration = d2;
        this.endAddress = endAddress;
        this.endLocation = endLocation;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
        this.points = points;
    }

    
    
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

    public Distancia getDistance() { return distance; }

    public void setDistance(Distancia distance) { this.distance = distance; }

    public Duracion getDuration() { return duration; }

    public void setDuration(Duracion duration) { this.duration = duration; }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }
    
    @Override
    public String toString() {
        return "Ruta : {" + "distance : " + distance.toString() + ", duration : " + duration.toString() + ", endAddress : " + endAddress + ", endLocation : " + endLocation.toString() + ", startAddress : " + startAddress + ", startLocation : " + startLocation.toString() + ", points : " + points.toString() + '}';
    }
    
}
