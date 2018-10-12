package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CriarConta1Activity extends AppCompatActivity {
    private EditText campoEmail, campoSenha, campoRepetirSenha;
    private Button botaoProximo;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta1);
        irCadastro2();
    }

    private void irCadastro2(){
        this.botaoProximo = findViewById(R.id.button);
        this.campoEmail = findViewById(R.id.editText);
        this.campoSenha = findViewById(R.id.editText2);
        this.campoRepetirSenha = findViewById(R.id.editText3);
        botaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos()){
//                    try {
//                        isOnline();
//                    } catch (JSONException e) {
//                        Toast.makeText(getApplicationContext(), "rabujhoo", Toast.LENGTH_LONG).show();
                    }
//                    CriarConta1Activity.GetJsonTask newsTask = new GetJsonTask("http://localhost:5000/public/register/user", user);
////                       new GetJsonTask("http://localhost:5000/public/register/user", user);
//                        newsTask.execute();

                    Intent intent = new Intent(getApplicationContext(), CriarConta2Activity.class);
                    startActivity(intent);
                }

//            }
        });
    }

    private JSONObject setarUsuario(String email, String senha) throws JSONException {
        Usuario usuario = new Usuario();
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setNivel(1);
        Gson gson = new Gson();
        String user = gson.toJson(usuario);
        JSONObject JsonObjRecv = new JSONObject(user);
        return JsonObjRecv;

    }

//    private void isOnline() throws JSONException {
//        if(ServicoDownload.isNetworkAvailable(getApplicationContext()))
//        {
//            JSONObject user = setarUsuario(campoEmail.getText().toString().trim(), campoSenha.getText().toString().trim());
//            GetJsonTask newsTask = new GetJsonTask("http://localhost:5000/public/register/user", user);
//            newsTask.execute();
//        }else{
//            Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
//        }
//    }


//    public class GetJsonTask extends AsyncTask<String, Void, String >{
//        private String URL;
//        private JSONObject jsonObjSend;
//
//        public GetJsonTask(String URL, JSONObject jsonObjSend) {
//            this.URL = URL;
//            this.jsonObjSend = jsonObjSend;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String resultString = null;
//            try {
//                DefaultHttpClient httpclient = new DefaultHttpClient();
//                HttpPost httpPostRequest = new HttpPost(URL);
//
//                StringEntity se;
//                se = new StringEntity(jsonObjSend.toString());
//
//                // Set HTTP parameters
//                httpPostRequest.setEntity(se);
//                httpPostRequest.setHeader("Accept", "application/json");
//                httpPostRequest.setHeader("Content-type", "application/json");
//
////                long t = System.currentTimeMillis();
//                HttpResponse response = httpclient.execute(httpPostRequest);
////                Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");
//
//                HttpEntity entity = response.getEntity();
//
//                if (entity != null) {
//                    // Read the content stream
//                    InputStream instream = entity.getContent();
//
//                    // convert content stream to a String
//                    resultString= convertStreamToString(instream);
//                    instream.close();
////                    resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"
//
////                    jsonObjRecv = new JSONObject(resultString);
//
//                    // Raw DEBUG output of our received JSON object:
////                    Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");
//
//
//                }
//
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultString;
//        }
//
//        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public static String convertStreamToString(InputStream is) throws Exception {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line + "\n");
//        }
//        is.close();
//        return sb.toString();
//    }

    private boolean verificarCampos(){
        String email = this.campoEmail.getText().toString().trim();
        String senha = this.campoSenha.getText().toString().trim();
        String repetirSenha = this.campoRepetirSenha.getText().toString().trim();
        if (servicoValidacao.verificarCampoEmail(email)){
            this.campoEmail.setError("Email inválido");
            return false;
        }
        else if (servicoValidacao.verificarCampoVazio(senha)){
            this.campoSenha.setError("Senha inválida");
            return false;
        }
        else if (!senha.equals(repetirSenha)){
            this.campoRepetirSenha.setError("As senhas não correspondem");
            return false;
        }
        else {
            return true;
        }

    }
}






