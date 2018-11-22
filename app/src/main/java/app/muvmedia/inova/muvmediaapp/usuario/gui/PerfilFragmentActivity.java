package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Sailor;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoHttpMuver;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class PerfilFragmentActivity extends Fragment {
    private Muver muver = Sessao.instance.getMuver();


    private TextView email;
    private Button alterarInformações, confirmarButton;
    private Usuario usuario = Sessao.instance.getSailor().getUser();
    private Sailor sailor = Sessao.instance.getSailor();
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private TextView nome, dialogText;
    private ImageView imSair;
    private int dia, mes, ano;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String nascimento;
    private String ultimoEmail = sailor.getUser().getEmail();
    private TextView changeEmail, changeSenha, changeNome, changeNascimento;
    private EditText edtNome, edtEmail, edtSenha;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        setUpView(v);
        //        exitApp(v);
        return v;
    }

    private void setUpView(View v) {
        alterarInformações = v.findViewById(R.id.btnAlterarInfo);
        nome = v.findViewById(R.id.textView2);
        nome.setText(Sessao.instance.getSailor().getName());
        setListners();
    }

    private void setListners() {
        this.alterarInformações.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogInformacoes();
            }
        });
    }


    private void createDialogInformacoes() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_mudar_inform, null);
        changeEmail = mView.findViewById(R.id.emailChange);
        changeSenha = mView.findViewById(R.id.novaSenha);
        changeNome = mView.findViewById(R.id.novoNome);
        changeNascimento = mView.findViewById(R.id.novoNascimento);
        changeEmail.setText(usuario.getEmail());
        changeNome.setText(Sessao.instance.getSailor().getName());
        formatarNasc(Sessao.instance.getSailor().getBirthday());
        changeNascimento.setText(nascimento);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        changeNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNome();
                dialog.dismiss();
            }
        });

        changeNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNascimento();
                dialog.dismiss();
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEmail();
                dialog.dismiss();
            }
        });

        changeSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSenha();
                dialog.dismiss();
            }
        });
    }

    private void dialogNome(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_nome_email, null);
        dialogText = mView.findViewById(R.id.textView6);
        dialogText.setText("Alterar Nome");
        edtNome = mView.findViewById(R.id.edtNomeEmailDialog);
        edtNome.setText(Sessao.instance.getSailor().getName());

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        confirmarButton = mView.findViewById(R.id.btnConfirmar);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarNome()){
                    try {
                        editarSailorNome(edtNome);
                        if(Sessao.instance.getResposta().equals("Erro")){
                            Log.i("Script Nome", "Erro: "+ Sessao.instance.getResposta());
                            edtNome.setError("Erro Nome");
                        }
                        else {
                            Log.i("Script Nome", "Sucesso: "+ Sessao.instance.getResposta());
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Nome Alterado", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        edtNome.setError("Erro inesperado");
                    }
                }
            }
        });
    }

    private void dialogNascimento(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_nome_email, null);
        dialogText = mView.findViewById(R.id.textView6);

        dialogText.setText("Alterar Nascimento");
        edtNome = mView.findViewById(R.id.edtNomeEmailDialog);
        edtNome.setText(nascimento);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        confirmarButton = mView.findViewById(R.id.btnConfirmar);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void dialogEmail(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_nome_email, null);
        dialogText = mView.findViewById(R.id.textView6);

        dialogText.setText("Alterar Email");
        edtEmail = mView.findViewById(R.id.edtNomeEmailDialog);
        edtEmail.setText(Sessao.instance.getSailor().getUser().getEmail());

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        confirmarButton = mView.findViewById(R.id.btnConfirmar);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                if(verificarCampoEmail(email)){
                    try {
                        editarEmail(email);
                        if(Sessao.instance.getResposta().equals("Sucess")){
                            Log.i("Script Update", "Sucesso: "+ Sessao.instance.getResposta());
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Editado com sucesso", Toast.LENGTH_SHORT).show();
                        }else{
                            edtEmail.setError("Email em uso");
                            edtEmail.setText(ultimoEmail);
                            Log.i("Script Update", "Email em uso: "+ Sessao.instance.getResposta());

                        }

                    } catch (Exception e) {
                        changeEmail.setError("Erro inesperado");
                    }

                }
                else {
                    edtEmail.setError("Email inválido");
                }
            }
        });
    }

    private void editarEmail(String changeEmail) throws Exception {
        this.sailor.getUser().setEmail(changeEmail);
        if(isOnline()){
            callServer(sailor);
        } else{
            Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void editarSailorNome(EditText novoNome) throws Exception {
        this.sailor.setName(novoNome.getText().toString());
        if(isOnline()){
            callServer(sailor);
        } else{
            Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void dialogSenha(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_senha, null);
        dialogText = mView.findViewById(R.id.textView7);
        dialogText.setText("Alterar Senha");
        edtSenha = mView.findViewById(R.id.edtSenha);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        confirmarButton = mView.findViewById(R.id.btnSenhaConfirmar);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCampoSenha(edtSenha)){
                    try {
                        editarUserSenha(edtSenha);
                        if(Sessao.instance.getResposta().equals("Sucess")){
                            Log.i("Script Nome", "Sucesso: "+ Sessao.instance.getResposta());
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Senha Alterada", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.i("Script Nome", "Erro: "+ Sessao.instance.getResposta());
                            edtSenha.setError("Erro Senha");
                        }
                    } catch (Exception e) {
                        edtSenha.setError("Erro inesperado");
                    }
                }
            }
        });
    }

    private void editarUserSenha(EditText novaSenha) throws Exception {
        this.sailor.getUser().setPassword(novaSenha.getText().toString());
        if(isOnline()){
            callServer(sailor);
        } else{
            Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void callServer(final Sailor sailor) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServicoHttpMuver servicoHttpMuver = new ServicoHttpMuver();
                try {
                    servicoHttpMuver.updateSailor(sailor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Sessao.instance.setResposta(usuarioEditado);
            }
        });
        thread.start();
        thread.join();
    }


    private boolean verificarCampoSenha(EditText changeSenha) {
        String senha = changeSenha.getText().toString().trim();
        if(this.servicoValidacao.verificarCampoSenha(senha)){
            edtSenha.setError("Senha inválida");
            edtSenha.requestFocus();
            return false;
        }
        return true;
    }


    private void formatarNasc(String nasc){
        String ano = nasc.substring(0, 4);

        String dia = nasc.substring(8, 10);

        String mes = nasc.substring(5, 7);
        nascimento = dia+"/"+mes+"/"+ano;
    }

    private String putBackNasc(String nasc){
        String ano = nasc.substring(6, 10);

        String dia = nasc.substring(0, 2);

        String mes = nasc.substring(3, 5);

        return ano+"-"+mes+"-"+dia;

    }


    private boolean validarNome() {
        if (!verificarCampoNome(edtNome)) {
            edtNome.setError("Campo não pode ser vazio");
            edtNome.requestFocus();
            return false;
        }
        return true;
    }

    private boolean verificarCampoNome(EditText Changenome){
        String nome = Changenome.getText().toString();
        if(this.servicoValidacao.verificarCampoVazio(nome)){
            return false;
        }
        return true;
    }

    private boolean verificarCampoEmail(String campo) {
        if(this.servicoValidacao.verificarCampoEmail(campo)){
            return false;
        }
        return true;
    }

    private boolean isOnline() {
        if(ServicoDownload.isNetworkAvailable(getActivity()))
        {
            return true;
        }else{
            return false;
        }
    }

}
