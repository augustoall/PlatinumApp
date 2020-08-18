package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;

public class Receber_SqliteBean {

    public static final String ID_SIMPLECURSORADAPTER = "_id";
    public static final String CODIGO_RECEBER = "rec_codigo";
    public static final String NUMERO_DA_PARCELA = "rec_num_parcela";
    public static final String CODIGO_DO_CLIENTE = "rec_cli_codigo";
    public static final String NOME_DO_CLIENTE = "rec_cli_nome";
    public static final String DATA_DO_MOVIMENTO = "rec_datamovimento";
    public static final String VALOR_A_RECEBER = "rec_valorreceber";
    public static final String DATA_DO_VENCIMENTO = "rec_datavencimento";
    public static final String DATA_DO_VENCIMENTO_EXTENSO = "rec_datavencimento_extenso";
    public static final String DATA_QUE_PAGOU = "rec_data_que_pagou";
    public static final String VALOR_PAGO = "rec_valor_pago";
    public static final String FORMATO_RECEBIMENTO = "rec_recebeu_com";
    public static final String PARCELAS_CARTAO = "rec_parcelas_cartao";
    public static final String CHAVE_DA_VENDA = "vendac_chave";
    public static final String REC_ENVIADO = "rec_enviado";

    private Integer rec_codigo;
    private Integer rec_num_parcela;
    private Integer rec_cli_codigo;
    private String rec_cli_nome;
    private String rec_datamovimento;
    private BigDecimal rec_valorreceber;
    private String rec_datavencimento;
    private String rec_datavencimento_extenso;
    private String rec_data_que_pagou;
    private BigDecimal rec_valor_pago;
    private String rec_recebeu_com;
    private Integer rec_parcelas_cartao;
    private String vendac_chave;
    private String rec_enviado;
    private boolean isSelected;


    public Integer getRec_codigo() {
        return rec_codigo;
    }

    public void setRec_codigo(Integer rec_codigo) {
        this.rec_codigo = rec_codigo;
    }

    public Integer getRec_num_parcela() {
        return rec_num_parcela;
    }

    public void setRec_num_parcela(Integer rec_num_parcela) {
        this.rec_num_parcela = rec_num_parcela;
    }

    public Integer getRec_cli_codigo() {
        return rec_cli_codigo;
    }

    public void setRec_cli_codigo(Integer rec_cli_codigo) {
        this.rec_cli_codigo = rec_cli_codigo;
    }

    public String getRec_cli_nome() {
        return rec_cli_nome;
    }

    public void setRec_cli_nome(String rec_cli_nome) {
        this.rec_cli_nome = rec_cli_nome;
    }

    public String getRec_datamovimento() {
        return rec_datamovimento;
    }

    public void setRec_datamovimento(String rec_datamovimento) {
        this.rec_datamovimento = rec_datamovimento;
    }

    public BigDecimal getRec_valorreceber() {
        return rec_valorreceber;
    }

    public void setRec_valorreceber(BigDecimal rec_valorreceber) {
        this.rec_valorreceber = rec_valorreceber;
    }

    public String getRec_datavencimento() {
        return rec_datavencimento;
    }

    public void setRec_datavencimento(String rec_datavencimento) {
        this.rec_datavencimento = rec_datavencimento;
    }

    public String getRec_datavencimento_extenso() {
        return rec_datavencimento_extenso;
    }

    public void setRec_datavencimento_extenso(String rec_datavencimento_extenso) {
        this.rec_datavencimento_extenso = rec_datavencimento_extenso;
    }

    public String getRec_data_que_pagou() {
        return rec_data_que_pagou;
    }

    public void setRec_data_que_pagou(String rec_data_que_pagou) {
        this.rec_data_que_pagou = rec_data_que_pagou;
    }

    public BigDecimal getRec_valor_pago() {
        return rec_valor_pago;
    }

    public void setRec_valor_pago(BigDecimal rec_valor_pago) {
        this.rec_valor_pago = rec_valor_pago;
    }

    public String getRec_recebeu_com() {
        return rec_recebeu_com;
    }

    public void setRec_recebeu_com(String rec_recebeu_com) {
        this.rec_recebeu_com = rec_recebeu_com;
    }

    public Integer getRec_parcelas_cartao() {
        return rec_parcelas_cartao;
    }

    public void setRec_parcelas_cartao(Integer rec_parcelas_cartao) {
        this.rec_parcelas_cartao = rec_parcelas_cartao;
    }

    public String getVendac_chave() {
        return vendac_chave;
    }

    public void setVendac_chave(String vendac_chave) {
        this.vendac_chave = vendac_chave;
    }

    public String getRec_enviado() {
        return rec_enviado;
    }

    public void setRec_enviado(String rec_enviado) {
        this.rec_enviado = rec_enviado;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
