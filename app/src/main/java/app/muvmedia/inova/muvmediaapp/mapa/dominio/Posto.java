package app.muvmedia.inova.muvmediaapp.mapa.dominio;

public class Posto {
   public Endereco endereco;
   public Location location;

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
