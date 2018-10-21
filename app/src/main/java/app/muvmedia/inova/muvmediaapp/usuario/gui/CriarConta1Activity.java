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
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class CriarConta1Activity extends AppCompatActivity {
    private EditText campoEmail, campoSenha, campoRepetirSenha;
    private Button botaoProximo;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private String validar = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta1);
        irCadastro2();
    }

    private void irCadastro2() {
        this.botaoProximo = findViewById(R.id.button);
        this.campoEmail = findViewById(R.id.editText);
        this.campoSenha = findViewById(R.id.editText2);
        botaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos()) {
                    String user = setarUsuario(campoEmail.getText().toString().trim(), campoSenha.getText().toString().trim());
                    try {
                        cadastrar(user);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cadastrarUser();
                }
                }

        });
    }

    private void cadastrarUser() {
        if (this.validar.length()>10){
            String teste = this.validar.substring(2,4);
            if(teste.equals("er")){
                campoEmail.requestFocus();
                campoEmail.setError("Email em uso");
            }else {
                irSegundaTela();
            }
        }

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

    private void cadastrar(String json) throws InterruptedException{
        callServer("POST",json);
    }


    private void callServer(final String method, final String data)  throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                validar = HttpConnection.post("https://muvmedia-api.herokuapp.com/public/register/user",data);
//                String answer = HttpConnection.getSetDataWeb("https://muvmedia-api.herokuapp.com/public/register/user", method, data);
//                Log.i("Script", "ANSWER: "+ answer);
            }
        });
        thread.start();
        thread.join();
    }

    private boolean verificarCampos() {
        String email = this.campoEmail.getText().toString().trim();
        String senha = this.campoSenha.getText().toString().trim();
        if (servicoValidacao.verificarCampoEmail(email)) {
            this.campoEmail.setError("Email inválido");
            return false;
        } else if (servicoValidacao.verificarCampoVazio(senha)) {
            this.campoSenha.setError("Senha inválida");
            return false;
        } else {
            return true;
        }
    }
    private void irSegundaTela(){
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String jaja = validar;
        Usuario usuario = gson.fromJson(validar, Usuario.class);
        bundle.putSerializable("tripla", usuario);
        Intent it = new Intent(getApplicationContext(), CriarConta2Activity.class);
        it.putExtra("tela1", bundle);
        startActivity(it);
        finish();
    }
}







