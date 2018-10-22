package app.muvmedia.inova.muvmediaapp.usuario.servico;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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

    public Muver addMuver(Muver muver) {
        Gson gson = new Gson();
        String muverString = gson.toJson(muver);
        String resposta = this.post(muverUri, muverString);
        if (resposta != null) {
            muver = gson.fromJson(resposta, Muver.class);
        }
        return muver;
    }

    public Usuario updateUsuario(Usuario usuario) {
        String idUsuario = usuario.get_id();
        String userUrl = "http://muvmedia-api.herokuapp.com/users/" + idUsuario;
        Gson gson = new Gson();
        String userString = gson.toJson(usuario);
        String resposta = this.put(userUrl, userString);
        if (resposta != null) {
            usuario = gson.fromJson(resposta, Usuario.class);
        }
        return usuario;
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

    public static String post(String url, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.addHeader("Authorization", "Bearer "+ Sessao.instance.getSession().getToken());
        String answer;
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);
            HttpResponse resposta = httpClient.execute(httpPost);
            int status = resposta.getStatusLine().getStatusCode();
            if (status == 201) {
                answer = EntityUtils.toString(resposta.getEntity());
                Log.i("Script", "ANSWER: "+ answer);
            } else if (status == 409) {
                throw new Exception("Muver já existe");
            } else {
                throw new Exception("Erro inesperado");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

    private static String put(String url, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Content-type", "application/json");
        httpPut.addHeader("Authorization", "Bearer "+ Sessao.instance.getSession().getToken());
        String answer;
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPut.getRequestLine();
            httpPut.setEntity(stringEntity);
            HttpResponse resposta = httpClient.execute(httpPut);
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
