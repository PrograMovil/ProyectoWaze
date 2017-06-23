/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Locale;

/**
 *
 * @author SheshoVega
 */
public class LatLng implements UrlValue {
    /**
     * The latitude of this location.
     */
    public double lat;

    /**
     * The longitude of this location.
     */
    public double lng;

    /**
     * Construct a location with a latitude longitude pair.
     */
    public LatLng(double lat, double lng) {
      this.lat = lat;
      this.lng = lng;
    }

    @Override
    public String toString() {
      return toUrlValue();
    }

    @Override
    public String toUrlValue() {
      // Enforce Locale to English for double to string conversion
      return String.format(Locale.ENGLISH, "%.8f,%.8f", lat, lng);
    }
}
