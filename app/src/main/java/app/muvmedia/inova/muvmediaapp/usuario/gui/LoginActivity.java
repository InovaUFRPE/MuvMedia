package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class LoginActivity extends AppCompatActivity {
    private EditText campoSenha, campoEmail;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private Button botaoCadstrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        encontrarElementosView();
        cadastrar();
    }

    private void cadastrar(){
        this.botaoCadstrar = findViewById(R.id.button2);
        botaoCadstrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CriarConta1Activity.class);
                startActivity(intent);
            }
        });
    }

    private void encontrarElementosView() {
        this.campoEmail = findViewById(R.id.editText4);
        this.campoSenha = findViewById(R.id.editText5);
        Button botaoLogar = findViewById(R.id.butaozinho);
        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {
        if (this.verificarCampos()) {
            if(isOnline()){
                String usuario = setarUsuario(campoEmail.getText().toString().trim(), campoSenha.getText().toString().trim());
                String logado = logar(usuario);
                if(logado.equals("404")){
                    //avisar que n encontrou
                } else{
                    Muver muverLogado = montarMuver(logado);
                    //Sessao.setOnline(muverlogado);

                }

            }

        }
    }

    private Muver montarMuver(String logado) {
        Gson gson = new Gson();
        Muver muver = gson.fromJson(logado, Muver.class);
        return muver;

    }

    private String logar(String jason){
        String call = callServer("GET", jason);
        return call;

    }

    private String callServer(final String method, final String data){
        final String[] resposta = {null};
        new Thread(){
            public void run(){
                resposta[0] = HttpConnection.getSetDataWeb("URL LOGIN", method, data);
                Log.i("Script", "ANSWER: "+ resposta);
            }
        }.start();
        return resposta[0];

    }

    private String setarUsuario(String email, String senha){
        Usuario usuario = new Usuario();
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setNivel(1);
        Gson gson = new Gson();
        String user = gson.toJson(usuario);

        return user;
    }

    private boolean isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext()))
        {
            return true;
        }else{
           return false;
        }
    }

    private boolean verificarCampos() {
        String email = this.campoEmail.getText().toString().trim();
        String senha = this.campoSenha.getText().toString().trim();
        if (this.servicoValidacao.verificarCampoEmail(email)) {
            this.campoEmail.setError("Email Inválido");
            return false;
        } else if (this.servicoValidacao.verificarCampoVazio(senha)) {
            this.campoSenha.setError("Senha Inválida");
            return false;
        } else {
            return true;
        }
    }


    private void montarUsuario(JSONArray jsonArray) throws JSONException {
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        Usuario usuario = new Usuario();
        usuario.set_id(jsonObject.optString("_id"));
        usuario.setEmail(jsonObject.optString("email"));
        usuario.setSenha(jsonObject.optString("senha"));
        //usuario.setMuver()...

        //Sessao.setUsuarioLogado...
    }

}
