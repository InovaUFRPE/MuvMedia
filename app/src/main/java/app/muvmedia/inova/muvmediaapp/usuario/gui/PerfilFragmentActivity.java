package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

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
    private Button alterarInformações;
    private Usuario usuario = Sessao.instance.getSailor().getUser();
    private Sailor sailor = Sessao.instance.getSailor();
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private TextView nome;
    private ImageView imSair;
    private int dia, mes, ano;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String nascimento;

    private EditText changeEmail, changeSenha, changeNome, changeNascimento;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        setUpView(v);
        //        exitApp(v);
        return v;
    }


//TODO      Método comentado
//    private void exitApp(View v){
//        imSair = v.findViewById(R.id.imageView3);
//        imSair.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Sessao.instance.setMuver(null);
//                getActivity().finish();
//            }
//        });
//    }

    private void setUpView(View v) {
        alterarInformações = v.findViewById(R.id.btnAlterarInfo);
        nome = v.findViewById(R.id.textView2);
//        nome.setText(Sessao.instance.getMuver().getNome());
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

    private void setDatePicker(){
        changeNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog nascimentoDialog = new DatePickerDialog(
                        getActivity(), dateSetListener,
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
                nascimento = dia+"-"+mesStr+"-"+year;
                if (!servicoValidacao.validarIdade(nascimento)){
                    Toast.makeText(getActivity(), "Idade inválida", Toast.LENGTH_SHORT).show();
                }
                else {
                    nascimento = dia+"-"+mesStr+"-"+year;
                    changeNascimento.setText(nascimento);
                }
            }
        };
    }

    private void createDialogInformacoes() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_mudar_inform, null);
        changeEmail = mView.findViewById(R.id.emailChange);
        changeSenha = mView.findViewById(R.id.novaSenha);
        changeNome = mView.findViewById(R.id.novoNome);
        changeNascimento = mView.findViewById(R.id.novoNascimento);
        Button buttonChangeInfo = mView.findViewById(R.id.buttonConfirmarAlteracoes);
        changeEmail.setText(usuario.getEmail());
        changeNome.setText(Sessao.instance.getSailor().getName());
        formatarNasc(Sessao.instance.getSailor().getBirthday());
        changeNascimento.setText(nascimento);
        setDatePicker();
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        buttonChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDados(changeNome, changeNascimento,changeEmail,changeSenha, dialog)){
                    try {
                        editarSailor(changeNome, changeNascimento,changeEmail,changeSenha);
                        if(Sessao.instance.getResposta().equals("Erro")){
                            changeEmail.setError("Email em uso");

                        }else{
                            changeEmail.setError("Email em uso");
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Editado com sucesso", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        changeEmail.setError("Erro inesperado");
                    }

                }

//                mudarEmail(changeEmail,dialog);
//                mudarSenha(changeSenha, dialog);
//                mudarDataNascimento(changeNascimento, dialog);
//                mudarNome(changeNome, dialog);
            }
        });
    }

    private void editarSailor(EditText changeNome, EditText changeNascimento, EditText changeEmail, EditText changeSenha) throws Exception {
        this.sailor.setName(changeNome.getText().toString());
        this.sailor.setBirthday(putBackNasc(nascimento));
        this.sailor.getUser().setEmail(changeEmail.getText().toString().trim());
        String password = changeSenha.getText().toString().trim();
        if(!this.servicoValidacao.verificarCampoVazio(password)){
            this.sailor.getUser().setPassword(password);
        }
        if(isOnline()){
            callServerUser(sailor);
        } else{
            Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }

    }

//    private void mudarSenha(EditText changeSenha, AlertDialog dialog){
//        if(verificarCampoSenha(changeSenha)){
//            if(isOnline()){
//                String senha = changeSenha.getText().toString().trim();
//                usuario.setPassword(senha);
//                try {
//                    mudarSenhaUsuario(senha);
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(), "Editado com sucesso", Toast.LENGTH_SHORT).show();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
//
//            }
//
//        }
//    }

    private boolean validarDados(EditText nome, EditText data,EditText email, EditText senha, AlertDialog dialog ){
        if(!verificarCampoNome(nome)){
            nome.setError("Campo não pode ser vazio");
            nome.requestFocus();
            return false;
        } else if(!verificarCampoEmail(email)){
            email.setError("Email inválido");
            return false;
        } else if(!vefificarSenhaVazia(senha)){
            if(!verificarCampoSenha(senha)){
                senha.setError("Sua senha deve ter no mínmo 6 dígitos");
                return false;
            }
        }
        return true;
    }

    private boolean vefificarSenhaVazia(EditText senha) {
        String nSenha = senha.getText().toString().trim();
        if(this.servicoValidacao.verificarCampoVazio(nSenha)) {
            return true;
        }
        return false;
    }

//    private void mudarSenhaUsuario(String senha) throws Exception {
//        this.usuario.setPassword(senha);
//        editarUsuario(this.usuario);
//    }

    private boolean verificarCampoNome(EditText Changenome){
        String nome = Changenome.getText().toString();
        if(this.servicoValidacao.verificarCampoVazio(nome)){
            return false;
        }
        return true;
    }
    private boolean verificarCampoSenha(EditText changeSenha) {
        String senha = changeSenha.getText().toString().trim();
        if(this.servicoValidacao.verificarCampoSenha(senha)){
            changeSenha.setError("Senha inválida");
            return false;
        }
        return true;
    }

//    private void mudarEmail(EditText changeEmail, AlertDialog dialog) {
//        if(verificarCampoEmail(changeEmail)){
//            if(isOnline()){
//                String resultado = "Editado com sucesso";
//                try {
//                    String email = changeEmail.getText().toString().trim();
//                    mudarEmailUsuario(email);
//                    if(Sessao.instance.getResposta().contains("Erro")){
//                        changeEmail.setError("Email em uso");
//                    } else {
//                        dialog.dismiss();
//                        Toast.makeText(getActivity(), "Editado com sucesso", Toast.LENGTH_SHORT).show();
//                        this.email.setText(email);
//                    }
////                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    resultado = e.getMessage();
//                    changeEmail.setError("Email em uso");
//                }
//            } else {
//                Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            changeEmail.setError("Email inválido");
//        }
//    }

//    private void mudarEmailUsuario(String email) throws Exception {
//        this.usuario.setEmail(email);
//        editarUsuario(this.usuario);
//    }

//    private void editarUsuario(Sailor usuario) throws Exception {
//        callServerUser(usuario);
//    }

    private void callServerUser(final Sailor sailor) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServicoHttpMuver servicoHttpMuver = new ServicoHttpMuver();
                try {
                    Sailor usuarioEditado = servicoHttpMuver.updateSailor(sailor);
                    Sessao.instance.setResposta("Sucess");
                } catch (Exception e) {
                    Sessao.instance.setResposta("Erro");
                }
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
}
