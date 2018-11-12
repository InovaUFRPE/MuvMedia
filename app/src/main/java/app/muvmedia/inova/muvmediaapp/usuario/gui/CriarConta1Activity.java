package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;



import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoHttpMuver;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class CriarConta1Activity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;
    private Button botaoProximo;
    private TextView textView;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private String validar = "";
    private ProgressDialog mprogressDialog;
    private String retornoEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta1);
        setTextViews();
        irCadastro2();
    }

    private void setTextViews() {
        textView = findViewById(R.id.textView7);
        String fontPath = "fonts/teste1.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        textView.setTypeface(typeface);

    }

    private void irCadastro2() {
        this.botaoProximo = findViewById(R.id.button);
        this.campoEmail = findViewById(R.id.editText);
        this.campoSenha = findViewById(R.id.editText2);
        mprogressDialog = new ProgressDialog(CriarConta1Activity.this);
        this.mprogressDialog.setMessage("Aguarde...");
        botaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos()) {
                    verificarEmail();
                }
                }

        });
    }

    private void verificarEmail() {
        if(isOnline()){
            String user = setarUsuario(campoEmail.getText().toString().trim(), campoSenha.getText().toString().trim());
            mprogressDialog.show();
            try {
                callServer(campoEmail.getText().toString());
                if (retornoEmail.contains(campoEmail.getText().toString())){
                    Log.i("Script", "Email encontrado no banco");
                    campoEmail.requestFocus();
                    campoEmail.setError("Email já cadastrado");
                }
                else {
                    irSegundaTela();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mprogressDialog.dismiss();
        } else {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();

        }
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
            this.mprogressDialog.dismiss();
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
//        callServer("POST",json);
    }


//    private void callServer(final String method, final String data)  throws InterruptedException{
private void callServer(final String email)  throws InterruptedException{
    final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                retornoEmail = ServicoHttpMuver.getEmailUser(email);
//                validar = HttpConnection.post("https://muvmedia-api.herokuapp.com/public/register/user",data);
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
        } else if (servicoValidacao.verificarCampoSenha(senha)) {
            this.campoSenha.setError("Senha inválida");
            return false;
        } else {
            return true;
        }
    }
    private void irSegundaTela(){
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        Usuario usuario = new Usuario();
        usuario.setEmail(campoEmail.getText().toString());
        usuario.setPassword(campoSenha.getText().toString());
//        gson.fromJson(validar, Usuario.class);
        bundle.putSerializable("tripla", usuario);
        Intent it = new Intent(getApplicationContext(), CriarConta2Activity.class);
        it.putExtra("tela1", bundle);
        startActivity(it);
        finish();
    }

    private boolean isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext())) {
            return true;
        }else{
            return false;
        }
    }
}







