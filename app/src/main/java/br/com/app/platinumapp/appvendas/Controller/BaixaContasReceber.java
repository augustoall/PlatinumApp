package br.com.app.platinumapp.appvendas.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.app.platinumapp.appvendas.Adapters.ContaRecAdapter;
import br.com.app.platinumapp.appvendas.Adapters.ListaHistoricoPagamentosAdapter;
import br.com.app.platinumapp.appvendas.Model.Cheque_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Cheques_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.RecyclerTouchListener;
import br.com.app.platinumapp.appvendas.Util.Util;


public class BaixaContasReceber extends Activity {

    private Intent CODIGO_CLIENTE_INTENT;
    private Integer CODIGO_CLIENTE;
    private Intent CHAVEDAVENDA_INTENT;
    private String VENDACHAVE;
    private EditText pgvalorcheque;
    private EditText pgTelefone1, pgTelefone2, pgnumerocheque, pgnomedobanco, pgdonocheque, pgcpfdono;
    private RadioGroup RecrgReceber;
    private RadioButton RecrbDinheiro, RecrbCheque, RecrbCartao;
    private TextView textView2, textView3, textView4, textView5, textView6, textView7, textView8;
    private TextView pgvencimentocheque, textView25;
    private LayoutInflater inflater;
    private String RECEBEU_COM_DIN_CHQ_CAR = "";
    private String CHQ_TERCEIRO = "";
    private String CHAVE_DA_VENDA = "";

    private ImageView rec_btn_datainicial, rec_btn_datafinal, pg_img_vencimentocheque;
    private CheckBox check_titulo_pago;

    private SparseBooleanArray titulo_escolhido;
    private Receber_SqliteBean recBean;
    private Receber_SqliteDao recDao;
    private List<Receber_SqliteBean> mLista_de_parcelas_selecionadas;
    private List<Receber_SqliteBean> mLista_de_parcelas_recyclerview;
    private int mQuantidade_de_parcelas_pagas;
    private ImageView img_recconta;
    private TextView rec_txt_datafinal;
    private TextView rec_txt_datainicial;
    private Integer quantidade_parcelas_pagas = 0;
    private TextView txv_quantidade_parcelaspagas;
    private TextView txvInfoRecCliente;
    private AlertDialog.Builder alerta;
    private AlertDialog alertaDialog;
    private BigDecimal SOMA_DAS_PARCELAS_SELECIONADAS = BigDecimal.ZERO;
    private BigDecimal SOMA_DE_TODAS_AS_PARCELAS_EM_ABERTO = BigDecimal.ZERO;

    private int PARCELAS_SELECIONADAS = 0;
    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapterHistPagamento;
    private RecyclerView.LayoutManager mLayoutManager;
    private BigDecimal VALOR_INFORMADO_RECEBIDO;
    private Configuracoes_SqliteBean mConfiguracoes_sqliteBean;
    private View positiveAction;
    private String SENHA_CADASTRADA;
    private String SENHA_DIGITADA;
    private Cliente_SqliteBean cliente;

    private DatePickerDialog.OnDateSetListener data_inicial = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String mes = Util.RetornaMesEmString(monthOfYear);
            String dia = Util.RetornaDiaString(dayOfMonth);
            String data = dia + "/" + mes + "/" + String.valueOf(year);

