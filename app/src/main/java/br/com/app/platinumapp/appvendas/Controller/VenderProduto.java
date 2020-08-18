package br.com.app.platinumapp.appvendas.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.app.platinumapp.appvendas.Adapters.VenderProdutoAdapterRecyclerView;
import br.com.app.platinumapp.appvendas.FormaPagamento.ObjetoVenda;
import br.com.app.platinumapp.appvendas.FormaPagamento.PagamentoMensal;
import br.com.app.platinumapp.appvendas.FormaPagamento.PagamentoQuinzenal;
import br.com.app.platinumapp.appvendas.FormaPagamento.PagamentoSemanal;
import br.com.app.platinumapp.appvendas.FormaPagamento.iPagamento;
import br.com.app.platinumapp.appvendas.Main.Principal;
import br.com.app.platinumapp.appvendas.Model.Cheque_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.ConfPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.ConfPagamento_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.VendaC_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.VendaDAO;
import br.com.app.platinumapp.appvendas.Model.VendaD_SqliteBeanTEMP;
import br.com.app.platinumapp.appvendas.Model.VendaD_SqliteDaoTEMP;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;
import br.com.app.platinumapp.appvendas.Util.RecyclerTouchListener;


public class VenderProduto extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    //********************************************************


    public static final int TERCA_FEIRA = 2;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    public static BigDecimal SUB_TOTAL = BigDecimal.ZERO;
    public static String CLI_NOME;
    public static Integer CLI_CODIGO;
    //*******************************************************
    public static String NOME_FANTASIA;
    public static String ENDERECO_CLIENTE;
    public static String BAIRRO_CLIENTE;
    public static String CEP_CIDADE;
    public static String CONTATO_CLIENTE1;
    public static String CONTATO_CLIENTE2;
    public static String CONTATO_CLIENTE3;
    public static String DATA_NASCIMETO;
    public static String CPF_CNPJ_CLIENTE;
    public static String RG_INSCRICAO_ESTADUAL;
    public static BigDecimal LIMITE_CLIENTE;
    public static String EMAIL_CLIENTE;
    public static Integer CODIGO_VENDEDOR_USUARIO;
    public static String CLIENTE_ENVIADO;
    public static String CHAVE_CLIENTE;
    private MaterialDialog mMaterialDialog;
    private boolean permissionIsGranted = false;
    //************ GPS **************
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private LocationManager mLocationManager;
    private Location mlocation;
    private Button btn_configlocation;

    private List<VendaD_SqliteBeanTEMP> lista_de_todos_items_da_venda = new ArrayList<>();
    private Produto_SqliteDao prdDao;
    private Produto_SqliteBean prdBean;
    private VendaC_SqliteBean venBean_C;
    private ConfPagamento_SqliteDao confPagDao;
    private ConfPagamento_SqliteBean cPagBean;
    private BigDecimal VALOR_DA_PARCELA = BigDecimal.ZERO;
    private Intent CODIGO_CLIENTE_INTENT;
    private TextView vendas_txvInfo;
    private TextView vendas_dataPrevEntrega;
    private TextView vendas_txvSubTotal;
    private TextView vendas_formadepagamentoescolhida;
    private TextView vendas_parcelascalculadas;
    private RecyclerView mRecyclerViewItensVendidos;
    private CheckBox vendas_check_distribuirvalorparcelas;
    private Integer CODIGO_CLIENTE;
    private boolean VENDA_COM_ENTRADA;
    private String CHAVE_VENDA;
    private String PREVISAO_ENTREGA;
    private Configuracoes_SqliteBean conf;
    private VenderProdutoAdapterRecyclerView mVenderProdutoAdapter;
    private EditText txt_dlg_pedirSenha;
    private View positiveAction;
    private String SENHA_CADASTRADA;
    private String SENHA_DIGITADA;


    //*** datepicker dialog
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venderproduto_recyclerview);
        declaraObjetos();
        recuperaValoresIntent();
        atualiza_listview_com_os_items_da_venda();
        busca_informacoes_do_cliente(CODIGO_CLIENTE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            btn_configlocation.setVisibility(View.GONE);
        }


        btn_configlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
                } else {
                    permissionIsGranted = true;
                }

            }
        });


        final ImageButton btPesquisarProdutoVenda = (ImageButton) findViewById(R.id.btPesquisarProdutoVenda);
        btPesquisarProdutoVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent AddProdutoVenda = new Intent(getBaseContext(), AddProdutoVenda.class);
                AddProdutoVenda.putExtra("CODIGO_CLIENTE", CODIGO_CLIENTE);
                startActivityForResult(AddProdutoVenda, 2017);

            }
        });


        mRecyclerViewItensVendidos.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerViewItensVendidos, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                confirma_exclusao_do_item(lista_de_todos_items_da_venda.get(position));
            }
        }));

    }


    private void inserir_formapagamento() {
        lista_de_todos_items_da_venda = new VendaD_SqliteDaoTEMP(getApplicationContext()).busca_todos_items_na_vendad();
        // verifica se existe venda
        if (lista_de_todos_items_da_venda.size() > 0) {
            new ConfPagamento_SqliteDao(getApplicationContext()).p_excluir_confpagamento();
            Intent FormaDePagamento = new Intent(getBaseContext(), FormaDePagamento.class);
            FormaDePagamento.putExtra("CODIGO_CLIENTE", CODIGO_CLIENTE);
            FormaDePagamento.putExtra("SUB_TOTAL", SUB_TOTAL.doubleValue());
            startActivityForResult(FormaDePagamento, 2017);
        } else {
            Util.Mensagem(VenderProduto.this, "sem produtos adicionados na venda...", Util.THEME_CMDV);
        }
    }

    private void setDateTimeField() {

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataescolhida = Calendar.getInstance();
                dataescolhida.set(year, monthOfYear, dayOfMonth);

                vendas_dataPrevEntrega.setText("");
                vendas_dataPrevEntrega.setText("ENTREGA : " + dateFormatter.format(dataescolhida.getTime()));
                PREVISAO_ENTREGA = dateFormatter.format(dataescolhida.getTime());
                Log.i("script", " data escolhida .: " + PREVISAO_ENTREGA);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2017) {
            if (resultCode == Activity.RESULT_OK) {
                atualiza_listview_com_os_items_da_venda();
            }
        }
    }

    public void finalizar_venda_com_senha_cliente_dialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.dlg_pedir_senha)
                .customView(R.layout.dlg_pedir_senha_cliente, true)
                .positiveText(R.string.dlg_positiveText_confirmar)
                .negativeText(R.string.dlg_negativeText_cancelar)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        txt_dlg_pedirSenha = (EditText) dialog.getCustomView().findViewById(R.id.txt_dlg_pedirSenha);

                        if (gravaVenda() > 0) {

                            cPagBean = confPagDao.p_busca_confpagamento();

                            // atualiza a chave da venda no cheque
                            if (cPagBean.getPag_recebeucom_din_chq_cart().equals("CHEQUE")) {
                                new Cheque_SqliteDao(getApplicationContext()).atualiza_vendac_chave_no_cheque(CHAVE_VENDA, CODIGO_CLIENTE, Util.DataSemHorasUSA());
                            }

                            // gerando as parcelas da venda
                            gerar_receber(vendas_txvSubTotal.getText().toString().trim(), cPagBean);

                            // atualizado a chave da venda na configuraco de pagameto
                            confPagDao.AtualizaVendac_chaveConfPagamento(CHAVE_VENDA);

                            // alterar estoque  de produtos aqui.

                        }

                    }
                })
                .build();

        EditText txt_dlg_pedirSenha = (EditText) dialog.getCustomView().findViewById(R.id.txt_dlg_pedirSenha);


        // determina qual o botao de acao positiva , botao OK
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

        txt_dlg_pedirSenha.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        SENHA_DIGITADA = s.toString().trim();

                        if (SENHA_DIGITADA.equals(SENHA_CADASTRADA)) {
                            positiveAction.setEnabled(true);
                        } else {
                            positiveAction.setEnabled(false);
                        }

                        //positiveAction.setEnabled(s.toString().trim().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


        int widgetColor = ThemeSingleton.get().widgetColor;

        MDTintHelper.setTint(
                (EditText) txt_dlg_pedirSenha,
                widgetColor == 0 ? ContextCompat.getColor(this, R.color.colorAccent) : widgetColor);

        dialog.show();
        positiveAction.setEnabled(false); // disabled by default


    }

    public void finaliza_venda_sem_senha() {
        if (gravaVenda() > 0) {
            cPagBean = confPagDao.p_busca_confpagamento();
            // atualiza a chave da venda no cheque
            if (cPagBean.getPag_recebeucom_din_chq_cart().equals("CHEQUE")) {
                new Cheque_SqliteDao(getApplicationContext()).atualiza_vendac_chave_no_cheque(CHAVE_VENDA, CODIGO_CLIENTE, Util.DataSemHorasUSA());
            }
            // gerando as parcelas da venda
            gerar_receber(vendas_txvSubTotal.getText().toString().trim(), cPagBean);
            // atualizado a chave da venda na configuraco de pagameto
            confPagDao.AtualizaVendac_chaveConfPagamento(CHAVE_VENDA);
            // alterar estoque  de produtos aqui.
        }
    }


    public void FinalizarVenda(View v) {

        if (pode_fechar_venda()) {
            if (vender_acima_limite()) {
                if (conf.getPEDIR_SENHA_NA_VENDA().equals("S")) {
                    finalizar_venda_com_senha_cliente_dialog();
                } else {
                    finaliza_venda_sem_senha();
                }
            }
        }
    }


    private void gerar_receber(final String valor_total_venda, ConfPagamento_SqliteBean cPagBean) {

        VENDA_COM_ENTRADA = false;

        if (cPagBean.getPag_sementrada_comentrada().equals("true")) {
            VENDA_COM_ENTRADA = true;

        }

        Calendar calendario = Calendar.getInstance(new Locale("pt", "BR"));
        Date data1 = calendario.getTime();
        SimpleDateFormat data_venda = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dataporextenso = DateFormat.getDateInstance(DateFormat.FULL);

        Receber_SqliteBean rec4 = new Receber_SqliteBean();
        rec4.setRec_num_parcela(1);
        rec4.setRec_cli_codigo(CODIGO_CLIENTE);
        rec4.setRec_cli_nome(CLI_NOME);
        rec4.setRec_datamovimento(data_venda.format(data1));
        rec4.setRec_valorreceber(new BigDecimal(valor_total_venda));
        rec4.setRec_datavencimento(Util.DataSemHorasUSA());
        rec4.setRec_datavencimento_extenso(dataporextenso.format(data1));
        rec4.setRec_data_que_pagou(data_venda.format(data1));
        rec4.setRec_recebeu_com(cPagBean.getPag_recebeucom_din_chq_cart());
        rec4.setRec_parcelas_cartao(cPagBean.getPag_parcelas_cartao());
        rec4.setVendac_chave(CHAVE_VENDA);
        rec4.setRec_enviado("N");

        ObjetoVenda obj_venda = new ObjetoVenda();
        obj_venda.setCtx(getApplicationContext());
        obj_venda.setDistribuir_valor_entrada_nas_parcelas(vendas_check_distribuirvalorparcelas.isChecked());
        obj_venda.setVenda_com_entrada(VENDA_COM_ENTRADA);
        obj_venda.setQtde_veses(cPagBean.getPag_parcelas_normal());
        obj_venda.setRecebimentoDiaSemana(TERCA_FEIRA);
        obj_venda.setValor(valor_total_venda);
        obj_venda.setValor_pago_primeira_parcela(cPagBean.getPag_valor_recebido().toString());
        obj_venda.setRec(rec4);

        if (cPagBean.getPag_tipo_pagamento().equals("AVISTA")) {

            Receber_SqliteBean rec = new Receber_SqliteBean();
            Receber_SqliteDao rDao = new Receber_SqliteDao(getApplicationContext());

            rec.setRec_num_parcela(1);
            rec.setRec_cli_codigo(CODIGO_CLIENTE);
            rec.setRec_cli_nome(CLI_NOME);

            rec.setVendac_chave(CHAVE_VENDA);
            rec.setRec_datamovimento(data_venda.format(data1));
            rec.setRec_valorreceber(new BigDecimal(valor_total_venda));
            rec.setRec_datavencimento(Util.DataSemHorasUSA());

            rec.setRec_datavencimento_extenso(dataporextenso.format(data1));
            rec.setRec_data_que_pagou(data_venda.format(data1));
            rec.setRec_valor_pago(cPagBean.getPag_valor_recebido());

            rec.setRec_recebeu_com(cPagBean.getPag_recebeucom_din_chq_cart());
            rec.setRec_parcelas_cartao(cPagBean.getPag_parcelas_cartao());
            rec.setRec_enviado("N");

            if (rDao.grava_receber(rec)) {
                Util.gerarHistoricoPagamento(getApplicationContext(), 1, CLI_NOME, cPagBean.getPag_recebeucom_din_chq_cart(), CHAVE_VENDA, new BigDecimal(valor_total_venda).setScale(2, RoundingMode.HALF_UP), new BigDecimal(valor_total_venda).setScale(2, RoundingMode.HALF_UP), BigDecimal.ZERO, "N");
            }

            finish();
            Intent Principal = new Intent(this, Principal.class);
            startActivity(Principal);
            Util.Mensagem(getApplicationContext(), "Venda realizada com sucesso", Util.THEME_CMDV);
        }

        if (cPagBean.getPag_tipo_pagamento().equals("QUINZENAL")) {
            iPagamento pgq = new PagamentoQuinzenal();
            pgq.pagar(obj_venda);

            finish();
            Intent Principal = new Intent(this, Principal.class);
            startActivity(Principal);
            Util.Mensagem(getApplicationContext(), "Venda realizada com sucesso", Util.THEME_CMDV);

        }

        if (cPagBean.getPag_tipo_pagamento().equals("MENSAL")) {
            iPagamento pgm = new PagamentoMensal();
            pgm.pagar(obj_venda);
            finish();
            Intent Principal = new Intent(this, Principal.class);
            startActivity(Principal);
            Util.Mensagem(getApplicationContext(), "Venda realizada com sucesso", Util.THEME_CMDV);
        }

        if (cPagBean.getPag_tipo_pagamento().equals("SEMANAL")) {
            iPagamento pgs = new PagamentoSemanal();
            pgs.pagar(obj_venda);
            finish();
            Intent Principal = new Intent(this, Principal.class);
            startActivity(Principal);
            Util.Mensagem(getApplicationContext(), "Venda realizada com sucesso", Util.THEME_CMDV);
        }

    }

    public boolean vender_acima_limite() {

        boolean fechar = true;

        if (cPagBean != null) {
            if (!cPagBean.getPag_tipo_pagamento().equals("AVISTA") && SUB_TOTAL.doubleValue() > LIMITE_CLIENTE.doubleValue()) {

                if (conf.getPERMITIR_VENDER_ACIMA_DO_LIMITE().equals("S")) {
                    fechar = true;
                } else {
                    fechar = false;
                    Util.Mensagem(VenderProduto.this, "o limite do cliente é insulficiente...", Util.THEME_CMDV);
                }
            }
        }
        return fechar;
    }


    public boolean pode_fechar_venda() {

        atualiza_subtotal_na_tela();

        boolean fechar = true;

        if (confPagDao.p_lista_de_pagamentos().isEmpty()) {
            fechar = false;
            inserir_formapagamento();
        } else if (SUB_TOTAL.compareTo(BigDecimal.ZERO) <= 0) {
            fechar = false;
            Util.Mensagem(VenderProduto.this, "impossivel fechar venda com valor (0) zero...", Util.THEME_CMDV);
        } else if (PREVISAO_ENTREGA.equals("")) {
            fechar = false;
            toDatePickerDialog.show();
        }

        return fechar;
    }

    private Long gravaVenda() {
        cPagBean = confPagDao.p_busca_confpagamento();
        Configuracoes_SqliteBean confbean = new Configuracoes_SqliteBean();
        Configuracoes_SqliteDao confdao = new Configuracoes_SqliteDao(getApplicationContext());

        confbean = confdao.BuscaParamentrosEmpresa();
        venBean_C.setVendac_chave(CHAVE_VENDA);
        venBean_C.setVendac_datahoravenda(Util.DataHojeComHorasStringUSA());
        venBean_C.setVendac_previsao_entrega(Util.FormataDataAAAAMMDD(PREVISAO_ENTREGA));
        venBean_C.setVendac_cli_codigo(CODIGO_CLIENTE);
        venBean_C.setVendac_cli_nome(CLI_NOME);
        venBean_C.setVendac_fpgto_codigo(0);
        venBean_C.setVendac_fpgto_tipo(cPagBean.getPag_tipo_pagamento() + " " + cPagBean.getPag_parcelas_normal().toString() + " x");
        venBean_C.setVendac_usu_codigo(confbean.getUSU_CODIGO());
        venBean_C.setVendac_usu_nome(confbean.getNOME_VENDEDOR());
        venBean_C.setVendac_valor(SUB_TOTAL);
        venBean_C.setVendac_peso_total(BigDecimal.ZERO);

        if (cPagBean.getPag_tipo_pagamento().equals("AVISTA") && cPagBean.getPag_recebeucom_din_chq_cart().equals("CARTAO")) {
            venBean_C.setVendac_observacao("VENDA AVISTA NO CARTAO EM " + cPagBean.getPag_parcelas_cartao().toString() + "x");
        } else {
            venBean_C.setVendac_observacao("");
        }

        venBean_C.setVendac_enviada("N");

        if (mlocation != null) {
            venBean_C.setVendac_latitude(String.valueOf(mlocation.getLatitude()));
            venBean_C.setVendac_longitude(String.valueOf(mlocation.getLongitude()));
        } else {
            venBean_C.setVendac_latitude("0");
            venBean_C.setVendac_longitude("0");
        }

        VendaDAO venda = new VendaDAO(getApplicationContext());
        Long gravacao = venda.gravaVenda(venBean_C, lista_de_todos_items_da_venda);

        // ALTERANDO A QUANTIDADE DO ESTOQUE ESTOQUE DO PRODUTO
        if (gravacao > 0) {
            atualiza_quantidade_estoque_produto_apenas_no_app();
        }

        return gravacao;
    }


    private void atualiza_quantidade_estoque_produto_apenas_no_app() {
        for (VendaD_SqliteBeanTEMP prd : lista_de_todos_items_da_venda) {
            Produto_SqliteBean p1 = new Produto_SqliteBean();
            Produto_SqliteBean p2 = new Produto_SqliteBean();
            p1.setPrd_codigo(prd.getVendad_codigo_produto());
            p2 = prdDao.BuscarProdutoPelo_ID(p1);

            if (p2 != null) {
                BigDecimal quantidade_existente = p2.getPrd_quant();
                BigDecimal quantidade_alterada = quantidade_existente.subtract(prd.getVendad_quantidade());
                prdBean.setPrd_codigo(prd.getVendad_codigo_produto());
                prdBean.setPrd_quant(quantidade_alterada);
                prdDao.AtualizaEstoqueProduto(prdBean);

            }
        }
    }


    private void busca_informacoes_do_cliente(Integer codigo_cliente_intent) {

        Cliente_SqliteBean clibean = new Cliente_SqliteBean();
        Cliente_SqliteDao clidao = new Cliente_SqliteDao(this);

        clibean = clidao.BuscarClientePorCli_codigo(codigo_cliente_intent);
        CLI_CODIGO = clibean.getCli_codigo();
        CLI_NOME = clibean.getCli_nome();
        NOME_FANTASIA = clibean.getCli_fantasia();
        ENDERECO_CLIENTE = clibean.getCli_endereco();
        BAIRRO_CLIENTE = clibean.getCli_bairro();
        CEP_CIDADE = clibean.getCli_cep();
        CONTATO_CLIENTE1 = clibean.getCli_contato1();
        CONTATO_CLIENTE2 = clibean.getCli_contato2();
        CONTATO_CLIENTE3 = clibean.getCli_contato3();
        DATA_NASCIMETO = clibean.getCli_nascimento();
        CPF_CNPJ_CLIENTE = clibean.getCli_cpfcnpj();
        RG_INSCRICAO_ESTADUAL = clibean.getCli_rginscricaoest();
        LIMITE_CLIENTE = clibean.getCli_limite();
        EMAIL_CLIENTE = clibean.getCli_email();
        CODIGO_VENDEDOR_USUARIO = clibean.getUsu_codigo();
        SENHA_CADASTRADA = clibean.getCli_senha();
        CLIENTE_ENVIADO = clibean.getCli_enviado();
        CHAVE_CLIENTE = clibean.getCli_chave();
        vendas_txvInfo.setText(CLI_NOME.toUpperCase() + " :: " + Util.DataHojeComHorasStringBR());

        Random gerador_chave_numerica = new Random();
        Integer primeira_chave = gerador_chave_numerica.nextInt(9000000);
        String chave = CLI_NOME + EMAIL_CLIENTE + ENDERECO_CLIENTE + new Date().getTime() + primeira_chave.toString();
        CHAVE_VENDA = String.valueOf(primeira_chave);
    }


    private void confirma_exclusao_do_item(final VendaD_SqliteBeanTEMP item) {
        new MaterialDialog.Builder(this)
                .title(R.string.dlg_confima_exclusao)
                .content("Excluir produto " + item.getVendad_descricao_produto() + " ?")
                .iconRes(R.drawable.cmdvic)
                .cancelable(false)
                .onPositive(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                        new VendaD_SqliteDaoTEMP(getApplicationContext()).exclui_um_item_da_venda(item);
                        atualiza_listview_com_os_items_da_venda();
                    }
                })
                .onNegative(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .positiveText("Confirmar")
                .negativeText("Cancelar")
                .show();
    }


    public void calcularParcela() {

        cPagBean = confPagDao.p_busca_confpagamento();

        if (cPagBean != null) {

            vendas_parcelascalculadas.setText("");
            if (cPagBean.getPag_parcelas_normal() > 0) {
                BigDecimal divisor = new BigDecimal(cPagBean.getPag_parcelas_normal().toString());
                BigDecimal valorVenda = new BigDecimal(vendas_txvSubTotal.getText().toString());
                VALOR_DA_PARCELA = valorVenda.divide(divisor, RoundingMode.HALF_UP);
                vendas_parcelascalculadas.setText("parc. " + VALOR_DA_PARCELA.toString());
                vendas_formadepagamentoescolhida.setText(cPagBean.getPag_tipo_pagamento() + " " + cPagBean.getPag_parcelas_normal() + "x");

            } else if (cPagBean.getPag_parcelas_normal() == 0) {
                BigDecimal divisor = new BigDecimal(1);
                BigDecimal valorVenda = new BigDecimal(vendas_txvSubTotal.getText().toString());
                VALOR_DA_PARCELA = valorVenda.divide(divisor, RoundingMode.HALF_UP);
                vendas_parcelascalculadas.setText("parc." + VALOR_DA_PARCELA.toString());
                vendas_formadepagamentoescolhida.setText(cPagBean.getPag_tipo_pagamento() + " " + cPagBean.getPag_parcelas_normal() + "x");
            }

        }
    }

    private void atualiza_listview_com_os_items_da_venda() {

        lista_de_todos_items_da_venda = new VendaD_SqliteDaoTEMP(getApplicationContext()).busca_todos_items_na_vendad();

        mVenderProdutoAdapter = new VenderProdutoAdapterRecyclerView(this, lista_de_todos_items_da_venda);
        mRecyclerViewItensVendidos.setHasFixedSize(true);
        mRecyclerViewItensVendidos.setAdapter(mVenderProdutoAdapter);
        mRecyclerViewItensVendidos.setLayoutManager(new LinearLayoutManager(this));


        atualiza_subtotal_na_tela();
    }

    private void atualiza_subtotal_na_tela() {
        BigDecimal total = BigDecimal.ZERO;
        for (VendaD_SqliteBeanTEMP item : lista_de_todos_items_da_venda) {
            total = total.add(item.getVendad_quantidade().multiply(item.getVendad_precovenda()));
        }
        SUB_TOTAL = total.setScale(2, RoundingMode.HALF_UP);
        vendas_txvSubTotal.setText(total.setScale(2, RoundingMode.HALF_UP).toString());
        calcularParcela();
    }

    private void recuperaValoresIntent() {
        CODIGO_CLIENTE_INTENT = getIntent();
        CODIGO_CLIENTE = CODIGO_CLIENTE_INTENT.getIntExtra("CLI_CODIGO", 0);
    }

    private void declaraObjetos() {

        vendas_parcelascalculadas = (TextView) findViewById(R.id.vendas_parcelascalculadas);
        vendas_txvInfo = (TextView) findViewById(R.id.vendas_txvInfo);
        vendas_check_distribuirvalorparcelas = (CheckBox) findViewById(R.id.vendas_check_distribuirvalorparcelas);
        vendas_dataPrevEntrega = (TextView) findViewById(R.id.vendas_dataPrevEntrega);
        PREVISAO_ENTREGA = vendas_dataPrevEntrega.getText().toString();
        vendas_txvSubTotal = (TextView) findViewById(R.id.vendas_txvSubTotal);
        vendas_formadepagamentoescolhida = (TextView) findViewById(R.id.vendas_formadepagamentoescolhida);
        mRecyclerViewItensVendidos = (RecyclerView) findViewById(R.id.venderprodutorecyclerview);
        mLatitudeTextView = (TextView) findViewById((R.id.mLatitudeTextView));
        mLongitudeTextView = (TextView) findViewById((R.id.mLongitudeTextView));
        btn_configlocation = (Button) findViewById((R.id.btn_configlocation));
        prdDao = new Produto_SqliteDao(getApplicationContext());
        prdBean = new Produto_SqliteBean();
        venBean_C = new VendaC_SqliteBean();
        confPagDao = new ConfPagamento_SqliteDao(getApplicationContext());
        cPagBean = new ConfPagamento_SqliteBean();
        vendas_check_distribuirvalorparcelas.setVisibility(View.GONE);
        cPagBean = confPagDao.p_busca_confpagamento();
        conf = new Configuracoes_SqliteDao(getApplicationContext()).BuscaParamentrosEmpresa();


        if (cPagBean != null) {
            if (cPagBean.getPag_sementrada_comentrada().equals("true")) {
                vendas_check_distribuirvalorparcelas.setVisibility(View.VISIBLE);
            }
        }


        //https://www.youtube.com/watch?v=z2JaVke71RY
        //http://www.northborder-software.com/runtime_permissions.html
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        //**** listener datepick
        setDateTimeField();

        check_GPS_Enable();
    }

    private boolean isGPSEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private boolean check_GPS_Enable() {
        if (!isGPSEnabled()) {
            showConfigGpsSettings();
        }
        return isGPSEnabled();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showGetPermissionInsistent("É necessário ligar o GPS para saber onde a venda foi feita.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
                }
            } else {
                permissionIsGranted = true;
            }
            return;
        }
        if (l != null) {
            mlocation = l;
            Log.i("script", "latitude: " + l.getLatitude());
            Log.i("script", "longitude: " + l.getLongitude());
            mLatitudeTextView.setText("" + l.getLatitude());
            mLongitudeTextView.setText("" + l.getLongitude());
        }
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showGetPermissionInsistent("É necessário ligar o GPS para saber onde a venda foi feita.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
                }
            } else {
                permissionIsGranted = true;
            }
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionIsGranted = true;
                } else {
                    permissionIsGranted = false;
                    Util.Mensagem(getApplicationContext(), "Permissão de localização negada.", Util.THEME_CMDV);
                    mLongitudeTextView.setText("Longitude : 0.00");
                    mLatitudeTextView.setText("Latitude : 0.00");
                }
                break;
        }
    }

    private void showGetPermissionInsistent(String message, final String[] permissions) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(this)
                .title("Permisssão de Localização")
                .content(message)
                .iconRes(R.drawable.cmdvic)
                .cancelable(false)
                .onPositive(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(VenderProduto.this, permissions, MY_PERMISSION_REQUEST_FINE_LOCATION);
                        dialog.dismiss();
                    }
                })
                .onNegative(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .positiveText("Confirmar Permissão")
                .negativeText("Negar Permissão")
                .show();
    }


    // GPS está desligado nas configurações do android
    private void showConfigGpsSettings() {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(this)
                .title("Configurações de GPS")
                .content("você precisa ativar seu gps para saber onde a venda foi feita. Usamos esta função para saber o local onde foi realizada a venda e mostrá-la em um mapa do GOOGLE.")
                .iconRes(R.drawable.cmdvic)
                .cancelable(false)
                .onPositive(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        dialog.dismiss();
                    }
                })
                .positiveText("Confirmar")
                .show();
    }


    public void showExitDialog() {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(this)
                .title(R.string.Menuopcoes)
                .items(R.array.opcoes_venda_2)
                .content("você esta saindo sem salvar o pedido... o que deseja fazer ?")
                .iconRes(R.drawable.cmdvic)
                .cancelable(false)
                .itemsCallbackSingleChoice(0, new com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(com.afollestad.materialdialogs.MaterialDialog dialog, View itemView, int which, CharSequence text) {

                        switch (which) {

                            case 0:
                                // fica na tela de vendas
                                dialog.dismiss();
                                break;

                            case 1:
                                // exclui o pedido e a forma de pagamento que foi salva
                                new VendaD_SqliteDaoTEMP(getApplicationContext()).exclui_itens_da_venda();
                                new ConfPagamento_SqliteDao(getApplicationContext()).p_excluir_confpagamento();
                                finish();
                                break;

                            case 2:
                                // sair e salvar o pedido
                                new ConfPagamento_SqliteDao(getApplicationContext()).p_excluir_confpagamento();
                                finish();
                                break;
                        }
                        return true;
                    }
                })

                .positiveText("Confirmar")
                .show();
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLongitudeTextView.setText("Longitude.: " + location.getLongitude());
        mLatitudeTextView.setText("Latitude.: " + location.getLatitude());
        mlocation = location;
    }


    @Override
    public void onResume() {
        super.onResume();

        calcularParcela();
        cPagBean = confPagDao.p_busca_confpagamento();
        if (cPagBean != null) {
            if (cPagBean.getPag_sementrada_comentrada().equals("true")) {
                vendas_check_distribuirvalorparcelas.setVisibility(View.VISIBLE);
            }
        }
        atualiza_listview_com_os_items_da_venda();


        if (permissionIsGranted) {
            if (mGoogleApiClient.isConnected()) {
                requestLocationUpdates();
            }
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (permissionIsGranted) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (permissionIsGranted) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onBackPressed() {
        PREVISAO_ENTREGA = "";
        lista_de_todos_items_da_venda = new VendaD_SqliteDaoTEMP(getApplicationContext()).busca_todos_items_na_vendad();
        if (lista_de_todos_items_da_venda.size() > 0) {
            showExitDialog();
        } else {
            finish();
        }
    }
}
