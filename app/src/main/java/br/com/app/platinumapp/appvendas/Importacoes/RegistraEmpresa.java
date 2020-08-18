package br.com.app.platinumapp.appvendas.Importacoes;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
//https://stackoverflow.com/questions/36309169/classnotfoundexception-for-signinconfiguration-when-signing-in-with-google
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;
import br.com.app.platinumapp.appvendas.Main.Applications;
import br.com.app.platinumapp.appvendas.Main.Principal;
import br.com.app.platinumapp.appvendas.Model.Cidades_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cidades_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

public class RegistraEmpresa extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        View.OnClickListener {

    // android:name=".Importacoes.RegistraEmpresa"

    public static final int REQUEST_READ_PHONE_STATE = 2522;
    public static final int MENU1 = Menu.FIRST + 1;
    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_MENSAGEM = "mensagem";
    private static final String TAG = "script";
    private static final int RC_SIGN_IN = 9001;
    private static String URL_REGISTRA_EMPRESA;
    private String emp_nome;
    private String emp_cpf;
    private String emp_licenca;
    private String emp_celularkey;
    private String usu_codigo;
    private String emp_totalemdias;
    private String emp_numerocelular;
    private String nome_vendedor;
    private String emp_email;
    private EditText txt_emp_cpf;
    private EditText txtnome_empresa;
    private EditText txtemp_email;
    private EditText txtemp_numerocelular;
    private EditText txtNomeVendedor;
    private Button btRegistrarEmpresa;
    private RequestQueue rq;
    private Map<String, String> params;
    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;
    private JSONArray usuario_array = null;
    //   private GoogleApiClient mGoogleApiClient;
    private boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registra_empresa);

        declaraObjetos();
        inicializa_empresa_sqlite();
        updateNetwork();


            txtemp_email.setText("usuario@gmail.com");
            txt_emp_cpf.setText("17984958050");
            txtemp_numerocelular.setText("1699432345");
            txtnome_empresa.setText("CMDV-PRODUTOS-LA");
            txtNomeVendedor.setText("vendedor1");



        new Cidades_SqliteDao(this).grava_cidade(new Cidades_SqliteBean(99009, "CIDADE_PADRAO", "UF"));




