package app.muvmedia.inova.muvmediaapp.cupom.dominio;

public class Campaign {
    private String name;
    private String description;
    private String url;
    private String initDate;
    private String endDate;

    public Campaign(String name, String description, String initDate) {
        this.name = name;
        this.description = description;
        this.initDate = initDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public String getUrl() {
        return url;
    }

    public String getInitDate() {
        return initDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