            rec_txt_datainicial.setText("");
            rec_txt_datainicial.setText(data);

        }

    };
    private DatePickerDialog.OnDateSetListener data_final = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String mes = Util.RetornaMesEmString(monthOfYear);
            String dia = Util.RetornaDiaString(dayOfMonth);
            String data = dia + "/" + mes + "/" + String.valueOf(year);

            rec_txt_datafinal.setText("");
            rec_txt_datafinal.setText(data);

        }

    };
    private DatePickerDialog.OnDateSetListener cheque_datavenc = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String mes = Util.RetornaMesEmString(monthOfYear);
            String dia = Util.RetornaDiaString(dayOfMonth);
            String data = dia + "/" + mes + "/" + String.valueOf(year);

            pgvencimentocheque.setText("");
            pgvencimentocheque.setText(data);

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_contas_receber);

        DeclaraObjetos();

        CODIGO_CLIENTE_INTENT = getIntent();
        CHAVEDAVENDA_INTENT = getIntent();

        VENDACHAVE = CHAVEDAVENDA_INTENT.getStringExtra("CHAVE_VENDA");
        CODIGO_CLIENTE = CODIGO_CLIENTE_INTENT.getIntExtra("CLI_CODIGO", 0);

        // buscando os dados do cliente
        cliente = new Cliente_SqliteBean();
        Cliente_SqliteDao cliDao = new Cliente_SqliteDao(getApplicationContext());
        cliente = cliDao.BuscarClientePorCli_codigo(CODIGO_CLIENTE);

        if (cliente != null) {
            SENHA_CADASTRADA = cliente.getCli_senha();
        }

        recBean = new Receber_SqliteBean();
        recDao = new Receber_SqliteDao(getApplicationContext());

        mLista_de_parcelas_recyclerview = recDao.busca_parcelas_do_cliente(CODIGO_CLIENTE, "01/01/2017", "14/09/2050", false, VENDACHAVE);

        if (mLista_de_parcelas_recyclerview.size() <= 0) {
            Util.Mensagem(this, "o Pedido " + VENDACHAVE + " não possui parcelas em aberto", Util.THEME_CMDV);
            finish();
        } else {
            txvInfoRecCliente.setText(mLista_de_parcelas_recyclerview.get(0).getRec_cli_nome().toLowerCase());
        }


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ContaRecAdapter(mLista_de_parcelas_recyclerview, BaixaContasReceber.this);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


            }

            @Override
            public void onLongClick(View view, int position) {
                Receber_SqliteBean receber = mLista_de_parcelas_recyclerview.get(position);
                inflater_lista_historico_de_pagamentos_da_parcela(receber.getRec_num_parcela().toString(), receber.getVendac_chave().toString());
            }
        }));

        busca_e_soma_parcelas_selecionadas();


        check_titulo_pago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar_contas_do_cliente();
            }
        });

    }


    private void inflater_lista_historico_de_pagamentos_da_parcela(String parcela, String vendac_chave) {

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.inflater_lista_dos_hist_das_parcelas_pagas, null);
        alerta = new AlertDialog.Builder(this);
        alerta.setTitle("informações da parcela ( " + parcela + " )");
        alerta.setView(view);

        final ListView listView_historicos_das_parcelas = (ListView) view.findViewById(R.id.lista_historicos_das_parcelas);

        List<HistoricoPagamento_SqliteBean> lista_de_pagamentos = new HistoricoPagamento_SqliteDao(getApplicationContext()).busca_historicos_por_vendac_chave(vendac_chave, parcela);

        if (lista_de_pagamentos.isEmpty()) {
            Util.Mensagem(getApplicationContext(), "parcela sem pagamentos", Util.THEME_CMDV);
            return;
        }

        listView_historicos_das_parcelas.setAdapter(new ListaHistoricoPagamentosAdapter(getApplicationContext(), lista_de_pagamentos));

        view.findViewById(R.id.btn_fecharhistparcelaspagas).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                alertaDialog.dismiss();
            }
        });


        alertaDialog = alerta.create();
        alertaDialog.show();

    }

    public void receberconta(View v) {
        busca_e_soma_parcelas_selecionadas();
        informar_valor_recebido_dialog(SOMA_DAS_PARCELAS_SELECIONADAS);
    }

    public void sair(View v) {
        finish();
    }

    public void pesquisar_titulos(View v) {
        buscar_contas_do_cliente();
    }

    public void setListnersComponents() {
        RecrgReceber.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.RecrbDinheiro:
                        RECEBEU_COM_DIN_CHQ_CAR = "DINHEIRO";
                        break;
                    case R.id.RecrbCheque:
                        RECEBEU_COM_DIN_CHQ_CAR = "CHEQUE";
                        receber_com_cheque();
                        break;
                    case R.id.RecrbCartao:
                        RECEBEU_COM_DIN_CHQ_CAR = "CARTAO";
                        break;
                }
            }
        });
    }

    public void buscar_contas_do_cliente() {

        String datainicial = rec_txt_datainicial.getText().toString().trim();
        String datafinal = rec_txt_datafinal.getText().toString().trim();

        mLista_de_parcelas_recyclerview = recDao.busca_parcelas_do_cliente(CODIGO_CLIENTE, datainicial, datafinal, check_titulo_pago.isChecked(), VENDACHAVE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ContaRecAdapter(mLista_de_parcelas_recyclerview, BaixaContasReceber.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void baixa_conta_cliente_sem_senha() {
        if (validar()) {
            baixar();
        }
    }

    private void receber_com_cheque() {

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.inflater_informa_como_pagou, null);

        alerta = new AlertDialog.Builder(this);
        alerta.setCancelable(false);

        alerta.setView(view);

        pgvalorcheque = (EditText) view.findViewById(R.id.pgvalorcheque);
        pgTelefone1 = (EditText) view.findViewById(R.id.pgTelefone1);

        pgTelefone2 = (EditText) view.findViewById(R.id.pgtelefone2);

        pgnumerocheque = (EditText) view.findViewById(R.id.pgnumerocheque);
        pgnomedobanco = (EditText) view.findViewById(R.id.pgnomedobanco);
        pgdonocheque = (EditText) view.findViewById(R.id.pgdonocheque);
        pgcpfdono = (EditText) view.findViewById(R.id.pgcpfdono);
        pgvencimentocheque = (TextView) view.findViewById(R.id.pgvencimentocheque);
        pg_img_vencimentocheque = (ImageView) view.findViewById(R.id.pg_img_vencimentocheque);

        textView2 = (TextView) view.findViewById(R.id.textView2);
        textView3 = (TextView) view.findViewById(R.id.textView3);
        textView4 = (TextView) view.findViewById(R.id.textView4);
        textView5 = (TextView) view.findViewById(R.id.textView5);
        textView6 = (TextView) view.findViewById(R.id.textView6);
        textView7 = (TextView) view.findViewById(R.id.textView7);
        textView8 = (TextView) view.findViewById(R.id.textView8);
        textView25 = (TextView) view.findViewById(R.id.textView25);

        textView2.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        textView3.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        textView4.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        textView5.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        textView6.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        textView7.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        textView8.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        textView25.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));

        pgvalorcheque.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        pgTelefone1.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        pgTelefone2.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        pgnumerocheque.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        pgnomedobanco.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        pgdonocheque.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        pgcpfdono.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));
        pgvencimentocheque.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensed));

        List<String> items_spinner = new ArrayList<String>();
        items_spinner.add("CHEQUE DE TERCEIRO");
        items_spinner.add("CHEQUE PROPRIO");

        final Spinner sp_chequeterceiro = (Spinner) view.findViewById(R.id.sp_chequeterceiro);
        ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_spinner);
        sp_chequeterceiro.setAdapter(array_adapter);

        sp_chequeterceiro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {

                if (posicao == 0) {
                    CHQ_TERCEIRO = "S";
                    mostratodoscampos();
                }

                if (posicao == 1) {
                    CHQ_TERCEIRO = "N";
                    mostra_numero_banco_vencimento();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.bt_gravarcheque).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Cheques_SqliteBean chqBean = new Cheques_SqliteBean();
                Cheque_SqliteDao chqDao = new Cheque_SqliteDao(getApplicationContext());

                boolean validou = false;

                if (CHQ_TERCEIRO == "N") {

                    if (valida_cheque_proprio().equals("")) {

                        validou = true;

                        // O CHEQUE E DO PROPRIO CLIENTE
                        Cliente_SqliteBean cliBean = new Cliente_SqliteBean();
                        cliBean = new Cliente_SqliteDao(getApplicationContext()).BuscarClientePorCli_codigo(CODIGO_CLIENTE);
                        chqBean.setChq_cli_codigo(CODIGO_CLIENTE);
                        chqBean.setChq_cpf_dono(cliBean.getCli_cpfcnpj());
                        chqBean.setChq_telefone1(cliBean.getCli_contato1());
                        chqBean.setChq_telefone2(cliBean.getCli_contato2());
                        chqBean.setChq_nomedono(cliBean.getCli_fantasia());

                        chqBean.setChq_numerocheque(pgnumerocheque.getText().toString());
                        chqBean.setChq_nomebanco(pgnomedobanco.getText().toString());
                        chqBean.setChq_vencimento(Util.FormataDataAAAAMMDD(pgvencimentocheque.getText().toString()));
                        chqBean.setChq_valorcheque(new BigDecimal(pgvalorcheque.getText().toString()).setScale(2, RoundingMode.HALF_UP));

                        chqBean.setChq_enviado("N");
                        chqBean.setChq_terceiro("N");
                        chqBean.setChq_dataCadastro(Util.DataSemHorasUSA());
                        chqBean.setVendac_chave(CHAVE_DA_VENDA);

                    } else {
                        Util.Mensagem(BaixaContasReceber.this, valida_cheque_proprio(), Util.THEME_CMDV);
                    }

                }

                if (CHQ_TERCEIRO == "S") {

                    if (validacampos().equals("")) {

                        validou = true;

                        chqBean.setChq_cli_codigo(CODIGO_CLIENTE);
                        chqBean.setChq_cpf_dono(pgcpfdono.getText().toString());
                        chqBean.setChq_telefone1(pgTelefone1.getText().toString());
                        chqBean.setChq_telefone2(pgTelefone2.getText().toString());
                        chqBean.setChq_nomedono(pgdonocheque.getText().toString());

                        chqBean.setChq_numerocheque(pgnumerocheque.getText().toString());
                        chqBean.setChq_nomebanco(pgnomedobanco.getText().toString());
                        chqBean.setChq_vencimento(Util.FormataDataAAAAMMDD(pgvencimentocheque.getText().toString()));
                        chqBean.setChq_valorcheque(new BigDecimal(pgvalorcheque.getText().toString()).setScale(2, RoundingMode.HALF_UP));

                        chqBean.setChq_enviado("N");
                        chqBean.setChq_terceiro("S");
                        chqBean.setVendac_chave(CHAVE_DA_VENDA);
                        chqBean.setChq_dataCadastro(Util.DataSemHorasUSA());
                    } else {
                        Util.Mensagem(BaixaContasReceber.this, validacampos(), Util.THEME_CMDV);
                    }

                }

                if (validou) {
                    chqDao.gravar_cheque(chqBean);
                    Util.Mensagem(getApplicationContext(), "CHEQUE ADICIONADO", Util.THEME_CMDV);
                    limpa_campos();
                }
            }
        });


        view.findViewById(R.id.pg_img_vencimentocheque).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                showDialog(3);

            }
        });


        view.findViewById(R.id.saircheque).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                alertaDialog.dismiss();

            }
        });

        alertaDialog = alerta.create();
        alertaDialog.show();
    }

    private void informar_valor_recebido_dialog(BigDecimal valor_recebido) {


        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.dlg_informa_valor_recebido)
                .customView(R.layout.dlg_pedir_valor_recebido, true)
                .positiveText(R.string.dlg_positiveText_confirmar)
                .negativeText(R.string.dlg_negativeText_cancelar)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        EditText dlg_txt_pedir_valor_recebido = (EditText) dialog.getCustomView().findViewById(R.id.dlg_txt_pedir_valor_recebido);

                        VALOR_INFORMADO_RECEBIDO = new BigDecimal(dlg_txt_pedir_valor_recebido.getText().toString());
                        busca_e_soma_parcelas_selecionadas();

                        if (mQuantidade_de_parcelas_pagas > 0) {
                            Util.Mensagem(BaixaContasReceber.this, "as parcelas selecionadas ja foram pagas", Util.THEME_CMDV);
                        } else {
                            if (mConfiguracoes_sqliteBean.getPEDIR_SENHA_NA_VENDA().equals("S")) {
                                baixa_conta_cliente_com_senha_dialog();
                            } else {
                                baixa_conta_cliente_sem_senha();
                            }
                        }


                    }
                })
                .build();

        EditText dlg_txt_pedir_valor_recebido = (EditText) dialog.getCustomView().findViewById(R.id.dlg_txt_pedir_valor_recebido);

        if (PARCELAS_SELECIONADAS > 1) {
            dlg_txt_pedir_valor_recebido.setEnabled(false);
            dlg_txt_pedir_valor_recebido.setText(valor_recebido.toString());
        } else {
            dlg_txt_pedir_valor_recebido.setEnabled(true);
            dlg_txt_pedir_valor_recebido.setText(valor_recebido.toString());
            dlg_txt_pedir_valor_recebido.requestFocus();
            dlg_txt_pedir_valor_recebido.selectAll();
        }

        // determina qual o botao de acao positiva , botao OK
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

        dlg_txt_pedir_valor_recebido.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


        int widgetColor = ThemeSingleton.get().widgetColor;

        MDTintHelper.setTint(
                (EditText) dlg_txt_pedir_valor_recebido,
                widgetColor == 0 ? ContextCompat.getColor(this, R.color.colorAccent) : widgetColor);

        dialog.show();
        positiveAction.setEnabled(dlg_txt_pedir_valor_recebido.getText().length() > 0);

    }


    private void mostratodoscampos() {

        pgvalorcheque.setVisibility(View.VISIBLE);
        pgTelefone1.setVisibility(View.VISIBLE);
        pgTelefone2.setVisibility(View.VISIBLE);
        pgnumerocheque.setVisibility(View.VISIBLE);
        pgnomedobanco.setVisibility(View.VISIBLE);
        pgdonocheque.setVisibility(View.VISIBLE);
        pgcpfdono.setVisibility(View.VISIBLE);
        pgvencimentocheque.setVisibility(View.VISIBLE);

        textView2.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.VISIBLE);
        textView4.setVisibility(View.VISIBLE);
        textView5.setVisibility(View.VISIBLE);
        textView6.setVisibility(View.VISIBLE);
        textView7.setVisibility(View.VISIBLE);
        textView8.setVisibility(View.VISIBLE);
        textView25.setVisibility(View.VISIBLE);

    }

    private void mostra_numero_banco_vencimento() {

        pgvencimentocheque.setVisibility(View.VISIBLE);
        pgnumerocheque.setVisibility(View.VISIBLE);
        pgnomedobanco.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        textView5.setVisibility(View.VISIBLE);
        textView7.setVisibility(View.VISIBLE);

        textView8.setVisibility(View.VISIBLE);
        pgvalorcheque.setVisibility(View.VISIBLE);

        pgTelefone1.setVisibility(View.GONE);
        pgTelefone2.setVisibility(View.GONE);
        pgdonocheque.setVisibility(View.GONE);
        pgcpfdono.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        textView6.setVisibility(View.GONE);
        textView25.setVisibility(View.GONE);

    }

    private String valida_cheque_proprio() {

        String texto_error = "";
        if (pgnomedobanco.getText().toString().trim().length() <= 3) {
            pgnomedobanco.requestFocus();
            texto_error = "informe o nome do banco";
        } else if (pgvalorcheque.getText().toString().trim().length() <= 1) {
            pgvalorcheque.requestFocus();
            texto_error = "informe o valor do cheque";
        } else if (pgnumerocheque.getText().toString().trim().length() <= 2) {
            pgnumerocheque.requestFocus();
            texto_error = "informe o numero do cheque";
        } else if (pgvencimentocheque.getText().toString().trim().length() <= 8) {
            pgvencimentocheque.requestFocus();
            texto_error = "informe a data do vencimento";
        }

        return texto_error;

    }

    private String validacampos() {

        String texto_error = "";

        if (pgnomedobanco.getText().toString().trim().length() <= 3) {
            pgnomedobanco.requestFocus();
            texto_error = "informe o nome do banco";
        } else if (pgcpfdono.getText().toString().trim().length() < 11) {
            pgcpfdono.requestFocus();
            texto_error = "informe 11 digitos para o cpf";
        } else if (!Util.validaCPF(pgcpfdono.getText().toString().trim())) {
            pgcpfdono.requestFocus();
            texto_error = "este cpf nao e valido";
        } else if (pgdonocheque.getText().toString().trim().length() <= 4) {
            pgdonocheque.requestFocus();
            texto_error = "informe quem e o dono do cheque";
        } else if (pgnumerocheque.getText().toString().trim().length() <= 2) {
            pgnumerocheque.requestFocus();
            texto_error = "informe o numero do cheque";
        } else if (pgTelefone1.getText().toString().trim().length() <= 6) {
            pgTelefone1.requestFocus();
            texto_error = "informe o telefone";
        } else if (pgvalorcheque.getText().toString().trim().length() <= 1) {
            pgvalorcheque.requestFocus();
            texto_error = "informe o valor do cheque";
        } else if (pgvencimentocheque.getText().toString().trim().length() <= 8) {
            pgvencimentocheque.requestFocus();
            texto_error = "informe a data do vencimento";
        }

        return texto_error;

    }

    private void limpa_campos() {
        pgvalorcheque.setText("");
        pgTelefone1.setText("");
        pgTelefone2.setText("");
        pgnumerocheque.setText("");
        pgnomedobanco.setText("");
        pgdonocheque.setText("");
        pgcpfdono.setText("");
        pgvencimentocheque.setText("");
    }

    public boolean validar() {

        boolean validou = true;
        busca_e_soma_parcelas_selecionadas();

        if (PARCELAS_SELECIONADAS <= 0) {
            Util.Mensagem(BaixaContasReceber.this, "nenhum titulo selecionado...", Util.THEME_CMDV);
            validou = false;
        }

        if (PARCELAS_SELECIONADAS == 1 && VALOR_INFORMADO_RECEBIDO.doubleValue() == 0) {
            Util.Mensagem(BaixaContasReceber.this, "valor zero nao permitido...", Util.THEME_CMDV);
            validou = false;
        }

        if (PARCELAS_SELECIONADAS == 1 && VALOR_INFORMADO_RECEBIDO.doubleValue() > SOMA_DE_TODAS_AS_PARCELAS_EM_ABERTO.doubleValue()) {
            Util.Mensagem(BaixaContasReceber.this, "o valor informado ultrapassa o valor de todas as parcelas...", Util.THEME_CMDV);
            validou = false;
        }

        return validou;

    }

    public void busca_e_soma_parcelas_selecionadas() {

        // lista de todas as parcelas que estao presentes no adapter do recyclerview .
        // o metodo getMyList_contarec é retornado da classe ContaRecAdapter com todos os itens inclusive
        // os que foram selecionados e os que não foram selecionados. é dessa lista que obtemos
        // all processo aqui na classe BaixaContasReceber.
        mLista_de_parcelas_recyclerview = ((ContaRecAdapter) mAdapter).getMyList_contarec();


        // lista das parcelas que foram selecionadas
        mLista_de_parcelas_selecionadas = new ArrayList<>();

        // quantidade de parcelas que foram selecionadas
        PARCELAS_SELECIONADAS = 0;

        // quantidade de parcelas que ja foram pagas
        mQuantidade_de_parcelas_pagas = 0;

        // soma das parcelas que foram selecionadas
        SOMA_DAS_PARCELAS_SELECIONADAS = BigDecimal.ZERO;

        // soma total da soma de todas as parcelas
        SOMA_DE_TODAS_AS_PARCELAS_EM_ABERTO = BigDecimal.ZERO;

        for (Receber_SqliteBean item_parcela : mLista_de_parcelas_recyclerview) {

            // testa se a parcela nao foi pago  rec_valor_pago = 0
            if (item_parcela.getRec_valor_pago().doubleValue() == 0) {
                SOMA_DE_TODAS_AS_PARCELAS_EM_ABERTO = SOMA_DE_TODAS_AS_PARCELAS_EM_ABERTO.add(item_parcela.getRec_valorreceber());
            }


            if (item_parcela.isSelected()) {

                // quantidade de parcelas selecionadas
                PARCELAS_SELECIONADAS++;

                // armazenando a soma das parcelas selecionadas
                SOMA_DAS_PARCELAS_SELECIONADAS = SOMA_DAS_PARCELAS_SELECIONADAS.add(item_parcela.getRec_valorreceber());

                //pegando as parcelas selecionadas no recyclerview
                mLista_de_parcelas_selecionadas.add(item_parcela);

                //pegando as parcelas pagas
                if (item_parcela.getRec_valor_pago().doubleValue() > 0) {
                    mQuantidade_de_parcelas_pagas++;
                    txv_quantidade_parcelaspagas.setText("contas pagas (" + mQuantidade_de_parcelas_pagas + " de " + mLista_de_parcelas_selecionadas.size() + ")");
                }


            }
        }
    }


    public void baixa_conta_cliente_com_senha_dialog() {

        busca_e_soma_parcelas_selecionadas();

        if (validar()) {

            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title(R.string.dlg_pedir_senha)
                    .customView(R.layout.dlg_pedir_senha_cliente, true)
                    .positiveText(R.string.dlg_positiveText_confirmar)
                    .negativeText(R.string.dlg_negativeText_cancelar)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            baixar();

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

    }

    private void baixar() {

        busca_e_soma_parcelas_selecionadas();

        Calendar calendario = Calendar.getInstance(new Locale("pt", "BR"));
        Date data1 = calendario.getTime();
        SimpleDateFormat datapagamento = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < PARCELAS_SELECIONADAS; i++) {


            if (VALOR_INFORMADO_RECEBIDO.doubleValue() == SOMA_DAS_PARCELAS_SELECIONADAS.doubleValue()) {

                BigDecimal VALOR_A_RECEBER = mLista_de_parcelas_selecionadas.get(i).getRec_valorreceber();
                Receber_SqliteBean recBean = new Receber_SqliteBean();
                Receber_SqliteDao recDao = new Receber_SqliteDao(getApplication());
                recBean.setVendac_chave(mLista_de_parcelas_selecionadas.get(i).getVendac_chave());
                recBean.setRec_num_parcela(mLista_de_parcelas_selecionadas.get(i).getRec_num_parcela());
                recBean.setRec_data_que_pagou(datapagamento.format(data1));
                recBean.setRec_valor_pago(VALOR_INFORMADO_RECEBIDO);
                recBean.setRec_recebeu_com(RECEBEU_COM_DIN_CHQ_CAR);
                recDao.baixa_parcela_cliente(recBean);
                Util.gerarHistoricoPagamento(getApplicationContext(), mLista_de_parcelas_selecionadas.get(i).getRec_num_parcela(), txvInfoRecCliente.getText().toString(), RECEBEU_COM_DIN_CHQ_CAR, mLista_de_parcelas_selecionadas.get(i).getVendac_chave(), VALOR_A_RECEBER.setScale(2, RoundingMode.HALF_UP), VALOR_INFORMADO_RECEBIDO, BigDecimal.ZERO, "N");
            }

            if (VALOR_INFORMADO_RECEBIDO.doubleValue() < SOMA_DAS_PARCELAS_SELECIONADAS.doubleValue()) {

                BigDecimal VALOR_A_RECEBER = mLista_de_parcelas_selecionadas.get(i).getRec_valorreceber();
                Receber_SqliteBean recBean = new Receber_SqliteBean();
                Receber_SqliteDao recDao = new Receber_SqliteDao(getApplication());
                recBean.setVendac_chave(mLista_de_parcelas_selecionadas.get(i).getVendac_chave());
                recBean.setRec_num_parcela(mLista_de_parcelas_selecionadas.get(i).getRec_num_parcela());
                BigDecimal RESTANTE_A_PAGAR = mLista_de_parcelas_selecionadas.get(i).getRec_valorreceber().subtract(VALOR_INFORMADO_RECEBIDO);
                recBean.setRec_valorreceber(RESTANTE_A_PAGAR);

                // o cliente nao pagou a parcela totalmente aqui entao alteramos a sua nova data de vencimento
                // para a data da proxima parcela existente
                Integer NUMERO_PARCELA = mLista_de_parcelas_selecionadas.get(i).getRec_num_parcela();
                String VENDAC_CHAVE =mLista_de_parcelas_selecionadas.get(i).getVendac_chave();

                Receber_SqliteBean data_proxima_parcela = new Receber_SqliteDao(getApplicationContext()).busca_proxima_parcela(NUMERO_PARCELA,VENDAC_CHAVE);

                if (data_proxima_parcela != null){
                    recBean.setRec_datavencimento(data_proxima_parcela.getRec_datavencimento());
                }else{
                    recBean.setRec_datavencimento(mLista_de_parcelas_selecionadas.get(i).getRec_datavencimento());
                }

                recDao.atualiza_valor_e_data_vencimento_parcela_recvaloreceber(recBean);
                Util.gerarHistoricoPagamento(getApplicationContext(), mLista_de_parcelas_selecionadas.get(i).getRec_num_parcela(), txvInfoRecCliente.getText().toString(), RECEBEU_COM_DIN_CHQ_CAR, mLista_de_parcelas_selecionadas.get(i).getVendac_chave(), VALOR_A_RECEBER.setScale(2, RoundingMode.HALF_UP), VALOR_INFORMADO_RECEBIDO.setScale(2, RoundingMode.HALF_UP), RESTANTE_A_PAGAR.setScale(2, RoundingMode.HALF_UP), "N");


            }


            if (VALOR_INFORMADO_RECEBIDO.doubleValue() > SOMA_DAS_PARCELAS_SELECIONADAS.doubleValue()) {

                // enquanto der pra ir pagando as parcelas , vai dando baixa... ate o valor informado acabar
                int p = 0;
                while (VALOR_INFORMADO_RECEBIDO.doubleValue() >= SOMA_DAS_PARCELAS_SELECIONADAS.doubleValue()) {

                    BigDecimal VALOR_A_RECEBER = mLista_de_parcelas_recyclerview.get(p).getRec_valorreceber();

                    if (VALOR_INFORMADO_RECEBIDO.doubleValue() >= VALOR_A_RECEBER.doubleValue()) {
                        Receber_SqliteBean recBean = new Receber_SqliteBean();
                        Receber_SqliteDao recDao = new Receber_SqliteDao(getApplication());
                        recBean.setVendac_chave(mLista_de_parcelas_recyclerview.get(p).getVendac_chave());
                        recBean.setRec_num_parcela(mLista_de_parcelas_recyclerview.get(p).getRec_num_parcela());
                        recBean.setRec_data_que_pagou(datapagamento.format(data1));
                        recBean.setRec_valor_pago(VALOR_A_RECEBER);
                        recBean.setRec_recebeu_com(RECEBEU_COM_DIN_CHQ_CAR);
                        recDao.baixa_parcela_cliente(recBean);
                        Util.gerarHistoricoPagamento(getApplicationContext(), mLista_de_parcelas_recyclerview.get(p).getRec_num_parcela(), txvInfoRecCliente.getText().toString(), RECEBEU_COM_DIN_CHQ_CAR, mLista_de_parcelas_recyclerview.get(p).getVendac_chave(), VALOR_A_RECEBER.setScale(2, RoundingMode.HALF_UP), VALOR_INFORMADO_RECEBIDO.setScale(2, RoundingMode.HALF_UP), BigDecimal.ZERO, "N");
                        VALOR_INFORMADO_RECEBIDO = VALOR_INFORMADO_RECEBIDO.subtract(VALOR_A_RECEBER);
                    }

                    if (VALOR_INFORMADO_RECEBIDO.doubleValue() < VALOR_A_RECEBER.doubleValue()) {

                        String CHAVE_DA_VENDA = mLista_de_parcelas_recyclerview.get(p).getVendac_chave();
                        Receber_SqliteBean recBean3 = new Receber_SqliteBean();
                        Receber_SqliteBean recBean4 = new Receber_SqliteBean();
                        Receber_SqliteBean recBean5 = new Receber_SqliteBean();
                        Receber_SqliteDao recDao3 = new Receber_SqliteDao(getApplicationContext());

                        recBean4.setVendac_chave(CHAVE_DA_VENDA);
                        recBean5 = recDao3.busca_numero_parcela(recBean4);
                        if (recBean5 != null) {
                            recBean3.setRec_num_parcela(recBean5.getRec_num_parcela());
                            recBean3.setVendac_chave(recBean5.getVendac_chave());
                            BigDecimal VALOR_DESCONTADO = recBean5.getRec_valorreceber().subtract(VALOR_INFORMADO_RECEBIDO);
                            recBean3.setRec_valorreceber(VALOR_DESCONTADO);
                            recDao3.atualiza_valor_parcela_recvaloreceber(recBean3);
                            Util.gerarHistoricoPagamento(getApplicationContext(), recBean5.getRec_num_parcela(), txvInfoRecCliente.getText().toString(), RECEBEU_COM_DIN_CHQ_CAR, mLista_de_parcelas_recyclerview.get(p).getVendac_chave(), recBean5.getRec_valorreceber().setScale(2, RoundingMode.HALF_UP), VALOR_INFORMADO_RECEBIDO.setScale(2), VALOR_DESCONTADO, "N");
                            VALOR_INFORMADO_RECEBIDO = BigDecimal.ZERO;
                        }
                    }
                    p++;
                }
            }
        }
        // final do for
        buscar_contas_do_cliente();
        busca_e_soma_parcelas_selecionadas();
        Util.Mensagem(BaixaContasReceber.this, "baixa de conta efetuada com sucesso...", Util.THEME_CMDV);
        finish();


    }

    public void DeclaraObjetos() {

        mConfiguracoes_sqliteBean = new Configuracoes_SqliteDao(getApplicationContext()).BuscaParamentrosEmpresa();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        check_titulo_pago = (CheckBox) findViewById(R.id.check_titulo_pago);
        img_recconta = (ImageView) findViewById(R.id.recconta);
        RecrgReceber = (RadioGroup) findViewById(R.id.RecrgReceber);
        RecrbDinheiro = (RadioButton) findViewById(R.id.RecrbDinheiro);
        RecrbCheque = (RadioButton) findViewById(R.id.RecrbCheque);
        RecrbCartao = (RadioButton) findViewById(R.id.RecrbCartao);
        txvInfoRecCliente = (TextView) findViewById(R.id.txvInfoRecCliente);
        txv_quantidade_parcelaspagas = (TextView) findViewById(R.id.txv_quantidade_parcelaspagas);
        txv_quantidade_parcelaspagas.setTypeface(Util.SetmyTypeface(getApplicationContext(), Util.FONT_TYPEFACE_RobotoCondensedBold));
        rec_txt_datafinal = (TextView) findViewById(R.id.rec_txt_datafinal);
        rec_txt_datainicial = (TextView) findViewById(R.id.rec_txt_datainicial);
        rec_btn_datainicial = (ImageView) findViewById(R.id.rec_btn_datainicial);
        rec_btn_datafinal = (ImageView) findViewById(R.id.rec_btn_datafinal);
        rec_txt_datainicial.setText("01/01/2017");
        rec_txt_datafinal.setText("01/01/2020");
        Integer opcao_dinheiro_cartao_cheque = RecrgReceber.getCheckedRadioButtonId();
        if (opcao_dinheiro_cartao_cheque == RecrbDinheiro.getId()) {
            RECEBEU_COM_DIN_CHQ_CAR = "DINHEIRO";
        }
        if (opcao_dinheiro_cartao_cheque == RecrbCartao.getId()) {
            RECEBEU_COM_DIN_CHQ_CAR = "CARTAO";
        }
        if (opcao_dinheiro_cartao_cheque == RecrbCheque.getId()) {
            RECEBEU_COM_DIN_CHQ_CAR = "CHEQUE";
        }
        setListnersComponents();
    }

    public void data1(View view) {
        showDialog(0);
    }

    public void data2(View view) {
        showDialog(1);
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

        if (id == 1) {
            return new DatePickerDialog(this, data_final, ano, mes, dia);
        }

        if (id == 3) {
            return new DatePickerDialog(this, cheque_datavenc, ano, mes, dia);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        busca_e_soma_parcelas_selecionadas();
    }

}
