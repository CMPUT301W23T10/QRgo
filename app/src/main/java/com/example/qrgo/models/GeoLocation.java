package com.example.qrgo.models;

import java.util.Date;

/**

 Represents the geographic location of a point on the Earth's surface

 specified by latitude and longitude coordinates.
 */
public class GeoLocation {
    protected Double geoLocationLat;
    protected Double geoLocationLon;

    /**
     Gets the latitude coordinate of the geographic location.
     @return The latitude coordinate of the geographic location.
     */
    public Double getGeoLocationLat() {
        return geoLocationLat;
    }

    /**
     Sets the latitude coordinate of the geographic location.
     @param geoLocationLat The latitude coordinate of the geographic location.
     */
    public void setGeoLocationLat(Double geoLocationLat) {
        this.geoLocationLat = geoLocationLat;
    }

    /**
     Gets the longitude coordinate of the geographic location.
     @return The longitude coordinate of the geographic location.
     */
    public Double getGeoLocationLon() {
        return geoLocationLon;
    }

    /**
     Sets the longitude coordinate of the geographic location.
     @param geoLocationLon The longitude coordinate of the geographic location.
     */
    public void setGeoLocationLon(Double geoLocationLon) {
        this.geoLocationLon = geoLocationLon;
    }
}
