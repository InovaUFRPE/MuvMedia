package app.muvmedia.inova.muvmediaapp.usuario.dominio;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import app.muvmedia.inova.muvmediaapp.cupom.dominio.Campaign;

public class Sailor {
    private String _id;
    private Usuario user;
    private String name;
    private String birthday;
    private List<Campaign> campaigns;
    private int level;
    private int xp;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public List<Campaign> getCampaigns() {
        return this.campaigns;
    }

    public void setCampaigns(Campaign campanha) {
        if(this.campaigns == null){
            this.campaigns = new List<Campaign>() {
                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(Object o) {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<Campaign> iterator() {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(@NonNull T[] ts) {
                    return null;
                }

                @Override
                public boolean add(Campaign campaign) {
                    return false;
                }

                @Override
                public boolean remove(Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(@NonNull Collection<?> collection) {
                    return false;
                }

                @Override
                public boolean addAll(@NonNull Collection<? extends Campaign> collection) {
                    return false;
                }

                @Override
                public boolean addAll(int i, @NonNull Collection<? extends Campaign> collection) {
                    return false;
                }

                @Override
                public boolean removeAll(@NonNull Collection<?> collection) {
                    return false;
                }

                @Override
                public boolean retainAll(@NonNull Collection<?> collection) {
                    return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public boolean equals(Object o) {
                    return false;
                }

                @Override
                public int hashCode() {
                    return 0;
                }

                @Override
                public Campaign get(int i) {
                    return null;
                }

                @Override
                public Campaign set(int i, Campaign campaign) {
                    return null;
                }

                @Override
                public void add(int i, Campaign campaign) {

                }

                @Override
                public Campaign remove(int i) {
                    return null;
                }

                @Override
                public int indexOf(Object o) {
                    return 0;
                }

                @Override
                public int lastIndexOf(Object o) {
                    return 0;
                }

                @NonNull
                @Override
                public ListIterator<Campaign> listIterator() {
                    return null;
                }

                @NonNull
                @Override
                public ListIterator<Campaign> listIterator(int i) {
                    return null;
                }

                @NonNull
                @Override
                public List<Campaign> subList(int i, int i1) {
                    return null;
                }
            };
        }
        this.campaigns.add(campanha);
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
