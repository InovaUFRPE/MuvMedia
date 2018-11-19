package app.muvmedia.inova.muvmediaapp.usuario.servico;

import android.util.Patterns;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServicoValidacao {
    public boolean verificarCampoVazio(String campo) {
        if (campo.isEmpty()) {
            return true;
        }
        return false;
    }
    public boolean verificarCampoEmail(String email) {
        if (verificarCampoVazio(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verificarCampoSenha(String senha){
        if (verificarCampoVazio(senha) || senha.length()<=5){
            return true;
        } else {
            return false;
        }
    }


    public boolean verificarCpf(String cpf){
        cpf = String.valueOf(cpf).replace(".","").replace("-","");
        //if (cpf.isEmpty() || cpf.length() != 14){//CPF TEM 11 DIGITOS, FALTA UM ALGORITMO PARA VER SE O CPF É REAL;
        if (isCPF(cpf)){
            return false;
        }
        else{
            return true;
        }
    }


    public boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else{
                return(false);
            }
        } catch (Exception erro) {
            erro.printStackTrace();
        }
        return false;
    }

    public boolean validadorAnoMesDia(String nascimento) {
        if (nascimento.length()!=10){
            return false;
        }
        String ano = nascimento.substring(6, 10);
        int anoDigitado = Integer.parseInt(ano);
        Calendar cal = Calendar.getInstance();
        int anoAtual = cal.get(Calendar.YEAR);
        if (anoAtual - anoDigitado > 100 ){
            return false;
        }
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date datinha = null;
        date.setLenient(false);
        try {
            datinha = date.parse(nascimento);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public boolean validarIdade(String nascimento){
        String ano = nascimento.substring(6, 10);
        int anoDigitado = Integer.parseInt(ano);

        String dia = nascimento.substring(0, 2);
        int diaDigitado = Integer.parseInt(dia);

        String mes = nascimento.substring(3, 5);
        int mesDigitado = Integer.parseInt(mes);

        Calendar cal = Calendar.getInstance();
        int diaAtual = cal.get(Calendar.DATE);
        int mesAtual = cal.get(Calendar.MONTH)+1;
        int anoAtual = cal.get(Calendar.YEAR);

        if (anoAtual - anoDigitado > 10) {
            return true;
        }
        else if (anoAtual - anoDigitado == 10 && mesDigitado < mesAtual){
            return true;
        }
        else if(anoAtual - anoDigitado == 10 && mesDigitado == mesAtual && diaDigitado <= diaAtual){
            return true;
        }

        else {
            return false;
        }
    }

}