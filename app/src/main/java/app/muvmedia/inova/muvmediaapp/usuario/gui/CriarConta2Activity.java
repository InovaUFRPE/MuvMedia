package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class CriarConta2Activity extends AppCompatActivity {
    private EditText campoNome, campoSobrenome, campoCpf, campoNascimento;
    private Button cadastrarConta;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta2);
        this.campoNome = findViewById(R.id.editText6);
        this.campoSobrenome = findViewById(R.id.editText7);
        this.campoCpf = findViewById(R.id.editText10);
        this.campoNascimento = findViewById(R.id.editText8);
        setMascaras();
        cadastrarConta();
    }


    private void cadastrarConta(){
        cadastrarConta = findViewById(R.id.button3);
        cadastrarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos()){
                    Toast.makeText(CriarConta2Activity.this, "Conta Criada: "+ campoNascimento.getText().toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean verificarCampos(){
        String nome = campoNome.getText().toString().trim();
        String sobreNome = campoSobrenome.getText().toString().trim();
        String cpf = campoCpf.getText().toString().trim();
        String nascimento = campoNascimento.getText().toString().trim();
        if (servicoValidacao.verificarCampoVazio(nome)){
            this.campoNome.setError("Nome inválido");
            campoNome.requestFocus();
            return false;
        }
        else if (servicoValidacao.verificarCampoVazio(sobreNome)){
            this.campoSobrenome.setError("Sobrenome inválido");
            campoSobrenome.requestFocus();
            return false;
        }
        else if (servicoValidacao.verificarCpf(cpf)){
            this.campoCpf.setError("CPF inválido");
            campoCpf.requestFocus();
            return false;
        }
        else if (!servicoValidacao.validadorAnoMesDia(nascimento)){
            campoNascimento.setError("Data de nascimento inválida");
            return false;
        }
        else if (!servicoValidacao.validarMaiorIdade(nascimento)){
            campoNascimento.setError("Precisa ser maior de idade");
            return false;
        }
        else {
            return true;
        }
    }

    private void setMascaras(){
        SimpleMaskFormatter cpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher setMaskCpf =  new MaskTextWatcher(campoCpf, cpf);
        campoCpf.addTextChangedListener(setMaskCpf);

        SimpleMaskFormatter nascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher setMaskNasc =  new MaskTextWatcher(campoNascimento, nascimento);
        campoNascimento.addTextChangedListener(setMaskNasc);
    }
}
