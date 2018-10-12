package app.muvmedia.inova.muvmediaapp.usuario.dominio;

public class Usuario {
    private long id;
    private String email;
    private String senha;

    public Muver getMuver() {
        return muver;
    }

    public void setMuver(Muver muver) {
        this.muver = muver;
    }

    private Muver muver;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
