package app.muvmedia.inova.muvmediaapp.cupom.dominio;


import android.location.Location;

import java.util.ArrayList;
import java.util.stream.Stream;


public class Toten {
    private ArrayList<Location> location;
    private ArrayList<Campaign> campaign;
    private boolean deleted;
    private String name;

    public Toten(ArrayList<Location> location, ArrayList<Campaign> campaign, boolean deleted, String name){
        this.location = location;
        this.campaign = campaign;
        this.deleted = deleted;
        this.name = name;

    }

    public ArrayList<Location> getLocation(){
        return location;
    }

}