/*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(RegistraEmpresa.this, RC_SIGN_IN, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        setGooglePlusButtonText(signInButton, "Buscar meu e-mail no celular");
*/
        rq = Volley.newRequestQueue(RegistraEmpresa.this);
        URL_REGISTRA_EMPRESA = Util.getBaseUrl(getApplicationContext()) + "/json/registra_empresa.php";

        empDao = new Empresa_SqliteDao(this);
        empBean = new Empresa_SqliteBean();
        empBean = empDao.buscarEmpresa();


        // verifica se tem empresa no sqlite
        if (empBean != null) {
            int dias = Integer.parseInt(empBean.getEmp_totalemdias());

            if (dias > 0) {
                finish();
                Intent Principal = new Intent(getBaseContext(), Principal.class);
                Principal.putExtra("mensagem", "faltam ( " + empBean.getEmp_totalemdias() + " )  dias para terminar sua licenca");
                startActivity(Principal);
            }

            if (dias <= 0) {
                finish();
                Intent Pedir_chave_de_licenca = new Intent(this, Pedir_chave_de_licenca.class);
                startActivity(Pedir_chave_de_licenca);
            }
        }


    }


    public void registrar(View v) {

        if (!validouCamposnull()) {
            return;
        }

        if (!checkConnection()) {
            Util.Mensagem(getApplicationContext(), "Sem conexão com internet", Util.THEME_CMDV);
            return;
        }

        registra_empresa_na_web();


    }

    private boolean checkConnection() {
        return isConnected = ConnectivityReceiver.isConnected();
    }

    private void inicializa_empresa_sqlite() {
        Configuracoes_SqliteBean confbean = new Configuracoes_SqliteBean();
        Configuracoes_SqliteDao confdao = new Configuracoes_SqliteDao(this);
        confbean.setUSU_CODIGO(0);
        confbean.setIMPORTAR_TODOS_CLIENTES("S");
        confbean.setIP_SERVER("https://cmdv.sistemaseapps.com.br");
        confbean.setDESCONTO_VENDEDOR(BigDecimal.ZERO);
        confbean.setJUROS_VENDA_PRAZO(BigDecimal.ZERO);
        confbean.setVENDER_SEM_ESTOQUE("S");
        confbean.setPEDIR_SENHA_NA_VENDA("N");
        confbean.setPERMITIR_VENDER_ACIMA_DO_LIMITE("N");
        confbean.setNOME_VENDEDOR("NULL");
        confdao.gravarConfiguracoes(confbean);
    }

    public boolean onCreateOptionsMenu(Menu options) {
        options.add(0, MENU1, 0, "Configurar Servidor");
        return super.onCreateOptionsMenu(options);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU1:
                finish();
                Intent Trocar_ip_servidor = new Intent(getBaseContext(), Trocar_ip_servidor.class);
                startActivity(Trocar_ip_servidor);
                return true;
        }
        return false;
    }


    public void registra_empresa_na_web() {

        declaraObjetos();
        pegaValoresdoscampos();
        params = new HashMap<String, String>();

        params.put("emp_nome", emp_nome);
        params.put("emp_cpf", emp_cpf);
        params.put("emp_licenca", emp_licenca);
        params.put("emp_celularkey", emp_celularkey);
        params.put("usu_codigo", usu_codigo);
        params.put("emp_numerocelular", emp_numerocelular);
        params.put("emp_email", emp_email);
        params.put("nome_vendedor", nome_vendedor);

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_REGISTRA_EMPRESA, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    int sucesso = (Integer) response.get(TAG_SUCCESSO);
                    String mensagem = (String) response.get(TAG_MENSAGEM);

                    switch (sucesso) {

                        case 1:
                            // o telefone ja foi registrado na web , atulizar licenca
                            finish();
                            Util.Mensagem(getApplicationContext(), "este telefone ja foi registrado...", Util.THEME_CMDV);
                            Intent Pedir_chave_de_licenca = new Intent(getBaseContext(), Pedir_chave_de_licenca.class);
                            startActivity(Pedir_chave_de_licenca);
                            break;

                        case 2:

                            usuario_array = response.getJSONArray("usuario_array");
                            for (int i = 0; i < usuario_array.length(); i++) {
                                JSONObject vendedor = usuario_array.getJSONObject(i);
                                Configuracoes_SqliteBean confbean = new Configuracoes_SqliteBean();
                                Configuracoes_SqliteDao confdao = new Configuracoes_SqliteDao(getApplicationContext());
                                confbean.setUSU_CODIGO(vendedor.getInt("usu_codigo"));
                                confbean.setNOME_VENDEDOR(vendedor.getString("nome_vendedor"));
                                confbean.setDESCONTO_VENDEDOR(new BigDecimal(vendedor.getDouble("usu_desconto")));
                                confdao.atualiza_dados_vendedor(confbean);
                            }

                            // registrou entao mando o usuario para a tela principal do sistema

                            Intent Principal = new Intent(getBaseContext(), Principal.class);
                            Principal.putExtra("mensagem", mensagem);
                            empBean = new Empresa_SqliteBean();
                            empDao = new Empresa_SqliteDao(getApplicationContext());
                            empBean.setEmp_nome(emp_nome);
                            empBean.setEmp_cpf(emp_cpf);
                            empBean.setEmp_celularkey(emp_celularkey);
                            empBean.setEmp_totalemdias(emp_totalemdias);
                            empBean.setUsu_codigo(usu_codigo);
                            empBean.setEmp_numerocelular(emp_numerocelular);
                            empBean.setEmp_email(emp_email);
                            empDao.GravaParamsEmpresa(empBean);
                            startActivity(Principal);
                            finish();
                            break;

                        case 3:
                            Util.Mensagem(getApplicationContext(), mensagem, Util.THEME_CMDV);
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

            }
        });

        request.setTag("tag");
        rq.add(request);

    }


    private boolean validouCamposnull() {

        declaraObjetos();
        pegaValoresdoscampos();

        if (emp_cpf.length() <= 0) {
            txt_emp_cpf.setError("cpf invalido");
            txt_emp_cpf.requestFocus();
            return false;
        }

        if (emp_cpf.length() <= 10) {
            txt_emp_cpf.setError("cpf invalido");
            txt_emp_cpf.requestFocus();
            return false;
        }

        if (emp_cpf.length() == 11) {
            if (!Util.validaCPF(emp_cpf)) {
                txt_emp_cpf.setError("cpf invalido");
                txt_emp_cpf.requestFocus();
                return false;
            }
        }


        if (emp_numerocelular.length() < 11) {
            txtemp_numerocelular.setError("formato incorreto use : xx991990055");
            txtemp_numerocelular.requestFocus();
            return false;
        }

        if (!Util.validaremail(emp_email)) {
            txtemp_email.setError("E-mail invalido...");
            txtemp_email.requestFocus();
            return false;
        }

        if (txtnome_empresa.length() < 4) {
            txtnome_empresa.setError("informe o nome da empresa");
            txtnome_empresa.requestFocus();
            return false;
        }

        if (txtNomeVendedor.length() < 4) {
            txtNomeVendedor.setError("informe o nome do vendedor");
            txtNomeVendedor.requestFocus();
            return false;
        }


        return true;
    }

    public void declaraObjetos() {
        btRegistrarEmpresa = (Button) findViewById(R.id.btregistraempresa);
        txt_emp_cpf = (EditText) findViewById(R.id.txtemp_cpf);
        txtnome_empresa = (EditText) findViewById(R.id.txtnome_empresa);
        txtemp_email = (EditText) findViewById(R.id.txtemp_email);
        txtemp_numerocelular = (EditText) findViewById(R.id.txtemp_numerocelular);
        txtNomeVendedor = (EditText) findViewById(R.id.txtNomeVendedor);
        // txtemp_numerocelular.addTextChangedListener(MaskEditText.insert(MaskEditText.TELEFONE_CELULAR, txtemp_numerocelular));
    }

    public void pegaValoresdoscampos() {

        // ENTRADA DE DADOS
        emp_celularkey = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        emp_numerocelular = txtemp_numerocelular.getText().toString()
                .replace(" ", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "").trim();
        emp_cpf = txt_emp_cpf.getText().toString();
        emp_email = txtemp_email.getText().toString();
        emp_nome = txtnome_empresa.getText().toString().toLowerCase().trim();
        nome_vendedor = txtNomeVendedor.getText().toString().toLowerCase().trim();
        Random gerador_chave_numerica = new Random();
        Integer primeira_chave = gerador_chave_numerica.nextInt(15000);
        String chave = emp_email + emp_numerocelular + emp_cpf + primeira_chave.toString();
        String chave_cortada = Util.stringHexa(Util.gerarHash(chave, "MD5")).substring(0, 5);
        emp_licenca = primeira_chave.toString() + chave_cortada;
        usu_codigo = "0";
        emp_totalemdias = "10";

    }

    @Override
    public void onStop() {
        super.onStop();
        rq.cancelAll("tag");
        //mGoogleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        Applications.getInstance().setConnectivityListener(this);
        updateNetwork();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        btRegistrarEmpresa.setEnabled(isConnected);
        btRegistrarEmpresa.setText(isConnected ? "Registrar" : "Sem Internet");
    }


    private void updateNetwork() {
        isConnected = ConnectivityReceiver.isConnected();
        btRegistrarEmpresa.setEnabled(isConnected);
        btRegistrarEmpresa.setText(isConnected ? "Registrar" : "Sem Internet");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
        updateNetwork();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // case R.id.sign_in_button:
            //     Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            //    startActivityForResult(signInIntent, RC_SIGN_IN);
            //    break;
        }
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {


            GoogleSignInAccount account = result.getSignInAccount();

            if (account != null) {
                Log.d("script", "getDisplayName.: " + account.getDisplayName().toString());
                Log.d("script", "getEmail.: " + account.getEmail().toString());
                Log.d("script", "getFamilyName.: " + account.getFamilyName().toString());
                Log.d("script", "getGivenName.: " + account.getGivenName().toString());
                Log.d("script", "getAccount.: " + account.getAccount().toString());
                Log.d("script", "getPhotoUrl.: " + account.getPhotoUrl().toString());
                Log.d("script", "getId.: " + account.getId().toString());
                txtemp_email.setText(account.getEmail().toString());
                txtNomeVendedor.setText(account.getDisplayName().toString());
            }

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void setGooglePlusButtonText(SignInButton signInButton,
                                         String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(18);
                tv.setTextColor(Color.RED);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                return;
            }
        }
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    */
}
