package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.cupom.dominio.Campaign;
import app.muvmedia.inova.muvmediaapp.cupom.gui.CampanhaAdapter;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Sailor;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Usuario;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoHttpMuver;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoValidacao;

public class PerfilFragmentActivity extends Fragment {
    private Button alterarInformações, confirmarButton;
    private Usuario usuario = Sessao.instance.getSailor().getUser();
    private Sailor sailor = Sessao.instance.getSailor();
    private ServicoValidacao servicoValidacao = new ServicoValidacao();
    private TextView nome, dialogText, emailInvisivel, nascimentoInvisivel;
    private int dia, mes, ano;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String nascimento;
    private String emailAtual = sailor.getUser().getEmail();
    private TextView changeEmail, changeSenha, changeNome, changeNascimento,xp, level;
    ProgressBar progressBar;
    private EditText edtNome, edtEmail, edtSenha, edtNascimento;
    private ListView listaCampanha;
    private List<Campaign> campanhas = sailor.getCampaigns();
    private CampanhaAdapter campanhaAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        try {
            callSailor();
        } catch (InterruptedException e) {
            Toast.makeText(getActivity(), "Erro Inesperado", Toast.LENGTH_SHORT).show();
        }
        setUpView(v);
        setUpListView(v);
        return v;
    }

    private void setUpListView(View v) {
        listaCampanha = v.findViewById(R.id.listacara);
        if(campanhas == null){
            campanhas = new ArrayList<Campaign>();
        }
        campanhaAdapter = new CampanhaAdapter(getContext(), campanhas);
        listaCampanha.setAdapter(campanhaAdapter);
        listaCampanha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Campaign campaign = campanhas.get(i);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(campaign.getUrl()));
                startActivity(intent);
            }
        });


    }

    private void setUpView(View v) {
        alterarInformações = v.findViewById(R.id.btnAlterarInfo);
        nome = v.findViewById(R.id.textView2);
        nome.setText(Sessao.instance.getSailor().getName());
        emailInvisivel = v.findViewById(R.id.emailTextPerfil);
        emailInvisivel.setText(Sessao.instance.getSailor().getUser().getEmail());
        nascimentoInvisivel = v.findViewById(R.id.nascimentoTextPerfil);
        nascimentoInvisivel.setText(formatarNasc(Sessao.instance.getSailor().getBirthday()));
//        nascimentoInvisivel.setText(Sessao.instance.getSailor().getBirthday());
        xp = v.findViewById(R.id.textViewXp);
        xp.setText("XP:"+String.valueOf(Sessao.instance.getSailor().getXp())+"/"+getMaxToBar());
        level = v.findViewById(R.id.textViewLevel);
        level.setText("LEVEL:"+String.valueOf(Sessao.instance.getSailor().getLevel()));
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setMax(getMaxToBar());
        progressBar.setProgress(sailor.getXp());
        setListners();
    }

    public void callSailor() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServicoHttpMuver servicoHttpMuver = new ServicoHttpMuver();
                try {
                    Sailor sailor = servicoHttpMuver.checkSailor(Sessao.instance.getSailor().get_id());
                    Sessao.instance.setSailor(sailor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Sessao.instance.setResposta(usuarioEditado);
            }
        });
        thread.start();
        thread.join();


    }

    private void setListners() {
        this.alterarInformações.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.dialog_mudar_inform, null);
                setInformacoesDialog(mView);
                createDialogInformacoes(mView);
            }
        });
    }


    private void setInformacoesDialog(View mView){
        changeEmail = mView.findViewById(R.id.emailChange);
        changeSenha = mView.findViewById(R.id.novaSenha);
        changeNome = mView.findViewById(R.id.novoNome);
        changeNascimento = mView.findViewById(R.id.novoNascimento);
        changeEmail.setText(emailInvisivel.getText().toString());
        changeNome.setText(nome.getText().toString());
        changeNascimento.setText(nascimentoInvisivel.getText().toString());
    }


    private void createDialogInformacoes(View mView) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

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

        changeNome.setText(nome.getText().toString());
        changeEmail.setText(emailInvisivel.getText().toString());
        changeNascimento.setText(nascimentoInvisivel.getText().toString());

    }


    private void dialogNome(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_nome_email, null);
        dialogText = mView.findViewById(R.id.textView6);
        dialogText.setText("Alterar Nome");
        edtNome = mView.findViewById(R.id.edtNomeEmailDialog);
        edtNome.setText(nome.getText().toString());

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
                            nome.setText(edtNome.getText().toString());
                            edtNome.setText(nome.getText().toString());
                            changeNome.setText(nome.getText().toString());
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
        View mView = getLayoutInflater().inflate(R.layout.dialog_birthday, null);

        edtNascimento = mView.findViewById(R.id.edtDataNascimento);
        edtNascimento.setText(nascimentoInvisivel.getText().toString());

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


        edtNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePicker();
//                Toast.makeText(getActivity(), nascimento, Toast.LENGTH_SHORT).show();
//                edtNascimento.setText(formatarNasc(nascimento));
            }
        });

        confirmarButton = mView.findViewById(R.id.btnConfirmarData);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDataNascimento(dialog);
            }
        });
    }


    private void salvarDataNascimento(AlertDialog dialog){
        if (!servicoValidacao.validarIdade(edtNascimento.getText().toString())){
            Toast.makeText(getActivity(), "Idade inválida", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                editarSailorNascimento(putBackNasc(edtNascimento.getText().toString()));
//                String nascimentoFinal = nascimento.replace("-", "/");
                nascimentoInvisivel.setText(edtNascimento.getText().toString());
                edtNascimento.setText(edtNascimento.getText().toString());
                changeEmail.setText(edtNascimento.getText().toString());
                dialog.dismiss();
                Toast.makeText(getActivity(), "Nascimento alterado" + nascimentoInvisivel.getText().toString(), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setDatePicker(){
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog nascimentoDialog = new DatePickerDialog(
                getActivity(), dateSetListener,
                ano, mes, dia);
        nascimentoDialog.show();

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
                edtNascimento.setText(nascimento.replace("-", "/"));
            }
        };
//        edtNascimento.setText(formatarNasc(nascimento));
    }

    private void editarSailorNascimento(String novoNasc) throws Exception {
        this.sailor.setBirthday(novoNasc);
        if(isOnline()){
            callServer(sailor);
        } else{
            Toast.makeText(getActivity(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }



    private void dialogEmail(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_nome_email, null);
        dialogText = mView.findViewById(R.id.textView6);

        dialogText.setText("Alterar Email");
        edtEmail = mView.findViewById(R.id.edtNomeEmailDialog);
        edtEmail.setText(emailInvisivel.getText().toString());

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
                            emailInvisivel.setText(edtEmail.getText().toString());
                            edtEmail.setText(emailInvisivel.getText().toString());
                            changeEmail.setText(emailInvisivel.getText().toString());
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Editado com sucesso", Toast.LENGTH_SHORT).show();
                        }else{
                            edtEmail.setError("Email em uso");
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


    private String formatarNasc(String nasc){
        String ano = nasc.substring(0, 4);

        String dia = nasc.substring(8, 10);

        String mes = nasc.substring(5, 7);
        String nascimento2 = dia+"/"+mes+"/"+ano;
        return nascimento2;
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

    public int getMaxToBar() {
            return (int) Math.floor(175*Math.pow((double)sailor.getLevel(),1.5));
        }
}
