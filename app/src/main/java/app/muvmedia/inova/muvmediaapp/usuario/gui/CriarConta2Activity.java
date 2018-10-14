package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.gson.Gson;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class CriarConta2Activity extends AppCompatActivity {
    private EditText campoNome, campoSobrenome, campoCpf, campoNascimento;
    private Button cadastrarConta;
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private Muver muver = new Muver();
    private Usuario usuario = new Usuario();
    private String validar;

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
                    receberDadosTela1();
                    String muver = criarMuver();
                    try {
                        cadastrar(muver);
                        if(Sessao.instance.getResposta().contains("EMV01")){
                            campoCpf.requestFocus();
                            campoCpf.setError("CPF JÁ CADASTRADO");
                        } else{
                            Toast.makeText(CriarConta2Activity.this, "Conta Criada", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            }
        });
    }

    private void cadastrar(String json) throws InterruptedException {
        callServer(json);
    }

    private void callServer(final String data) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Sessao.instance.setResposta(HttpConnection.post("https://muvmedia-api.herokuapp.com/public/register/muver",data));
//                String answer = HttpConnection.getSetDataWeb("https://muvmedia-api.herokuapp.com/public/register/user", method, data);
//                Log.i("Script", "ANSWER: "+ answer);
            }
        });
        thread.start();
        thread.join();
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

    private String criarMuver(){
        String cpf = String.valueOf(campoCpf.getText().toString()).replace(".","").replace("-","");
        muver.setNome(campoNome.getText().toString());
//        muver.setSobrenome(campoSobrenome.getText().toString());
        muver.setCpf(cpf);
        muver.setDataDeNascimento(formatarData());
        muver.setUsuario(this.usuario);
        Gson gson = new Gson();
        String muver = gson.toJson(this.muver);
        return muver;
    }

    private String formatarData(){
        String ano = campoNascimento.getText().toString().substring(6, 10);
        String mes = campoNascimento.getText().toString().substring(3, 5);
        String dia = campoNascimento.getText().toString().substring(0, 2);
        String nascimento = ano+"-"+mes+"-"+dia;

        return nascimento;
    }

    private void receberDadosTela1(){
        Bundle bundle = getIntent().getBundleExtra("tela1");
        if (bundle != null){
            this.usuario = (Usuario) bundle.getSerializable("tripla");
            }
    }
}
