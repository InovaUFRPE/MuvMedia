package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoHttpMuver;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class PerfilFragmentActivity extends Fragment {
    private TextView boasVindas;
    private TextView anuncio;
    private TextView conversoes;
    private ImageView botaoEditarPerfil;
    private Muver muver = Sessao.instance.getMuver();


    private TextView email;
    private Button mudarEmailButton, mudarSenhaButton, voltarButton;
    private Usuario usuario = Sessao.instance.getMuver().getUsuario();
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private ImageView imSair;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
//        irEditarPerfil(v);
//        setBoasVindas(v);
//        setAnuncio(v);
//        setConversoes(v);
        setUpView(v);
        exitApp(v);
        return v;
    }

    private void exitApp(View v){
//        imSair = v.findViewById(R.id.imageView3);
//        imSair.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Sessao.instance.setMuver(null);
//                getActivity().finish();
//            }
//        });
    }

//    private void setBoasVindas(View v) {
//        boasVindas = v.findViewById(R.id.nomeMuver);
//        String nome = muver.getNome();
//        boasVindas.setText("Olá "+nome+"!"); //+nome do muver
//    }

    private void setUpView(View v) {
        mudarEmailButton = v.findViewById(R.id.btnMudarEmail);
        mudarSenhaButton = v.findViewById(R.id.btnMudarSenha);
        setListners();
    }

    private void setListners() {
        this.mudarEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogEmail();
            }
        });
        this.mudarSenhaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crateDialogSenha();

            }
        });
    }

    private void crateDialogSenha() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_mudar_senha, null);
        final EditText changeSenha = mView.findViewById(R.id.senhaChange);
        Button buttonChangeSenha = mView.findViewById(R.id.buttonConfirmarMudarSenha);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        buttonChangeSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarSenha(changeSenha,dialog);
            }
        });

    }

    private void createDialogEmail() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_mudar_email, null);
        final EditText changeEmail = mView.findViewById(R.id.emailChange);
        Button buttonChangeEmail = mView.findViewById(R.id.buttonConfirmarMudarEmail);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        buttonChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarEmail(changeEmail,dialog);
            }
        });
    }

    private void mudarSenha(EditText changeSenha, AlertDialog dialog){
        if(verificarCampoSenha(changeSenha)){
            if(isOnline()){
                String senha = changeSenha.getText().toString().trim();
                usuario.setPassword(senha);
                try {
                    mudarSenhaUsuario(senha);
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Editado com sucesso", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void mudarSenhaUsuario(String senha) throws Exception {
        this.usuario.setPassword(senha);
        editar(this.usuario);
    }

    private boolean verificarCampoSenha(EditText changeSenha) {
        String senha = changeSenha.getText().toString().trim();
        if(this.servicoValidacao.verificarCampoSenha(senha)){
            changeSenha.setError("Senha inválida");
            return false;
        }
        return true;
    }

    private void mudarEmail(EditText changeEmail, AlertDialog dialog) {
        if(verificarCampoEmail(changeEmail)){
            if(isOnline()){
                String resultado = "Editado com sucesso";
                try {
                    String email = changeEmail.getText().toString().trim();
                    mudarEmailUsuario(email);
                    if(Sessao.instance.getResposta().contains("Erro")){
                        changeEmail.setError("Email em uso");
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Editado com sucesso", Toast.LENGTH_SHORT).show();
                        this.email.setText(email);
                    }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultado = e.getMessage();
                    Toast.makeText(getActivity(), resultado, Toast.LENGTH_SHORT).show();
                    changeEmail.setError("Email em uso");
                }
            } else {
                Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
            }

        } else {
            changeEmail.setError("Email inválido");
        }
    }

    private void mudarEmailUsuario(String email) throws Exception {
        this.usuario.setEmail(email);
        editar(this.usuario);
    }

    private void editar(Usuario usuario) throws Exception {
        callServer(usuario);
    }

    private void callServer(final Usuario usuario) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServicoHttpMuver servicoHttpMuver = new ServicoHttpMuver();
                try {
                    Usuario usuarioEditado = servicoHttpMuver.updateUsuario(usuario);
                    Sessao.instance.setResposta("Sucess");
                } catch (Exception e) {
                    Sessao.instance.setResposta("Erro");
                }
                //Sessao.instance.setResposta(usuarioEditado);
            }
        });
        thread.start();
        thread.join();
    }


    private boolean verificarCampoEmail(EditText campo) {
        String email = campo.getText().toString().trim();
        if(this.servicoValidacao.verificarCampoEmail(email)){
            campo.setError("Email inválido");
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
