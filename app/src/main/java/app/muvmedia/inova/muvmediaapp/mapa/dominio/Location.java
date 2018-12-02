package app.muvmedia.inova.muvmediaapp.mapa.dominio;

import java.util.ArrayList;

public class Location {
    private String type;
    private ArrayList<Double> coordinates;

    public String getType() {
        return type;
    }

    public double getLatitude(){
        return getCoordinates().get(0);
    }

    public double getLongitude(){
        return getCoordinates().get(1);
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    //    public double lng;
//    public double lat;
//
//    public double getLng() {
//        return lng;
//    }
//
//    public void setLng(double lng) {
//        this.lng = lng;
//    }
//
//    public double getLat() {
//        return lat;
//    }
//
//    public void setLat(double lat) {
//        this.lat = lat;
//    }
}
