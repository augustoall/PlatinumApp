package br.com.app.platinumapp.appvendas.Util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteDao;


public class Util extends Activity {

    public static final boolean IN_PRODUCAO = false;
    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";
    public static final int THEME_CMDV = 5;
    public static final int FONT_TYPEFACE_RobotoCondensed = 1;
    public static final int FONT_TYPEFACE_RobotoThin = 2;
    public static final int FONT_TYPEFACE_RobotoLight = 3;
    public static final int FONT_TYPEFACE_RobotoCondensedBold = 4;

    public static Typeface SetmyTypeface(Context ctx, int FONT_TYPEFACE) {
        Typeface myTypeface = null;
        switch (FONT_TYPEFACE) {
            case FONT_TYPEFACE_RobotoCondensed:
                myTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/RobotoCondensed-Light.ttf");
                break;

            case FONT_TYPEFACE_RobotoThin:
                myTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Thin.ttf");
                break;

            case FONT_TYPEFACE_RobotoLight:
                myTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Light.ttf");
                break;

            case FONT_TYPEFACE_RobotoCondensedBold:
                myTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
                break;

            default:
                break;
        }

        return myTypeface;

    }


    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[)]", "");
    }


    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String str = Util.unmask(s.toString());
                String str = s.toString();
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }


    public static double Bigdecimal_to_double(BigDecimal bigDecimal) {

        BigDecimal bd = new BigDecimal(bigDecimal.toString());
        double doublevalor = bd.doubleValue();
        return doublevalor;

    }

    public static String getBaseUrl(Context ctx) {
        String url = "";
        Configuracoes_SqliteDao dao = new Configuracoes_SqliteDao(ctx);
        Configuracoes_SqliteBean params = dao.BuscaParamentrosEmpresa();
        if (params != null) {
            url = params.getIP_SERVER();
        }
        return url.trim();
    }


    public static void Mensagem(Context context, String text, int toastType) {

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

        /*
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView tv = (TextView) layout.findViewById(R.id.tvTexto);
        tv.setTypeface(Util.SetmyTypeface(context, Util.FONT_TYPEFACE_RobotoCondensedBold));
        tv.setText(text);

        LinearLayout llRoot = (LinearLayout) layout.findViewById(R.id.llRoot);

        Drawable img;
        int bg;

        switch (toastType) {

            case THEME_CMDV:
                img = context.getResources().getDrawable(R.drawable.cmdvic);
                bg = R.drawable.toast_background_theme_cmdv;
                break;

            default:
                img = context.getResources().getDrawable(R.drawable.cmdvic);
                bg = R.drawable.toast_background_theme_cmdv;
                break;
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        llRoot.setBackgroundResource(bg);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        */
    }

    public static String FormataDataDDMMAAAA(String dataAmericanaString) {
        String retorno = "";
        String vc = dataAmericanaString.replace("-", "");
        retorno = vc.substring(6, 8) + "/" + vc.substring(4, 6) + "/" + vc.substring(0, 4);
        return retorno;
    }

    public static String FormataDataAAAAMMDD(String dataBrasilString) {
        String retorno = "";
        String vc = dataBrasilString.replace("/", "");
        retorno = vc.substring(4, 8) + "-" + vc.substring(2, 4) + "-" + vc.substring(0, 2);

        return retorno;
    }

    public static String FormataDataDDMMAAAA_ComHoras(String dataAmericanaString) {
        String retorno = "";
        String vc = dataAmericanaString.replace("-", "");
        retorno = vc.substring(6, 8) + "/" + vc.substring(4, 6) + "/" + vc.substring(0, 4) + vc.substring(8, 14);

        return retorno;
    }

    public static String RetornaMesEmString(int mesDoAno) {
        String MM = "";

        if (mesDoAno + 1 == 1) {
            MM = "01";
        } else if (mesDoAno + 1 == 2) {
            MM = "02";
        } else if (mesDoAno + 1 == 3) {
            MM = "03";
        } else if (mesDoAno + 1 == 4) {
            MM = "04";
        } else if (mesDoAno + 1 == 5) {
            MM = "05";
        } else if (mesDoAno + 1 == 6) {
            MM = "06";
        } else if (mesDoAno + 1 == 7) {
            MM = "07";
        } else if (mesDoAno + 1 == 8) {
            MM = "08";
        } else if (mesDoAno + 1 == 9) {
            MM = "09";
        } else {
            MM = String.valueOf(mesDoAno + 1);
        }

        return MM;
    }

    public static String RetornaDiaString(int DiadoMes) {

        String DD = "";

        if (DiadoMes == 1) {
            DD = "01";
        } else if (DiadoMes == 2) {
            DD = "02";
        } else if (DiadoMes == 3) {
            DD = "03";
        } else if (DiadoMes == 4) {
            DD = "04";
        } else if (DiadoMes == 5) {
            DD = "05";
        } else if (DiadoMes == 6) {
            DD = "06";
        } else if (DiadoMes == 7) {
            DD = "07";
        } else if (DiadoMes == 8) {
            DD = "08";
        } else if (DiadoMes == 9) {
            DD = "09";
        } else {
            DD = String.valueOf(DiadoMes);
        }

        return DD;

    }

    public static String DataHojeComHorasStringUSA() {
        // gravando a data em string yyyy-MM-dd HH:mm
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date data = new Date();
        return sdf.format(data.getTime());

    }

    public static String DataHojeComHorasStringBR() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date data = new Date();
        return sdf.format(data.getTime());
    }


    public static String DataSemHorasUSA() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        return sdf.format(data.getTime());
    }


    public static String DataBrasilComHoras() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date data = new Date();
        return sdf.format(data.getTime());
    }


    // http://codare.net/2007/02/02/java-gerando-codigos-hash-md5-sha/
    // este metodo � responsavel por gerar o rash usando a classe
    // java.security.MessageDigest.
    public static byte[] gerarHash(String frase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    // http://codare.net/2007/02/02/java-gerando-codigos-hash-md5-sha/
    // este metodo � responsavel por ler o rash que foi gerado
    public static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0)
                s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }

    // -------- Valida CPF ou CNPJ
    public static boolean validaCPF(String cpf) {
        // ------- Rotina para CPF
        if (cpf.length() == 11) {
            int d1, d2;
            int digito1, digito2, resto;
            int digitoCPF;
            // int soma;
            String nDigResult;
            d1 = d2 = 0;
            digito1 = digito2 = resto = 0;
            for (int n_Count = 1; n_Count < cpf.length() - 1; n_Count++) {
                digitoCPF = Integer.valueOf(cpf.substring(n_Count - 1, n_Count)).intValue();
                // --------- Multiplique a ultima casa por 2 a seguinte por 3 a
                // seguinte por 4 e assim por diante.
                d1 = d1 + (11 - n_Count) * digitoCPF;
                // --------- Para o segundo digito repita o procedimento
                // incluindo o primeiro digito calculado no passo anterior.
                d2 = d2 + (12 - n_Count) * digitoCPF;
            }
            ;
            // --------- Primeiro resto da divis�o por 11.
            resto = (d1 % 11);
            // --------- Se o resultado for 0 ou 1 o digito � 0 caso
            // contr�rio o
            // digito � 11 menos o resultado anterior.
            if (resto < 2)
                digito1 = 0;
            else
                digito1 = 11 - resto;
            d2 += 2 * digito1;
            // --------- Segundo resto da divis�o por 11.
            resto = (d2 % 11);
            // --------- Se o resultado for 0 ou 1 o digito � 0 caso
            // contr�rio o
            // digito � 11 menos o resultado anterior.
            if (resto < 2)
                digito2 = 0;
            else
                digito2 = 11 - resto;
            // --------- Digito verificador do CPF que est� sendo validado.
            String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());
            // --------- Concatenando o primeiro resto com o segundo.
            nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
            // --------- Comparar o digito verificador do cpf com o primeiro
            // resto + o segundo resto.
            return nDigVerific.equals(nDigResult);
        }

        return false;

    }


    public static boolean validaCNPJ(String CNPJ) {
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") || (CNPJ.length() != 14))
            return (false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o c�digo para eventuais erros de conversao de tipo
        // (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-�simo caractere do CNPJ em um n�mero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posi��o de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else
                dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else
                dig14 = (char) ((11 - r) + 48);

            // Verifica se os d�gitos calculados conferem com os d�gitos
            // informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return (true);
            else
                return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }


    public static boolean exportar_banco_sdcard(Context context) {
        String DATABASE_NAME = "cmdv.db";
        String databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
        String inFileName = databasePath;
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME;

            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            //Close the streams
            output.flush();
            output.close();
            fis.close();
            return
                    true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validaremail(String email) {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }

    public static void gerarHistoricoPagamento(Context ctx, Integer Parcela, String Cliente, String ComoRecebeu, String ChaveVenda, BigDecimal valorrealparcela, BigDecimal valorpagonodia, BigDecimal restante, String enviado) {

        HistoricoPagamento_SqliteBean HistBean = new HistoricoPagamento_SqliteBean();
        HistoricoPagamento_SqliteDao HistDao = new HistoricoPagamento_SqliteDao(ctx);

        SimpleDateFormat data_simples = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendario = Calendar.getInstance(new Locale("pt", "BR"));
        Date data = calendario.getTime();

        HistBean.setHist_numero_parcela(Parcela);
        HistBean.setHist_datapagamento(data_simples.format(data));
        HistBean.setHist_nomecliente(Cliente);
        HistBean.setHist_pagou_com(ComoRecebeu);
        HistBean.setVendac_chave(ChaveVenda);
        HistBean.setHist_valor_real_parcela(valorrealparcela);
        HistBean.setHist_valor_pago_no_dia(valorpagonodia);
        HistBean.setHist_restante_a_pagar(restante);
        HistBean.setHist_enviado(enviado);
        HistDao.grava_historico(HistBean);
    }

}
