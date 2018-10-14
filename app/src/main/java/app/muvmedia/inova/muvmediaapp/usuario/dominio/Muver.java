package app.muvmedia.inova.muvmediaapp.usuario.dominio;


public class Muver {
    private String _id;
    private Usuario user;
    private String nome;
    private String cpf;
    private String dataNascimento;

    public Usuario getUsuario() {
        return user;
    }

    public void setUsuario(Usuario usuario) {
        this.user = usuario;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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
        return dataNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataNascimento = dataDeNascimento;
    }
}
