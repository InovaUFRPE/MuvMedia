package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.muvmedia.inova.muvmediaapp.R;
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
                if (verificarCampos()){
                    Intent intent = new Intent(getApplicationContext(), CriarConta2Activity.class);
                    startActivity(intent);
                }

            }
        });
    }

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
