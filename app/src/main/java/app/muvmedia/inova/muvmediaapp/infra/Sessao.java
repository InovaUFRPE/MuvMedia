package app.muvmedia.inova.muvmediaapp.infra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;

public class Sessao {
    public static final Sessao instance = new Sessao();
    private final Map<String, Object> values = new HashMap<>();

    private void setValor(String chave, Object valor) {
        values.put(chave, valor);
    }

    public void setResposta(String resposta) {
        setValor("sessao.resposta", resposta);
    }

    public String getResposta(){
        return (String) values.get("sessao.resposta");
    }

    public void setToken(Object valor) {
        values.put("sessao.token", valor);
    }

    public String getToken(){
        return (String) values.get("sessao.token");
    }

    public void setMuver(Object valor){
        values.put("sessao.muver", valor);
    }

    public Muver getMuver(){
        return (Muver) values.get("sessao.muver");
    }
}

