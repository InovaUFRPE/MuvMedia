package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
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
        if (!this.verificarCampos()) {
            isOnline();
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

    private void isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext()))
        {
            logar newsTask = new logar();
            newsTask.execute();
        }else{
            Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
        }
    }

    public class logar extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... strings) {
            String xml;
            String urlParameters = "";
            xml = ServicoDownload.excuteGet("http://processo.stos.mobi/app/filme/listar", urlParameters);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            if(xml.length()>10){
                try {
                    JSONArray jsonArray = new JSONArray(xml);
                    //for (int i = 0; i < jsonArray.length(); i++) { PARA ARRAYS COM MUITAS COISAS, TIPO A DE BANDEIRAS DE UM POSTO
                    montarUsuario(jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro não esperado", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Usuário não encontrado", Toast.LENGTH_SHORT).show();
            }
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
