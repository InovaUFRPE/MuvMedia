package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

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
                if (verificarCampos()) {
                    String user = setarUsuario(campoEmail.getText().toString().trim(), campoSenha.getText().toString().trim());
                    cadastrar(user);
                    irSegundaTela();

                }
                }

        });
    }

    private String setarUsuario(String email, String senha){
        Usuario usuario = new Usuario();
        usuario.setPassword(senha);
        usuario.setEmail(email);
        usuario.setLevel(1);
        Gson gson = new Gson();
        String user = gson.toJson(usuario);

        return user;
    }

    private void cadastrar(String json){
        callServer("POST",json);
    }

    public void post(String completeUrl, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        String answer = "";
        HttpPost httpPost = new HttpPost(completeUrl);
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);

            HttpResponse resposta = httpClient.execute(httpPost);
            answer = EntityUtils.toString(resposta.getEntity());
            Log.i("Script", "ANSWER: "+ answer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void callServer(final String method, final String data){
        new Thread(){
            public void run(){
                post("https://muvmedia-api.herokuapp.com/public/register/user",data);
//                String answer = HttpConnection.getSetDataWeb("https://muvmedia-api.herokuapp.com/public/register/user", method, data);
//                Log.i("Script", "ANSWER: "+ answer);
            }
        }.start();

    }

    private boolean verificarCampos() {
        String email = this.campoEmail.getText().toString().trim();
        String senha = this.campoSenha.getText().toString().trim();
        String repetirSenha = this.campoRepetirSenha.getText().toString().trim();
        if (servicoValidacao.verificarCampoEmail(email)) {
            this.campoEmail.setError("Email inválido");
            return false;
        } else if (servicoValidacao.verificarCampoVazio(senha)) {
            this.campoSenha.setError("Senha inválida");
            return false;
        } else if (!senha.equals(repetirSenha)) {
            this.campoRepetirSenha.setError("As senhas não correspondem");
            return false;
        } else {
            return true;
        }
    }
    private void irSegundaTela(){
        Bundle bundle = new Bundle();
        Usuario usuario = new Usuario();
        usuario.setEmail(campoEmail.getText().toString());
        usuario.setPassword(campoSenha.getText().toString());
        bundle.putSerializable("tripla", usuario);

        Intent it = new Intent(getApplicationContext(), CriarConta2Activity.class);
        it.putExtra("tela1", bundle);
        startActivity(it);
    }
}







