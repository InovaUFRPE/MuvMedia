package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Sailor;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class NewCadastroActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha, campoNome, campoNascimento;
    private Button botaoCadastrar;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private int dia, mes, ano, diaX, mesX, anoX;
    private String nascimento, cadastro;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cadastro);
        setItensView();
        setDatePicker();
    }

    private void setDatePicker() {
        campoNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog nascimentoDialog = new DatePickerDialog(
                        NewCadastroActivity.this, dateSetListener,
                        ano, mes, dia);
                nascimentoDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mes = new Integer(month);
                mes = mes+1;
                String mesStr = String.valueOf(mes);
                if (mesStr.length() == 1){
                    mesStr = "0"+mesStr;
                }

                String dia = String.valueOf(dayOfMonth);
                if (dia.length() == 1){
                    dia = "0"+dia;
                }
                campoNascimento.setText(dia+"/"+mesStr+"/"+year);
//                nascimento = dia, mesStr, year);
                nascimento = year+"-"+mesStr+"-"+dia;
            }
        };

    }

    private void setItensView(){
        this.titulo = findViewById(R.id.textView5);
        String fontPath = "fonts/pieces.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        this.titulo.setTypeface(typeface);
        this.campoEmail = findViewById(R.id.campoEmailNew);
        this.campoSenha = findViewById(R.id.campoSenhaNew);
        this.campoNome = findViewById(R.id.campoNome2);
        this.campoNascimento = findViewById(R.id.campoNascimento);
        this.botaoCadastrar = findViewById(R.id.botaoRegistrar3);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificarCampos()){
                    cadastrar();
                    if (Sessao.instance.getResposta().contains("Usuário já existe")){
                        campoEmail.setError("Email em uso");
                        Log.i("Teste Cadastro", "Email em uso: "+ Sessao.instance.getResposta());

                        campoEmail.requestFocus();
                    } else {
                        finish();
                    }
                }
            }
        });



    }

    private void cadastrar() {
        if(isOnline()){
            try {
                cadastrarNewUser();
            } catch (InterruptedException e) {
                Toast.makeText(this, "Erro inesperado", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void cadastrarNewUser() throws InterruptedException {
        Sailor user = criarUser();
        Gson gson = new Gson();
        String usuario = gson.toJson(user);
        callServerCadastro(usuario);


    }

    private void callServerCadastro(final String usuario) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Sessao.instance.setResposta(HttpConnection.post("https://capitao-api.herokuapp.com/public/register",usuario));//Setar URL
            }
        });
        thread.start();
        thread.join();

    }

    private Sailor criarUser() {
        Sailor sailor = new Sailor();
        Usuario user = new Usuario();
        user.setEmail(this.campoEmail.getText().toString().trim());
        user.setPassword(this.campoSenha.getText().toString().trim());
        sailor.setName(this.campoNome.getText().toString().trim());
        sailor.setBirthday(nascimento);
        sailor.setUser(user);
        return sailor;

    }

    private boolean verificarCampos() {
        String email = this.campoEmail.getText().toString().trim();
        String senha = this.campoSenha.getText().toString().trim();
        String nome = this.campoNome.getText().toString().trim();
        String nascimento = campoNascimento.getText().toString().trim();
        if (servicoValidacao.verificarCampoEmail(email)) {
            this.campoEmail.setError("Email inválido");
            campoEmail.requestFocus();
            return false;
        } else if (servicoValidacao.verificarCampoSenha(senha)) {
            this.campoSenha.setError("Senha inválida");
            campoSenha.requestFocus();
            return false;
        } else if (servicoValidacao.verificarCampoVazio(nome)){
            this.campoNome.setError("Nome inválido");
            campoNome.requestFocus();
            return false;
        } else if (!servicoValidacao.validarIdade(nascimento)){
            Toast.makeText(getApplicationContext(), "Idade inválida", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext())) {
            return true;
        }else{
            return false;
        }
    }
}
