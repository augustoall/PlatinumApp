package br.com.app.platinumapp.appvendas.Controller;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.app.platinumapp.appvendas.Model.Cheque_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Cheques_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.ConfPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.ConfPagamento_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class FormaDePagamento extends AppCompatActivity {

    protected static String CHQ_TERCEIRO = "", TIPO_PAGAMENTO = "", SEM_ENTRADA_COM_ENTRADA = "", RECEBEU_COM_DIN_CHQ_CAR = "", VALOR_RECEBIDO = "";
    private LayoutInflater inflater;
    private Builder alerta;
    private AlertDialog alertaDialog;
    private List<String> array_tipo_parcelamento = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Spinner sp_formaspagamento;
    private Integer CODIGO_CLIENTE;
    private Intent INTENT_SUB_TOTAL, INTENT_CLI_CODIGO;
    private Double SUB_TOTAL;
    private LinearLayout LinearLayoutParcelasNormal, LinearLayoutEntrada, LinearLayoutTipoPagamento;
    private RadioGroup fp_rg_tipopagamento, fp_rg_opcoes_parcelamento;
    private RadioButton fp_rb_dinheiro, fp_rb_cheque, fp_rb_cartao;
    private RadioButton fp_rd_parc_sem_entrada, fp_rd_parc_com_entrada;
    private EditText fp_txt_parcelamento_normal, fp_txt_valorRecebido, fp_txt_parcelascartao_avista, pgvalorcheque;
    private EditText pgTelefone1, pgTelefone2, pgnumerocheque, pgnomedobanco, pgdonocheque, pgcpfdono;
    private TextView pgvencimentocheque, textView2, textView3, textView4, textView5, textView6, textView7, textView8, txvMetodoPagamento;
    private TextView textView25, fp_txv_total, fp_txv_valparcela;
    private DatePickerDialog.OnDateSetListener DateSetListenerVencimentoCheque = new DatePickerDialog.OnDateSetListener() {
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forma_de_pagamento);

        declaraObjetos();

        INTENT_CLI_CODIGO = getIntent();
        CODIGO_CLIENTE = INTENT_CLI_CODIGO.getIntExtra("CODIGO_CLIENTE", 0);

        INTENT_SUB_TOTAL = getIntent();
        SUB_TOTAL = INTENT_SUB_TOTAL.getDoubleExtra("SUB_TOTAL", 0);

        fp_txt_parcelamento_normal.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence valordigitado, int start, int before, int count) {
                calcula_valor_parcelas_no_textview(valordigitado);
            }

        });

        fp_txv_total.setText("Total venda " + new BigDecimal(SUB_TOTAL).setScale(2, RoundingMode.HALF_UP).toString());
        fp_txt_valorRecebido.setText(new BigDecimal(SUB_TOTAL).setScale(2).toString());

        fp_rg_tipopagamento.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.fp_rb_dinheiro:
                        RECEBEU_COM_DIN_CHQ_CAR = "DINHEIRO";
                        fp_txt_parcelascartao_avista.setVisibility(View.GONE);
                        fp_txt_valorRecebido.setVisibility(View.VISIBLE);
                        break;

                    case R.id.fp_rb_cheque:
                        RECEBEU_COM_DIN_CHQ_CAR = "CHEQUE";
                        receber_com_cheque();
                        fp_txt_parcelascartao_avista.setVisibility(View.GONE);
                        fp_txt_valorRecebido.setVisibility(View.VISIBLE);
                        break;

                    case R.id.fp_rb_cartao:
                        RECEBEU_COM_DIN_CHQ_CAR = "CARTAO";
                        fp_txt_parcelascartao_avista.setVisibility(View.VISIBLE);
                        fp_txt_valorRecebido.setVisibility(View.VISIBLE);
                        break;

                }

            }

        });

        fp_rg_opcoes_parcelamento.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.fp_rd_parc_sem_entrada:
                        SEM_ENTRADA_COM_ENTRADA = "N";
                        RECEBEU_COM_DIN_CHQ_CAR = "";
                        LinearLayoutTipoPagamento.setVisibility(View.GONE);
                        txvMetodoPagamento.setVisibility(View.GONE);
                        fp_txt_valorRecebido.setText("");
                        fp_txt_valorRecebido.setVisibility(View.GONE);
                        break;

                    case R.id.fp_rd_parc_com_entrada:

                        txvMetodoPagamento.setVisibility(View.VISIBLE);
                        LinearLayoutTipoPagamento.setVisibility(View.VISIBLE);
                        fp_txt_valorRecebido.setVisibility(View.VISIBLE);
                        SEM_ENTRADA_COM_ENTRADA = "S";
                        Integer opcao_dinheiro_cartao_cheque = fp_rg_tipopagamento.getCheckedRadioButtonId();
                        if (opcao_dinheiro_cartao_cheque == fp_rb_cartao.getId()) {
                            RECEBEU_COM_DIN_CHQ_CAR = "CARTAO";
                        }

                        if (opcao_dinheiro_cartao_cheque == fp_rb_dinheiro.getId()) {
                            RECEBEU_COM_DIN_CHQ_CAR = "DINHEIRO";
                        }

                        if (opcao_dinheiro_cartao_cheque == fp_rb_cheque.getId()) {
                            RECEBEU_COM_DIN_CHQ_CAR = "CHEQUE";
                        }

                        break;
                }

            }

        });
        array_tipo_parcelamento.add("AVISTA");
        array_tipo_parcelamento.add("SEMANAL");
        array_tipo_parcelamento.add("QUINZENAL");
        array_tipo_parcelamento.add("MENSAL");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array_tipo_parcelamento);
        sp_formaspagamento.setAdapter(arrayAdapter);
        sp_formaspagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View v, int posicao, long id) {
                TIPO_PAGAMENTO = spinner.getItemAtPosition(posicao).toString();
                if (TIPO_PAGAMENTO.equals("AVISTA")) {
                    txvMetodoPagamento.setVisibility(View.VISIBLE);
                    LinearLayoutTipoPagamento.setVisibility(View.VISIBLE);
                    LinearLayoutParcelasNormal.setVisibility(View.GONE);
                    LinearLayoutEntrada.setVisibility(View.GONE);
                    fp_txt_parcelascartao_avista.setVisibility(View.GONE);
                    fp_txt_valorRecebido.setVisibility(View.VISIBLE);
                    Integer opcao_dinheiro_cartao_cheque = fp_rg_tipopagamento.getCheckedRadioButtonId();
                    if (opcao_dinheiro_cartao_cheque == fp_rb_dinheiro.getId()) {
                        fp_txt_parcelascartao_avista.setText("0");
                        fp_txt_parcelamento_normal.setText("0");
                    }
                }

                if (TIPO_PAGAMENTO.equals("SEMANAL") || TIPO_PAGAMENTO.equals("QUINZENAL") || TIPO_PAGAMENTO.equals("MENSAL")) {
                    habilita_desabilita_componentes();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void gravarformapagamento(View v) {

        String sementradacomentrada = "";

        if (SEM_ENTRADA_COM_ENTRADA.equals("S")) {
            sementradacomentrada = "true";
        }

        if (SEM_ENTRADA_COM_ENTRADA.equals("N")) {
            sementradacomentrada = "false";
        }

        if (pode_fechar()) {

            ConfPagamento_SqliteBean cPagBean = new ConfPagamento_SqliteBean();
            ConfPagamento_SqliteDao cPagDao = new ConfPagamento_SqliteDao(getApplicationContext());
            cPagBean.setPag_parcelas_cartao(Integer.parseInt(fp_txt_parcelascartao_avista.getText().toString()));
            cPagBean.setPag_parcelas_normal(Integer.parseInt(fp_txt_parcelamento_normal.getText().toString()));
            cPagBean.setPag_recebeucom_din_chq_cart(RECEBEU_COM_DIN_CHQ_CAR);
            cPagBean.setPag_sementrada_comentrada(sementradacomentrada);
            cPagBean.setPag_tipo_pagamento(TIPO_PAGAMENTO);
            cPagBean.setPag_valor_recebido(new BigDecimal(fp_txt_valorRecebido.getText().toString()).setScale(2, RoundingMode.HALF_UP));
            cPagBean.setVendac_chave("sem_chave");
            cPagBean.setPag_enviado("N");
            cPagDao.p_gravar_confpagamento(cPagBean);

            // usando onActivityResult
            Bundle bundle = new Bundle();
            bundle.putInt("CLI_CODIGO", CODIGO_CLIENTE);
            Intent Realiza_Vendas = new Intent();
            Realiza_Vendas.putExtras(bundle);
            setResult(RESULT_OK, Realiza_Vendas);
            finish();
        }


    }

    public void habilita_desabilita_componentes() {

        Integer opcao_sem_entrada_com_entrada = fp_rg_opcoes_parcelamento.getCheckedRadioButtonId();
        Integer opcao_dinheiro_cartao_cheque = fp_rg_tipopagamento.getCheckedRadioButtonId();
        LinearLayoutParcelasNormal.setVisibility(View.VISIBLE);
        LinearLayoutEntrada.setVisibility(View.VISIBLE);
        fp_txt_valorRecebido.setVisibility(View.GONE);
        fp_txt_parcelascartao_avista.setVisibility(View.GONE);

        if (opcao_sem_entrada_com_entrada == fp_rd_parc_sem_entrada.getId()) {
            LinearLayoutTipoPagamento.setVisibility(View.GONE);
            txvMetodoPagamento.setVisibility(View.GONE);
            RECEBEU_COM_DIN_CHQ_CAR = "";

        }

        if (opcao_sem_entrada_com_entrada == fp_rd_parc_com_entrada.getId()) {
            LinearLayoutTipoPagamento.setVisibility(View.VISIBLE);
            if (opcao_dinheiro_cartao_cheque == fp_rb_cartao.getId()) {
                txvMetodoPagamento.setVisibility(View.VISIBLE);
                fp_txt_valorRecebido.setVisibility(View.VISIBLE);
                fp_txt_parcelascartao_avista.setVisibility(View.VISIBLE);
            }

            if (opcao_dinheiro_cartao_cheque == fp_rb_dinheiro.getId()) {
                fp_txt_valorRecebido.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean pode_fechar() {

        // VENDA AVISTA
        if (TIPO_PAGAMENTO.equals("AVISTA")) {

            if (fp_txt_valorRecebido.getText().toString().trim().length() <= 0) {
                fp_txt_valorRecebido.requestFocus();
                fp_txt_valorRecebido.setError("informe o valor recebido.");
                return false;
            }

            BigDecimal VALOR_RECEBIDO = new BigDecimal(fp_txt_valorRecebido.getText().toString());

            if (RECEBEU_COM_DIN_CHQ_CAR.equals("DINHEIRO") && VALOR_RECEBIDO.doubleValue() > SUB_TOTAL) {
                fp_txt_valorRecebido.requestFocus();
                fp_txt_valorRecebido.setError("valor informado maior que o valor total da venda.");
                return false;
            }

            if (RECEBEU_COM_DIN_CHQ_CAR.equals("DINHEIRO") && VALOR_RECEBIDO.doubleValue() < SUB_TOTAL) {
                fp_txt_valorRecebido.requestFocus();
                fp_txt_valorRecebido.setError("valor informado menor que o valor total da venda.");
                return false;
            }

            if (RECEBEU_COM_DIN_CHQ_CAR.equals("CARTAO") && VALOR_RECEBIDO.doubleValue() < SUB_TOTAL) {
                fp_txt_valorRecebido.requestFocus();
                fp_txt_valorRecebido.setError("valor informado menor que o total da venda.");
                return false;
            }

            if (RECEBEU_COM_DIN_CHQ_CAR.equals("CARTAO") && VALOR_RECEBIDO.doubleValue() > SUB_TOTAL) {
                fp_txt_valorRecebido.requestFocus();
                fp_txt_valorRecebido.setError("valor informado maior que o total da venda.");
                return false;
            }

            if (RECEBEU_COM_DIN_CHQ_CAR.equals("CARTAO") && fp_txt_parcelascartao_avista.getText().toString().trim().length() <= 0) {
                fp_txt_parcelascartao_avista.requestFocus();
                fp_txt_parcelascartao_avista.setError("informe (0) se não houver parcelamento no cartao.");
                return false;
            }

            if (RECEBEU_COM_DIN_CHQ_CAR.equals("CHEQUE") && VALOR_RECEBIDO.doubleValue() < SUB_TOTAL) {
                fp_txt_valorRecebido.requestFocus();
                fp_txt_valorRecebido.setError("valor informado menor que o total da venda");
                return false;
            }

            if (RECEBEU_COM_DIN_CHQ_CAR.equals("CHEQUE") && VALOR_RECEBIDO.doubleValue() > SUB_TOTAL) {
                fp_txt_valorRecebido.requestFocus();
                fp_txt_valorRecebido.setError("valor informado maior que o total da venda");
                return false;
            }
        }


        if (TIPO_PAGAMENTO.equals("SEMANAL") || TIPO_PAGAMENTO.equals("QUINZENAL") || TIPO_PAGAMENTO.equals("MENSAL")) {

            // /************** VENDA PARCELADA SEM ENTRADA
            if (SEM_ENTRADA_COM_ENTRADA.equals("N")) {
                fp_txt_valorRecebido.setText("0");
                fp_txt_parcelascartao_avista.setText("0");
                if (fp_txt_parcelamento_normal.getText().toString().trim().length() <= 0 || fp_txt_parcelamento_normal.getText().toString().equals("0")) {
                    fp_txt_parcelamento_normal.requestFocus();
                    fp_txt_parcelamento_normal.setError("informe a quantidade de parcelas.");
                    return false;
                }
            }

            // /************** VENDA PARCELADA COM ENTRADA
            if (SEM_ENTRADA_COM_ENTRADA.equals("S")) {

                if (RECEBEU_COM_DIN_CHQ_CAR.equals("DINHEIRO") || RECEBEU_COM_DIN_CHQ_CAR.equals("CARTAO") || RECEBEU_COM_DIN_CHQ_CAR.equals("CHEQUE")) {

                    if (fp_txt_valorRecebido.getText().toString().trim().length() <= 0 || fp_txt_valorRecebido.getText().toString().equals("0")) {
                        fp_txt_valorRecebido.requestFocus();
                        fp_txt_valorRecebido.setError("informe o valor recebido.");
                        return false;
                    }

                    if (fp_txt_parcelamento_normal.getText().toString().trim().length() <= 0 || fp_txt_parcelamento_normal.getText().toString().equals("0")) {
                        fp_txt_parcelamento_normal.requestFocus();
                        fp_txt_parcelamento_normal.setError("informe a quantidade de parcelas.");
                        return false;
                    }
                }

                BigDecimal VALOR_RECEBIDO = new BigDecimal(fp_txt_valorRecebido.getText().toString());
                // o valor aqui não pode ser maior mas pode ser menor pois se trata de uma venda parcelada com entrada (SEM_ENTRADA_COM_ENTRADA.equals("S")) e esta entrada pode ser menor
                if (VALOR_RECEBIDO.doubleValue() > SUB_TOTAL) {
                    fp_txt_valorRecebido.requestFocus();
                    fp_txt_valorRecebido.setError("valor informado não pode ser maior que o total da venda.");
                    return false;
                }

                if (RECEBEU_COM_DIN_CHQ_CAR.equals("CARTAO") && fp_txt_parcelascartao_avista.getText().toString().trim().length() <= 0) {
                    fp_txt_parcelascartao_avista.requestFocus();
                    fp_txt_parcelascartao_avista.setError("informe (0) se não houver parcelamento no cartao.");
                    return false;
                }

            }
        }
        return true;
    }

    private void calcula_valor_parcelas_no_textview(CharSequence valordigitado) {

        if (valordigitado.length() > 0) {
            String QUANTIDADE_DE_PARCELAS = fp_txt_parcelamento_normal.getText().toString();
            if (Integer.parseInt(QUANTIDADE_DE_PARCELAS) > 0) {
                BigDecimal divisor = new BigDecimal(Integer.parseInt(QUANTIDADE_DE_PARCELAS));
                BigDecimal valorVenda = new BigDecimal(SUB_TOTAL.toString());
                BigDecimal valor_da_parcela = valorVenda.divide(divisor, RoundingMode.HALF_UP);
                fp_txv_valparcela.setText("valor parcela " + valor_da_parcela.setScale(2, RoundingMode.HALF_UP).toString());
            }
        } else {
            BigDecimal divisor = new BigDecimal(Integer.parseInt("1"));
            BigDecimal valorVenda = new BigDecimal(SUB_TOTAL.toString());
            BigDecimal valor_da_parcela = valorVenda.divide(divisor, RoundingMode.HALF_UP);
            fp_txv_valparcela.setText("valor parcela " + valor_da_parcela.setScale(2, RoundingMode.HALF_UP).toString());
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

        List<String> items_spinner = new ArrayList<>();
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
                        chqBean.setVendac_chave("");
                        chqBean.setChq_dataCadastro(Util.DataSemHorasUSA());

                    } else {
                        Util.Mensagem(FormaDePagamento.this, valida_cheque_proprio(), Util.THEME_CMDV);
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
                        chqBean.setVendac_chave("");
                        chqBean.setChq_dataCadastro(Util.DataSemHorasUSA());
                    } else {
                        Util.Mensagem(FormaDePagamento.this, validacampos(), Util.THEME_CMDV);
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
                showDialog(0);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        if (id == 0) {
            return new DatePickerDialog(this, DateSetListenerVencimentoCheque, ano, mes, dia);
        }
        return null;
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
            texto_error = "este cpf não e valido";
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

    public void mostra_campos() {
        fp_txt_valorRecebido.setVisibility(View.GONE);
        pgvalorcheque.setVisibility(View.VISIBLE);
        pgTelefone1.setVisibility(View.VISIBLE);
        pgTelefone2.setVisibility(View.VISIBLE);
        pgnumerocheque.setVisibility(View.VISIBLE);
        pgnomedobanco.setVisibility(View.VISIBLE);
        pgdonocheque.setVisibility(View.VISIBLE);
        pgcpfdono.setVisibility(View.VISIBLE);
        pgvencimentocheque.setVisibility(View.VISIBLE);

        fp_txt_valorRecebido.setText("");
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

    @Override
    public void onBackPressed() {
        new ConfPagamento_SqliteDao(getApplicationContext()).p_excluir_confpagamento();
        new Cheque_SqliteDao(getApplicationContext()).exclui_cheque_sem_venda_vinculada();
        finish();
    }

    private void declaraObjetos() {
        sp_formaspagamento = (Spinner) findViewById(R.id.fp_sp_formaspagamento);
        LinearLayoutParcelasNormal = (LinearLayout) findViewById(R.id.LinearLayoutParcelasNormal);
        LinearLayoutEntrada = (LinearLayout) findViewById(R.id.LinearLayoutEntrada);
        LinearLayoutTipoPagamento = (LinearLayout) findViewById(R.id.LinearLayoutTipoPagamento);
        fp_txv_valparcela = (TextView) findViewById(R.id.fp_txv_valparcela);
        fp_txv_total = (TextView) findViewById(R.id.fp_txv_total);
        txvMetodoPagamento = (TextView) findViewById(R.id.txvMetodoPagamento);
        fp_txt_valorRecebido = (EditText) findViewById(R.id.fp_txt_valorRecebido);
        fp_txt_parcelascartao_avista = (EditText) findViewById(R.id.fp_txt_parcelascartao_avista);
        fp_txt_parcelamento_normal = (EditText) findViewById(R.id.fp_txt_parcelamento_normal);
        fp_rg_tipopagamento = (RadioGroup) findViewById(R.id.Rg_fp_tipopagamento);
        fp_rb_dinheiro = (RadioButton) findViewById(R.id.fp_rb_dinheiro);
        fp_rb_cheque = (RadioButton) findViewById(R.id.fp_rb_cheque);
        fp_rb_cartao = (RadioButton) findViewById(R.id.fp_rb_cartao);
        fp_rg_opcoes_parcelamento = (RadioGroup) findViewById(R.id.fp_rg_opcoes_parcelamento);
        fp_rd_parc_com_entrada = (RadioButton) findViewById(R.id.fp_rd_parc_com_entrada);
        fp_rd_parc_sem_entrada = (RadioButton) findViewById(R.id.fp_rd_parc_sem_entrada);


        Integer opcao_dinheiro_cartao_cheque = fp_rg_tipopagamento.getCheckedRadioButtonId();
        if (opcao_dinheiro_cartao_cheque == fp_rb_dinheiro.getId()) {
            RECEBEU_COM_DIN_CHQ_CAR = "DINHEIRO";
            fp_txt_parcelascartao_avista.setText("0");
            fp_txt_parcelamento_normal.setText("0");
        }

        if (opcao_dinheiro_cartao_cheque == fp_rb_cartao.getId()) {
            RECEBEU_COM_DIN_CHQ_CAR = "CARTAO";
        }

        if (opcao_dinheiro_cartao_cheque == fp_rb_cheque.getId()) {
            RECEBEU_COM_DIN_CHQ_CAR = "CHEQUE";
            fp_txt_parcelascartao_avista.setText("0");
            fp_txt_parcelamento_normal.setText("0");
        }

        Integer opcao_sem_entrada_com_entrada = fp_rg_opcoes_parcelamento.getCheckedRadioButtonId();
        if (opcao_sem_entrada_com_entrada == fp_rd_parc_sem_entrada.getId()) {
            SEM_ENTRADA_COM_ENTRADA = "N";
        }

        if (opcao_sem_entrada_com_entrada == fp_rd_parc_com_entrada.getId()) {
            SEM_ENTRADA_COM_ENTRADA = "S";
        }

    }

}
