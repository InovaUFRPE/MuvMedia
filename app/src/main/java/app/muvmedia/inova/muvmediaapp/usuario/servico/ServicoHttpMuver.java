package app.muvmedia.inova.muvmediaapp.usuario.servico;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.SessionApi;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;

public class ServicoHttpMuver {

    private static String muverUri = "http://muvmedia-api.herokuapp.com/muvers";

    public Muver getMuver(String id) {
        String url = muverUri + "/" + id;
        String resposta = this.get(url);
        Muver muver = null;
        if (resposta != null) {
            Gson gson = new Gson();
            muver = gson.fromJson(resposta, Muver.class);
        }
        return muver;
    }

    public Muver getMuverByUser(Usuario usuario) {
        String userId = usuario.get_id();
        String url = muverUri + "/user/" + userId;
        String resposta = this.get(url);
        Muver muver = null;
        if (resposta != null) {
            Gson gson = new Gson();
            muver = gson.fromJson(resposta, Muver.class);
        }
        return muver;
    }

    private static String get(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        httpGet.addHeader("Authorization", "Bearer "+ Sessao.instance.getSession().getToken());
        String answer;
        try {
            HttpResponse resposta = httpClient.execute(httpGet);
            int status = resposta.getStatusLine().getStatusCode();
            if (status == 200) {
                answer = EntityUtils.toString(resposta.getEntity());
                Log.i("Script", "ANSWER: "+ answer);
            } else if (status == 404) {
                throw new Exception("Muver não encontrado");
            } else {
                throw new Exception("Erro inesperado");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

}
