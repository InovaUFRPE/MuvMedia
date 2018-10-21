package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
//import app.muvmedia.inova.muvmediaapp.usuario.dominio.Login;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.SessionApi;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoHttpMuver;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class LoginActivity extends AppCompatActivity {
    private EditText campoSenha, campoEmail;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private Button botaoCadstrar;
    private String muverString;
    private TextView nomeApp,textCriarConta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        setAppContexto();
        encontrarElementosView();
        cadastrar();
    }

    private void setAppContexto() {
        nomeApp = findViewById(R.id.nomeEmp);
        String fontPath = "fonts/airstrike.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        nomeApp.setTypeface(typeface);
    }

    private void cadastrar(){
        //this.botaoCadstrar = findViewById(R.id.button2);
        this.textCriarConta = findViewById(R.id.textViewCriarConta);

        textCriarConta.setOnClickListener(new View.OnClickListener() {
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
                try {
                    login();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void login() throws InterruptedException {
        if (this.verificarCampos()) {
            if(isOnline()){
                String usuario = setarUsuario(campoEmail.getText().toString().trim(), campoSenha.getText().toString().trim());
                logar(usuario);
                if(Sessao.instance.getResposta().contains("Usuário ou senha incorreto")){
                    Toast.makeText(this, "Usuário ou senha incorreto", Toast.LENGTH_SHORT).show();
                } else {
                    getSessaoApi();
                    Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
                    startActivity(intent);
                    Toast.makeText(this, "Logado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getSessaoApi() throws InterruptedException {
        Gson gson = new Gson();
        SessionApi sessionApi = gson.fromJson(Sessao.instance.getResposta(), SessionApi.class);
        Sessao.instance.setSession(sessionApi);
        setMuverApi(sessionApi.getUser());
    }

    private void setMuverApi(final Usuario usuario) throws InterruptedException {
        final ServicoHttpMuver servicoHttpMuver = new ServicoHttpMuver();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Muver muver = servicoHttpMuver.getMuverByUser(usuario);
                muver.getNome();
            }
        });
        thread.start();
        thread.join();
    }


    private void logar(String jason)  throws InterruptedException {
        callServer(jason);

    }

    private void callServer(final String data) throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Sessao.instance.setResposta(HttpConnection.post("http://muvmedia-api.herokuapp.com/auth/login", data));
                Log.i("Script", "OLHAAA: "+ Sessao.instance
                .getResposta());
            }
        });
        thread.start();
        thread.join();
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
        usuario.setPassword(jsonObject.optString("senha"));
        //usuario.setMuver()...

        //Sessao.setUsuarioLogado...
    }



}
