package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import app.muvmedia.inova.muvmediaapp.R;
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
            return;
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
}
