package app.muvmedia.inova.muvmediaapp.usuario.dominio;

public class AppSession {
    private Sailor sailor;
    private Session session;

    public Sailor getSailor() {
        return sailor;
    }

    public void setSailor(Sailor sailor) {
        this.sailor = sailor;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
