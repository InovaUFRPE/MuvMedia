package app.muvmedia.inova.muvmediaapp.cupom.dominio;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class Toten implements Parcelable, Serializable {
    private String name;
    private app.muvmedia.inova.muvmediaapp.mapa.dominio.Location location;
    private List<String> campaigns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public app.muvmedia.inova.muvmediaapp.mapa.dominio.Location getLocation() {
        return location;
    }

    public void setLocation(app.muvmedia.inova.muvmediaapp.mapa.dominio.Location location) {
        this.location = location;
    }

    public List<String> getCampaigns() {
        return campaigns;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

//    public void setCampaigns(ArrayList<Campaign> campaigns) {
//        this.campaigns = campaigns;
//    }
//    //    private ArrayList<Location> location;
////    private ArrayList<Campaign> campaign;
////    private boolean deleted;
////    private String name;
////
////    public Toten(ArrayList<Location> location, ArrayList<Campaign> campaign, boolean deleted, String name){
////        this.location = location;
////        this.campaign = campaign;
////        this.deleted = deleted;
////        this.name = name;
////
////    }
////
////    public ArrayList<Location> getLocation(){
////        return location;
////    }

}
