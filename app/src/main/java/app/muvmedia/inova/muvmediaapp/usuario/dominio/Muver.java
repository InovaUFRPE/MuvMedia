package app.muvmedia.inova.muvmediaapp.usuario.dominio;


public class Muver {
    private long id;
    private Usuario user;
    private String nome;
    private String cpf;
    private String dataDeNascimento;

    public Usuario getUsuario() {
        return user;
    }

    public void setUsuario(Usuario usuario) {
        this.user = usuario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
}
