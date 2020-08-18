package br.com.app.platinumapp.appvendas.Importacoes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;
import br.com.app.platinumapp.appvendas.Main.Applications;
import br.com.app.platinumapp.appvendas.Model.Cidades_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cidades_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
//import br.com.app.platinumapp.appvendas.Util.MaskEditText;
import br.com.app.platinumapp.appvendas.Util.Util;


public class CadastraCliente extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_MENSAGEM = "mensagem";
    private static final String LIMITE_CLIENTE_CADASTRO_APP = "500";
    private static String URL_CADASTRA_CLIENTE;
    private boolean isConnected;
    private String cli_nome;
    private String cli_fantasia;
    private String cli_endereco;
    private String cli_bairro;
    private String cli_cep;
    private String cid_codigo;
    private String cli_contato1;
    private String cli_contato2;
    private String cli_contato3;
    private String cli_nascimento;
    private String cli_cpfcnpj;
    private String cli_rginscricaoest;
    private String cli_limite;
    private String cli_email;
    private String cli_observacao;
    private String usu_codigo;
    private String cli_senha;
    private String cli_chave;
    private Button btn_gravarcliente;
    private TextInputEditText txt_cli_nome;
    private TextInputEditText txt_cli_fantasia;
    private TextInputEditText txt_cli_endereco;
    private TextInputEditText txt_cli_bairro;
    private TextInputEditText txt_cli_cep;
    private TextInputEditText txt_cid_codigo;
    private TextInputEditText txt_cli_contato1;
    private TextInputEditText txt_cli_contato2;
    private TextInputEditText txt_cli_contato3;
    private TextInputEditText txt_cli_nascimento;
    private TextInputEditText txt_cli_cpfcnpj;
    private TextInputEditText txt_cli_rginscricaoest;
    private TextInputEditText txt_cli_limite;
    private TextInputEditText txt_cli_email;
    private TextInputEditText txt_cli_observacao;
    private TextInputEditText txt_usu_codigo;
    private TextInputEditText txt_cli_senha;
    private String ID_CPF_EMPRESA;
    private Spinner spcidade;
    private Cliente_SqliteBean cliSqliteBean;
    private Cliente_SqliteDao cliSqliteDao;
    private Configuracoes_SqliteBean confBean;
    private Configuracoes_SqliteDao confDao;
    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;
    private Map<String, String> params;
    private JSONArray cli_array = null;
    private Integer CODIGO_CIDADE = 0;
    private Integer CODIGO_CLIENTE = 0;
    private Intent CODIGO_CLIENTE_INTENT;
    private Intent ALTERARCLIENTE_INTENT;
    private boolean ALTERAR_CLIENTE;
    private TextWatcher cep;
    private RequestQueue rq;
    private DatePickerDialog.OnDateSetListener data_inicial = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String mes = Util.RetornaMesEmString(monthOfYear);
            String dia = Util.RetornaDiaString(dayOfMonth);
            String data = dia + "/" + mes + "/" + String.valueOf(year);

            txt_cli_nascimento.setText("");
            txt_cli_nascimento.setText(data);

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadcliente);

        declaraObjetos();
        updateNetwork();




        URL_CADASTRA_CLIENTE = Util.getBaseUrl(getApplicationContext()) + "/json/cadastra_cliente.php";
        rq = Volley.newRequestQueue(CadastraCliente.this);

        CODIGO_CLIENTE_INTENT = getIntent();
        CODIGO_CLIENTE = CODIGO_CLIENTE_INTENT.getIntExtra("CLI_CODIGO", 0);

        ALTERARCLIENTE_INTENT = getIntent();
        ALTERAR_CLIENTE = ALTERARCLIENTE_INTENT.getBooleanExtra("ALTERAR", false);

        if (ALTERAR_CLIENTE) {
            btn_gravarcliente.setText("ALTERANDO CLIENTE");
            preenche_dados_cliente_TextInputEditText();
        } else {
            btn_gravarcliente.setText("CADASTRAR CLIENTE");
            if (!Util.IN_PRODUCAO) {
                seta_valores_teste();
            }
        }

        btn_gravarcliente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                isConnected = ConnectivityReceiver.isConnected();

                if (!validou_campos()) {
                    return;
                }

                if (isConnected && !ALTERAR_CLIENTE) {
                    cadastra_cliente_no_servidor__e__retorna_pro_android();
                }

                if (isConnected && ALTERAR_CLIENTE) {
                    grava_cliente_sqlite();
                }

                if (!isConnected && !ALTERAR_CLIENTE) {
                    grava_cliente_sqlite();
                }


            }

        });

    }

    public void cadastra_cliente_no_servidor__e__retorna_pro_android() {

        declaraObjetos();
        pega_valores_dos_campos();

        params = new HashMap<String, String>();
        params.put("emp_celularkey", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        params.put("cli_nome", cli_nome);
        params.put("cli_fantasia", cli_fantasia);
        params.put("cli_endereco", cli_endereco);

        params.put("cli_bairro", cli_bairro);
        params.put("cli_cep", cli_cep);
        params.put("cid_codigo", cid_codigo);

        params.put("cli_contato1", cli_contato1);
        params.put("cli_contato2", cli_contato2);
        params.put("cli_contato3", cli_contato3);

        params.put("cli_nascimento", cli_nascimento);
        params.put("cli_cpfcnpj", cli_cpfcnpj);
        params.put("cli_rginscricaoest", cli_rginscricaoest);

        params.put("cli_limite", cli_limite);
        params.put("cli_email", cli_email);
        params.put("cli_observacao", cli_observacao);

        params.put("usu_codigo", usu_codigo);
        params.put("cli_senha", cli_senha);
        params.put("cli_chave", "");

        params.put("ID_CPF_EMPRESA", ID_CPF_EMPRESA);

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_CADASTRA_CLIENTE, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    cli_array = response.getJSONArray("cli_array");

                    for (int i = 0; i < cli_array.length(); i++) {
                        JSONObject clienteObject = cli_array.getJSONObject(i);

                        Cliente_SqliteBean cliSqliteBean = new Cliente_SqliteBean();
                        Cliente_SqliteDao cliSqliteDao = new Cliente_SqliteDao(getApplicationContext());

                        String cli_codigo = clienteObject.getString(cliSqliteBean.CODIGO_DO_CLIENTE);
                        String cli_nome = clienteObject.getString(cliSqliteBean.NOME_DO_CLIENTE);
                        String cli_fantasia = clienteObject.getString(cliSqliteBean.NOME_FANTASIA);
                        String cli_endereco = clienteObject.getString(cliSqliteBean.ENDERECO_CLIENTE);

                        String cli_bairro = clienteObject.getString(cliSqliteBean.BAIRRO_CLIENTE);
                        String cli_cep = clienteObject.getString(cliSqliteBean.CEP_CIDADE);
                        String cid_codigo = clienteObject.getString(cliSqliteBean.CODIGO_CIDADE);

                        String cli_contato1 = clienteObject.getString(cliSqliteBean.CONTATO_CLIENTE1);
                        String cli_contato2 = clienteObject.getString(cliSqliteBean.CONTATO_CLIENTE2);
                        String cli_contato3 = clienteObject.getString(cliSqliteBean.CONTATO_CLIENTE3);

                        String cli_nascimento = clienteObject.getString(cliSqliteBean.DATA_NASCIMETO);
                        String cli_cpfcnpj = clienteObject.getString(cliSqliteBean.CPF_CNPJ_CLIENTE);
                        String cli_rginscricaoest = clienteObject.getString(cliSqliteBean.RG_INSCRICAO_ESTADUAL);

                        String cli_limite = clienteObject.getString(cliSqliteBean.LIMITE_CLIENTE);
                        String cli_email = clienteObject.getString(cliSqliteBean.EMAIL_CLIENTE);
                        String cli_observacao = clienteObject.getString(cliSqliteBean.OBSERVACAO);

                        String usu_codigo = clienteObject.getString(cliSqliteBean.CODIGO_VENDEDOR_USUARIO);
                        String cli_senha = clienteObject.getString(cliSqliteBean.SENHA_DO_CLIENTE);

                        String cli_chave = "";

                        String cli_enviado = "S";

                        cliSqliteBean.setCli_codigo(Integer.parseInt(cli_codigo));
                        cliSqliteBean.setCli_nome(cli_nome);
                        cliSqliteBean.setCli_fantasia(cli_fantasia);
                        cliSqliteBean.setCli_endereco(cli_endereco);

                        cliSqliteBean.setCli_bairro(cli_bairro);
                        cliSqliteBean.setCli_cep(cli_cep);
                        cliSqliteBean.setCid_codigo(Integer.parseInt(cid_codigo));

                        cliSqliteBean.setCli_contato1(cli_contato1);
                        cliSqliteBean.setCli_contato2(cli_contato2);
                        cliSqliteBean.setCli_contato3(cli_contato3);

                        cliSqliteBean.setCli_nascimento(cli_nascimento);
                        cliSqliteBean.setCli_cpfcnpj(cli_cpfcnpj);
                        cliSqliteBean.setCli_rginscricaoest(cli_rginscricaoest);

                        cliSqliteBean.setCli_limite(new BigDecimal(Double.parseDouble(cli_limite)).setScale(2,RoundingMode.HALF_UP));
                        cliSqliteBean.setCli_email(cli_email);
                        cliSqliteBean.setCli_observacao(cli_observacao);

                        cliSqliteBean.setUsu_codigo(Integer.parseInt(usu_codigo));
                        cliSqliteBean.setCli_senha(cli_senha);
                        cliSqliteBean.setCli_enviado(cli_enviado);
                        cliSqliteBean.setCli_chave(cli_chave);

                        cliSqliteDao.GravarCliente_Sqlite(cliSqliteBean);

                    }

                    finish();
                    Util.Mensagem(CadastraCliente.this, "CLIENTE CADASTRADO NO SERVIDOR", Util.THEME_CMDV);

                } catch (JSONException e) {
                    Log.i("Script", "JSONException: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.Mensagem(CadastraCliente.this, error.getMessage(), Util.THEME_CMDV);
            }
        });

        request.setTag("tag");
        rq.add(request);

    }

    private void grava_cliente_sqlite() {

        cliSqliteBean = new Cliente_SqliteBean();
        cliSqliteDao = new Cliente_SqliteDao(getApplicationContext());

        pega_valores_dos_campos();

        cliSqliteBean.setCli_nome(cli_nome);
        cliSqliteBean.setCli_fantasia(cli_fantasia);
        cliSqliteBean.setCli_endereco(cli_endereco);

        cliSqliteBean.setCli_bairro(cli_bairro);
        cliSqliteBean.setCli_cep(cli_cep);
        cliSqliteBean.setCid_codigo(Integer.parseInt(cid_codigo));

        cliSqliteBean.setCli_contato1(cli_contato1);
        cliSqliteBean.setCli_contato2(cli_contato2);
        cliSqliteBean.setCli_contato3(cli_contato3);

        cliSqliteBean.setCli_nascimento(cli_nascimento);
        cliSqliteBean.setCli_cpfcnpj(cli_cpfcnpj);
        cliSqliteBean.setCli_rginscricaoest(cli_rginscricaoest);

        cliSqliteBean.setCli_limite(new BigDecimal(cli_limite).setScale(2, RoundingMode.DOWN));
        cliSqliteBean.setCli_email(cli_email);
        cliSqliteBean.setCli_observacao(cli_observacao);

        cliSqliteBean.setUsu_codigo(Integer.parseInt(usu_codigo));
        cliSqliteBean.setCli_senha(cli_senha);
        cliSqliteBean.setCli_enviado("N");
        cliSqliteBean.setCli_chave(cli_chave);


        if (ALTERAR_CLIENTE) {

            cliSqliteBean.setCli_codigo(CODIGO_CLIENTE);
            cliSqliteDao.atualizaCliente(cliSqliteBean);

            Bundle bundle = new Bundle();
            Intent ListaClientesEditar = new Intent();
            ListaClientesEditar.putExtras(bundle);
            setResult(RESULT_OK, ListaClientesEditar);
            Util.Mensagem(getApplicationContext(), "CLIENTE ATUALIZADO COM SUCESSO", Util.THEME_CMDV);
            finish();

        } else {


            cliSqliteBean.setCli_codigo(Integer.parseInt(cli_chave));
            cliSqliteDao.GravarCliente_Sqlite(cliSqliteBean);
            Bundle bundle = new Bundle();
            Intent ListaClientesEditar = new Intent();
            ListaClientesEditar.putExtras(bundle);
            setResult(RESULT_OK, ListaClientesEditar);
            Util.Mensagem(getApplicationContext(), "CLIENTE CADASTRADO COM SUCESSO", Util.THEME_CMDV);
            finish();

        }


    }

    private void preenche_dados_cliente_TextInputEditText() {

        Cliente_SqliteBean cliBean = new Cliente_SqliteBean();
        cliBean = new Cliente_SqliteDao(getApplicationContext()).BuscarClientePorCli_codigo(CODIGO_CLIENTE);

        if (cliBean != null) {
            declaraObjetos();
            txt_cli_senha.setText(cliBean.getCli_senha().toString());
            txt_cli_nome.setText(cliBean.getCli_nome().toString().trim());
            txt_cli_fantasia.setText(cliBean.getCli_fantasia().toString().trim());
            txt_cli_endereco.setText(cliBean.getCli_endereco().toString().trim());
            txt_cli_bairro.setText(cliBean.getCli_bairro().toString().trim());
            txt_cli_cep.setText(cliBean.getCli_cep().toString().trim());
            txt_cli_contato1.setText(cliBean.getCli_contato1().toString().trim());
            txt_cli_contato2.setText(cliBean.getCli_contato2().toString().trim());
            txt_cli_contato3.setText(cliBean.getCli_contato3().toString().trim());
            txt_cli_nascimento.setText(cliBean.getCli_nascimento());
            txt_cli_cpfcnpj.setText(cliBean.getCli_cpfcnpj().toString().trim());
            txt_cli_rginscricaoest.setText(cliBean.getCli_rginscricaoest().toString().trim());
            txt_cli_limite.setText(cliBean.getCli_limite().toString());
            txt_cli_email.setText(cliBean.getCli_email().toString().trim());
            txt_cli_observacao.setText(cliBean.getCli_observacao().toString().trim());

        }


    }

    private void seta_valores_teste() {
        declaraObjetos();
        txt_cli_nome.setText("Maria josé da silva santos");
        txt_cli_fantasia.setText("Maria josé da silva santos");
        txt_cli_endereco.setText("Maria josé da silva santos");
        txt_cli_bairro.setText("Maria josé da silva santos");
        txt_cli_cep.setText("65765435");
        txt_cli_cpfcnpj.setText("22124254812");
        txt_cli_rginscricaoest.setText("22124254812");
        txt_cli_senha.setText("123456");
    }

    private void pega_valores_dos_campos() {

        cli_nome = txt_cli_nome.getText().toString().trim().toLowerCase();
        cli_fantasia = txt_cli_fantasia.getText().toString().trim().toLowerCase();
        cli_endereco = txt_cli_endereco.getText().toString().trim().toLowerCase();
        cli_bairro = txt_cli_bairro.getText().toString().trim().toLowerCase();
        cli_cep = txt_cli_cep.getText().toString().trim();
        cli_contato1 = txt_cli_contato1.getText().toString().trim();
        cli_contato2 = txt_cli_contato2.getText().toString().trim();
        cli_contato3 = txt_cli_contato3.getText().toString().trim();
        cli_cpfcnpj = txt_cli_cpfcnpj.getText().toString().trim();
        cli_rginscricaoest = txt_cli_rginscricaoest.getText().toString().trim();
        String data = txt_cli_nascimento.getText().toString().trim();
        if (data.isEmpty() || data.length() <= 0) {
            cli_nascimento = "1980-01-01";
        } else {
            cli_nascimento = Util.FormataDataAAAAMMDD(data).trim();
        }
        cli_email = txt_cli_email.getText().toString().trim().toLowerCase();
        cli_observacao = txt_cli_observacao.getText().toString().trim().toLowerCase();
        cid_codigo = CODIGO_CIDADE.toString();
        confBean = new Configuracoes_SqliteBean();
        confDao = new Configuracoes_SqliteDao(getApplicationContext());
        confBean = confDao.BuscaParamentrosEmpresa();
        usu_codigo = confBean.getUSU_CODIGO().toString();
        empBean = empDao.buscarEmpresa();
        ID_CPF_EMPRESA = empBean.getEmp_cpf().trim();
        cli_senha = txt_cli_senha.getText().toString().trim();

        Random gerador = new Random();
        Integer cod = gerador.nextInt(100000);


        if (ALTERAR_CLIENTE) {
            Cliente_SqliteBean cliBean = new Cliente_SqliteBean();
            cli_limite = txt_cli_limite.getText().toString().trim();
            cli_chave = cod.toString().trim();

        } else {
            cli_limite = LIMITE_CLIENTE_CADASTRO_APP;

            // só envia a chave aleatoria se tiver cadastrando o cliente em off-line
            if (!isConnected) {
                cli_chave = cod.toString().trim();
            } else {
                cli_chave = "";
            }
        }


    }

    private boolean validou_campos() {

        declaraObjetos();
        pega_valores_dos_campos();

        if (cli_senha.length() <= 4) {
            txt_cli_senha.setError("informe a senha de 5 digitos");
            txt_cli_senha.requestFocus();
            return false;
        }

        if (cli_nome.length() <= 5) {
            txt_cli_nome.setError("Nome invalido...");
            txt_cli_nome.requestFocus();
            return false;
        }

        if (cli_fantasia.length() <= 5) {
            txt_cli_fantasia.setError("Nome fantasia invalido...");
            txt_cli_fantasia.requestFocus();
            return false;
        }

        if (cli_endereco.length() <= 5) {
            txt_cli_endereco.setError("Endereco invalido...");
            txt_cli_endereco.requestFocus();
            return false;
        }

        if (cli_bairro.length() <= 5) {
            txt_cli_bairro.setError("Bairro invalido...");
            txt_cli_bairro.requestFocus();
            return false;
        }

        if (cli_cep.length() < 7) {
            txt_cli_cep.setError("CEP invalido...");
            txt_cli_cep.requestFocus();
            return false;
        }

        if (cli_contato1.length() <= 7) {
            txt_cli_contato1.setError("Contato invalido...");
            txt_cli_contato1.requestFocus();
            return false;
        }


        // validacao do cnpj e/ou cnpj
        if (cli_cpfcnpj.length() == 11 || cli_cpfcnpj.length() == 14) {

            if (cli_cpfcnpj.length() == 11) {
                if (!Util.validaCPF(cli_cpfcnpj)) {
                    txt_cli_cpfcnpj.setError("cpf invalido");
                    txt_cli_cpfcnpj.requestFocus();
                    return false;
                }
            }

            if (cli_cpfcnpj.length() == 14) {
                if (!Util.validaCNPJ(cli_cpfcnpj)) {
                    txt_cli_cpfcnpj.setError("CNPJ invalido");
                    txt_cli_cpfcnpj.requestFocus();
                    return false;
                }
            }

        } else {
            txt_cli_cpfcnpj.setError("CNPJ invalido");
            txt_cli_cpfcnpj.requestFocus();
            return false;
        }


        if (cli_rginscricaoest.length() <= 7) {
            txt_cli_rginscricaoest.setError("RG / INSC. EST invalido...");
            txt_cli_rginscricaoest.requestFocus();
            return false;
        }

        if (!Util.validaremail(cli_email)) {
            txt_cli_email.setError("E-mail invalido...");
            txt_cli_email.requestFocus();
            return false;
        }

        return true;
    }

    private void declaraObjetos() {

        btn_gravarcliente = (Button) findViewById(R.id.btn_gravarcliente);
        txt_cli_nome = (TextInputEditText) findViewById(R.id.txtcli_nome);
        txt_cli_fantasia = (TextInputEditText) findViewById(R.id.txtcli_fantasia);
        txt_cli_endereco = (TextInputEditText) findViewById(R.id.txtcli_endereco);
        txt_cli_bairro = (TextInputEditText) findViewById(R.id.txtcli_bairro);
        txt_cli_cep = (TextInputEditText) findViewById(R.id.txtcli_cep);
        txt_cli_limite = (TextInputEditText) findViewById(R.id.txtcli_limite);
        txt_cli_email = (TextInputEditText) findViewById(R.id.txtcli_email);
        txt_cli_observacao = (TextInputEditText) findViewById(R.id.txtcli_observacao);
        txt_cli_senha = (TextInputEditText) findViewById(R.id.txtcli_senha);
        txt_cli_contato1 = (TextInputEditText) findViewById(R.id.txtcli_contato1);
        txt_cli_contato2 = (TextInputEditText) findViewById(R.id.txtcli_contato2);
        txt_cli_contato3 = (TextInputEditText) findViewById(R.id.txtcli_contato3);
        txt_cli_nascimento = (TextInputEditText) findViewById(R.id.txtcli_nascimento);
        txt_cli_cpfcnpj = (TextInputEditText) findViewById(R.id.txtcli_cpfcnpj);
        txt_cli_rginscricaoest = (TextInputEditText) findViewById(R.id.txtcli_rginscricaoest);
        spcidade = (Spinner) findViewById(R.id.spcidade);


        empBean = new Empresa_SqliteBean();
        empDao = new Empresa_SqliteDao(CadastraCliente.this);

        Cidades_SqliteDao cDao = new Cidades_SqliteDao(getApplicationContext());
        ArrayAdapter<Cidades_SqliteBean> cidades_adapter = new ArrayAdapter<Cidades_SqliteBean>(this, android.R.layout.simple_spinner_item, cDao.ListaCidades());
        spcidade.setAdapter(cidades_adapter);

        spcidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View arg1, int arg2, long arg3) {
                Cidades_SqliteBean cid = (Cidades_SqliteBean) parent.getSelectedItem();
                CODIGO_CIDADE = cid.getCid_codigo();
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
            }
        });


    }

    public void data1(View view) {
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        if (id == 0) {
            return new DatePickerDialog(this, data_inicial, ano, mes, dia);
        }

        return null;
    }

    private void updateNetwork() {
        isConnected = ConnectivityReceiver.isConnected();

        String label = "";

        if (isConnected && !ALTERAR_CLIENTE) {
            label = "Salvar Cliente no servidor";
        }

        if (isConnected && ALTERAR_CLIENTE) {
            label = "Editar Cliente no celular";
        }

        if (!isConnected && !ALTERAR_CLIENTE) {
            label = "Salvar Cliente no celular";
        }


        btn_gravarcliente.setText(label);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNetwork();
    }

    @Override
    public void onResume() {
        super.onResume();
        Applications.getInstance().setConnectivityListener(this);
        updateNetwork();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d("script", "onConnectionFailed:" + isConnected);
        btn_gravarcliente.setText(isConnected ? "Registrar cliente no servidor" : "Registrar cliente no celular");
    }
}
