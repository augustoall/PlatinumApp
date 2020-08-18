package br.com.app.platinumapp.appvendas.Importacoes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.app.platinumapp.appvendas.Main.Principal;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.FireBaseDao;
import br.com.app.platinumapp.appvendas.Model.FirebaseBean;
import br.com.app.platinumapp.appvendas.Model.Usuarios_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Usuarios_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

public class Pedir_chave_de_licenca extends Activity {

    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_MENSAGEM = "mensagem";
    private static final String TAG_DIAS = "dias";
    private static final String TAG_CPF = "emp_cpf";
    private static final String TAG_NUM_CEL = "emp_numerocelular";
    private static final String TAG_EMAIL = "emp_email";
    private static final String TAG_CODIGO_USUARIO_VENDEDOR = "usu_codigo";
    private static String URL_GET_LICENCA;

    private RequestQueue rq;
    private Map<String, String> params;
    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;
    private Usuarios_SqliteBean usuBean;
    private Usuarios_SqliteDao usuDao;
    private String licenca;
    private String nova_chave_licenca;
    private EditText txt_licenca;
    private String emp_cpf;
    private String emp_email;
    private String emp_celularkey;
    private String celkey;
    private int dias_reativando;
    private String cpf_reativando;
    private String numerocelular_reativando;
    private String email_reativando;
    private String TAG_CODIGO_USUARIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedir_chave_de_licenca);
        rq = Volley.newRequestQueue(Pedir_chave_de_licenca.this);
        empDao = new Empresa_SqliteDao(this);
        empBean = new Empresa_SqliteBean();
        empBean = empDao.buscarEmpresa();
        usuBean = new Usuarios_SqliteBean();
        usuDao = new Usuarios_SqliteDao(this);

        declaraObjetos();
        rq = Volley.newRequestQueue(Pedir_chave_de_licenca.this);

        URL_GET_LICENCA = Util.getBaseUrl(getApplicationContext()) + "/json/requisicao_chave_acesso.php";

        List<FirebaseBean> mFirebaseBeanList = new FireBaseDao(getApplicationContext()).getAllFirebasesTypeLIC();

        if (mFirebaseBeanList != null) {
            for (FirebaseBean fire : mFirebaseBeanList) {
                txt_licenca.setText(fire.getFbs_mensagem().toString().trim());
                Log.i("script", "licenca :::" + fire.getFbs_mensagem().toString().trim());
            }
        }

    }


    public void adquirirlicenca(View v) {

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            obter_licenca_com_chave_de_acesso();
        } else {
            Util.Mensagem(Pedir_chave_de_licenca.this, "sem conexão com internet", Util.THEME_CMDV);
        }
    }

    public void obter_licenca_com_chave_de_acesso() {


        // se a empresa for != de null existe dados da empresa no sqlite e nao foi
        // desinstalado o aplicativo . Se o usuario desinstalar o app pra tentar burlar
        // os dias de licenca a empBean vai ser = null entrando no proximo if (empBean == null) {}
        // ou seja o usuario vai estar reativando o aplicativo
        if (empBean != null) {

            declaraObjetos();
            pegaValoresFields();

            params = new HashMap<String, String>();

            params.put("reativar", "N");
            params.put("newkey", nova_chave_licenca);
            params.put("key_existing", licenca);
            params.put("emp_celularkey", emp_celularkey);
            params.put("emp_cpf", emp_cpf);
            params.put("emp_email", emp_email);
            Log.i("cmdv", "REATIVAR = NAO ");

            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Method.POST, URL_GET_LICENCA, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        int sucesso = (Integer) response.get(TAG_SUCCESSO);
                        String mensagem = (String) response.get(TAG_MENSAGEM);
                        int dias = (Integer) response.get(TAG_DIAS);

                        switch (sucesso) {

                            case 20:

                                empBean.setEmp_totalemdias(String.valueOf(dias));
                                empDao.EditarEmpresa(empBean);
                                empBean = empDao.buscarEmpresa();
                                Intent p1 = new Intent(getBaseContext(), Principal.class);
                                p1.putExtra("mensagem", "faltam " + empBean.getEmp_totalemdias() + " dias para terminar sua licenca");
                                startActivity(p1);
                                Util.Mensagem(getBaseContext(), mensagem, Util.THEME_CMDV);
                                break;


                            case 30:

                                Util.Mensagem(getBaseContext(), mensagem, Util.THEME_CMDV);
                                break;


                            case 40:

                                Util.Mensagem(getBaseContext(), mensagem, Util.THEME_CMDV);
                                break;


                            case 50:

                                empBean.setEmp_totalemdias(String.valueOf(dias));
                                empDao.EditarEmpresa(empBean);
                                empBean = empDao.buscarEmpresa();
                                Intent p2 = new Intent(getBaseContext(), Principal.class);
                                p2.putExtra("mensagem", "faltam " + empBean.getEmp_totalemdias() + "  dias para terminar sua licenca");
                                startActivity(p2);
                                Util.Mensagem(getBaseContext(), mensagem, Util.THEME_CMDV);
                                break;


                            case 60:

                                Util.Mensagem(getBaseContext(), mensagem, Util.THEME_CMDV);
                                break;


                            case 70:

                                Util.Mensagem(getBaseContext(), mensagem, Util.THEME_CMDV);
                                break;


                            case 80:

                                Util.Mensagem(getBaseContext(), mensagem, Util.THEME_CMDV);
                                break;

                        }

                    } catch (JSONException e) {
                        Log.i("Script", "JSONException: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.Mensagem(getApplicationContext(), error.getMessage().toString(), Util.THEME_CMDV);
                }
            });

            request.setTag("tag");
            rq.add(request);

        }

        // tabela da empresa esta vazia
        if (empBean == null) {

            // REATIVANDO A APP CASO O CLIENTE TENHA DESINSTALADO E INSTALADO DE
            // NOVO POR CAUSA DOS DIAS VENCIDOS

            Log.i("cmdv", "REATIVAR = SIM ");

            declaraObjetos();
            celkey = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
            licenca = txt_licenca.getText().toString();

            Random gerador_chave_numerica = new Random();
            Integer primeira_chave = gerador_chave_numerica.nextInt(15000);
            String chave = licenca + new Date().getTime() + celkey;
            String chave_cortada = Util.stringHexa(Util.gerarHash(chave, "MD5")).substring(0, 5);
            String new_key_reativando = primeira_chave.toString() + chave_cortada;

            params = new HashMap<String, String>();
            params.put("reativar", "S");
            params.put("newkey", new_key_reativando);
            params.put("key_existing", licenca);
            params.put("emp_celularkey", celkey);

            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Method.POST, URL_GET_LICENCA, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        int sucesso_reativando = (Integer) response.get(TAG_SUCCESSO);
                        String mensagem_reativando = (String) response.get(TAG_MENSAGEM);

                        switch (sucesso_reativando) {

                            case 20:


                                dias_reativando = (Integer) response.get(TAG_DIAS);
                                cpf_reativando = (String) response.get(TAG_CPF);
                                numerocelular_reativando = (String) response.get(TAG_NUM_CEL);
                                email_reativando = (String) response.get(TAG_EMAIL);
                                TAG_CODIGO_USUARIO = (String) response.get(TAG_CODIGO_USUARIO_VENDEDOR);
                                String dias = String.valueOf(dias_reativando);

                                //  ENTRA AQUI SE NAO  HOUVER DIAS PRA USAR


                                finish();
                                empBean = new Empresa_SqliteBean();
                                empDao = new Empresa_SqliteDao(getApplicationContext());
                                empBean.setEmp_nome("esta empresa esta reativando a conta ");
                                empBean.setEmp_cpf(cpf_reativando);
                                empBean.setEmp_celularkey(celkey);
                                empBean.setEmp_numerocelular(numerocelular_reativando);
                                empBean.setEmp_totalemdias(dias);
                                empBean.setUsu_codigo("0");
                                empBean.setEmp_email(email_reativando);
                                empDao.GravaParamsEmpresa(empBean);
                                empBean = empDao.buscarEmpresa();

                                // atualizar cod vendedor
                                Configuracoes_SqliteBean confbean = new Configuracoes_SqliteBean();
                                Configuracoes_SqliteDao confdao = new Configuracoes_SqliteDao(getApplicationContext());

                                // atualizando o codigo do vendedor gravado na web
                                // para o sqlite
                                confbean.setUSU_CODIGO(Integer.valueOf(TAG_CODIGO_USUARIO));
                                confdao.atualiza_codigo_vendedor(confbean);


                                Intent p1 = new Intent(getBaseContext(), Principal.class);
                                p1.putExtra("mensagem", "faltam " + empBean.getEmp_totalemdias() + " dias para terminar sua licenca");
                                startActivity(p1);
                                Util.Mensagem(getBaseContext(), mensagem_reativando, Util.THEME_CMDV);
                                break;

                            case 30:


                                Util.Mensagem(getBaseContext(), mensagem_reativando, Util.THEME_CMDV);
                                break;

                            case 40:


                                Util.Mensagem(getBaseContext(), mensagem_reativando, Util.THEME_CMDV);
                                break;

                            case 50:


                                dias_reativando = (Integer) response.get(TAG_DIAS);
                                cpf_reativando = (String) response.get(TAG_CPF);
                                numerocelular_reativando = (String) response.get(TAG_NUM_CEL);
                                email_reativando = (String) response.get(TAG_EMAIL);
                                TAG_CODIGO_USUARIO = (String) response.get(TAG_CODIGO_USUARIO_VENDEDOR);


                                dias = String.valueOf(dias_reativando);
                                //  ENTRA AQUI SE HOUVER DIAS PRA USAR


                                empBean = new Empresa_SqliteBean();
                                empDao = new Empresa_SqliteDao(getApplicationContext());
                                empBean.setEmp_nome("resgatando " + dias + " dias de licenca");
                                empBean.setEmp_cpf(cpf_reativando);
                                empBean.setEmp_celularkey(celkey);
                                empBean.setEmp_numerocelular(numerocelular_reativando);
                                empBean.setEmp_totalemdias(dias);
                                empBean.setUsu_codigo("0");
                                empBean.setEmp_email(email_reativando);
                                empDao.GravaParamsEmpresa(empBean);
                                empBean = empDao.buscarEmpresa();


                                // atualizar cod vendedor
                                Configuracoes_SqliteBean confbean2 = new Configuracoes_SqliteBean();
                                Configuracoes_SqliteDao confda2 = new Configuracoes_SqliteDao(getApplicationContext());

                                // atualizando o codigo do vendedor gravado na web
                                // para o sqlite
                                confbean2.setUSU_CODIGO(Integer.valueOf(TAG_CODIGO_USUARIO));
                                confda2.atualiza_codigo_vendedor(confbean2);


                                Intent p2 = new Intent(getBaseContext(), Principal.class);
                                p2.putExtra("mensagem", "faltam " + empBean.getEmp_totalemdias() + " dias para terminar sua licenca");
                                startActivity(p2);
                                Util.Mensagem(getBaseContext(), mensagem_reativando, Util.THEME_CMDV);
                                break;
                            case 60:

                                Util.Mensagem(getBaseContext(), mensagem_reativando, Util.THEME_CMDV);
                                break;

                            case 70:

                                Util.Mensagem(getBaseContext(), mensagem_reativando, Util.THEME_CMDV);
                                break;

                        }

                    } catch (JSONException e) {
                        Log.i("script", "JSONException: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.Mensagem(getApplicationContext(), error.getMessage().toString(), Util.THEME_CMDV);
                }
            });

            request.setTag("tag");
            rq.add(request);

        }
    }

    private void declaraObjetos() {
        txt_licenca = (EditText) findViewById(R.id.txt_licenca);
    }

    private void pegaValoresFields() {
        licenca = txt_licenca.getText().toString();

        Random gerador_chave_numerica = new Random();
        Integer primeira_chave = gerador_chave_numerica.nextInt(15000);
        String chave = empBean.getEmp_celularkey() + licenca + empBean.getEmp_cpf() + empBean.getEmp_email() + new Date().getTime();
        String chave_cortada = Util.stringHexa(Util.gerarHash(chave, "MD5")).substring(0, 5);
        nova_chave_licenca = primeira_chave.toString() + chave_cortada;

        emp_celularkey = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        emp_cpf = empBean.getEmp_cpf();
        emp_email = empBean.getEmp_email();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
