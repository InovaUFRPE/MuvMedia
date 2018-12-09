package app.muvmedia.inova.muvmediaapp.usuario.servico;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import app.muvmedia.inova.muvmediaapp.cupom.dominio.Campaign;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Sailor;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;

public class ServicoHttpMuver {

    private static String muverUri = "http://muvmedia-api.herokuapp.com/muvers";


    public static String getCpfMuver(String cpf) {
        String url = "http://muvmedia-api.herokuapp.com/public/verify/muver?cpf=" + cpf;
        String resposta = getMuverCpf(url);
        return resposta;
    }

    public static String getMuverCpf(String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        String answer;
        try {
            HttpResponse resposta = httpClient.execute(httpGet);
            int status = resposta.getStatusLine().getStatusCode();
            if (status == 200) {
//                CPF encontrado
                answer = EntityUtils.toString(resposta.getEntity());
                Log.i("Script", "ANSWER: "+ answer);
            } else if (status == 404) {
                Log.i("Script", "CPF não encontrado");
                return "CPF não encontrado";
//                throw new Exception("404. Email não encontrado");
            } else {
                Log.i("Script", "400. Erro inesperado");
                throw new Exception("Erro inesperado");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

    public static String getEmailUser(String email) {
        String url = "http://muvmedia-api.herokuapp.com/public/verify/user?email=" + email;
        String resposta = getUserEmail(url);
        return resposta;
    }

    public static String getUserEmail(String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        String answer;
        try {
            HttpResponse resposta = httpClient.execute(httpGet);
            int status = resposta.getStatusLine().getStatusCode();
            if (status == 200) {
//                Email encontrado
                answer = EntityUtils.toString(resposta.getEntity());
                Log.i("Script", "ANSWER: "+ answer);
            } else if (status == 404) {
                Log.i("Script", "Email não encontrado");
                return "Email não encontrado";
//                throw new Exception("404. Email não encontrado");
            } else {
                Log.i("Script", "400. Erro inesperado");
                throw new Exception("Erro inesperado");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }


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

    public Sailor updateSailor(Sailor sailor) throws Exception {
        String idSailor = sailor.get_id();
        String userUrl = "http://capitao-api.herokuapp.com/sailors/" + idSailor;
        Gson gson = new Gson();
        String userString = gson.toJson(sailor);
        String resposta = null;
        try {
            resposta = this.put(userUrl, userString);
        } catch (Exception e){
            e.printStackTrace();
//            throw new Exception("Erro Email");
        }
        if (resposta != null) {
            sailor = gson.fromJson(resposta, Sailor.class);
        }
        return sailor;

    }

    public Campaign getCampanha(String idTotem){
        String camapanhaString = "http://capitao-api.herokuapp.com/totens/campaign/" + idTotem;
        String resposta = null;
        try {
            resposta = get(camapanhaString);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(resposta!=null){
            Gson gson = new Gson();
            Campaign campaign = gson.fromJson(resposta, Campaign.class);
            return campaign;
        }else {
            return null;
        }
    }

    public Sailor checkSailor(String idSailor){
        String camapanhaString = "http://capitao-api.herokuapp.com/sailors/" + idSailor;
        String resposta = null;
        try {
            resposta = get(camapanhaString);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(resposta!=null){
            Gson gson = new Gson();
            Sailor sailor = gson.fromJson(resposta, Sailor.class);
            return sailor;
        }else {
            return null;
        }
    }


//    public Muver updateMuver(Muver muver) throws Exception {
////        String idMuver = muver.getId();
////        String muverUrl = muverUri + "/" + idMuver;
//        Gson gson = new Gson();
//        String muverString = gson.toJson(muver);
//        String resposta = null;
//        try {
//            resposta = this.put(muverUri, muverString);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
////        String resposta = this.put(userUrl, userString);
//        if (resposta != null) {
//            muver = gson.fromJson(resposta, Muver.class);
//        }
//        Log.i("Script", "Resposta: "+ resposta);
//        return muver;
//    }

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
                Sessao.instance.setResposta("200");
            } else if (status == 404) {
                throw new Exception("Não temos campanhas para você hoje, volte depois");
            } else if (status == 206){
                answer = EntityUtils.toString(resposta.getEntity());
                Sessao.instance.setResposta("206");
            } else {
                Sessao.instance.setResposta("1000");
                throw new Exception("Erro");
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
        String answer = null;
        String Erro = null;
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPut.getRequestLine();
            httpPut.setEntity(stringEntity);
            HttpResponse resposta = httpClient.execute(httpPut);
            
            int status = resposta.getStatusLine().getStatusCode();
            if (status == 200) {
                answer = EntityUtils.toString(resposta.getEntity());
                Sessao.instance.setResposta("Sucess");
                Log.i("Put", "ANSWER: "+ answer);
//            } else if (status == 404) {
//                Erro = "Sailor não encontrado";
//                Log.i("Put", "Sailor não encontrado, Status - " + status);
//                throw new Exception(Erro);
//            } else if(status == 400) {
//                Erro = "Email em uso";
//                Log.i("Put", "Email em uso, Status - " + status);
//                throw new Exception(Erro);
            } else {
                Sessao.instance.setResposta("Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

}
