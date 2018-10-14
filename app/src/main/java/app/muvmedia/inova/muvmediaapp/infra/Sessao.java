package app.muvmedia.inova.muvmediaapp.infra;

import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;

public class Sessao {
    private static Sessao instance = null;
    private Muver muver;
    private Sessao(){}

    public void setMuver(Muver muver){
        this.muver=muver;
    }
    public Muver getMuver(){
        return this.muver;
    }

    public static Sessao getInstance(){
        if(instance == null){
            instance = new Sessao();
        }
        return instance;
    }
}
