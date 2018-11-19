package app.muvmedia.inova.muvmediaapp.usuario.dominio;

public class AppSession {
    private Sailor sailor;
    private SessionApi sessionApi;

    public Sailor getSailor() {
        return sailor;
    }

    public void setSailor(Sailor sailor) {
        this.sailor = sailor;
    }

    public SessionApi getSessionApi() {
        return sessionApi;
    }

    public void setSessionApi(SessionApi sessionApi) {
        this.sessionApi = sessionApi;
    }
}
