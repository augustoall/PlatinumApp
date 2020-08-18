package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;

/**
 * Created by JAVA on 04/04/2015.
 */
public class HistoricoPagamento_SqliteBean {

    public static String CODIGO_HISTORICOPGTO = "hist_codigo";
    public static String NUMERODAPARCELA = "hist_numero_parcela";
    public static String VALORREALDAPARCELA = "hist_valor_real_parcela";
    public static String VALORPAGONODIA = "hist_valor_pago_no_dia";
    public static String RESTANTEAPAGAR = "hist_restante_a_pagar";
    public static String DATAQUEFOIREALIZADOPAGAMENTO = "hist_datapagamento";
    public static String NOMEDOCLIENTE = "hist_nomecliente";
    public static String PAGOUCOM = "hist_pagou_com";
    public static String CHAVEDAVENDA = "vendac_chave";
    public static String ENVIADO = "hist_enviado";

    private Integer hist_codigo;
    private Integer hist_numero_parcela;
    private BigDecimal hist_valor_real_parcela;
    private BigDecimal hist_valor_pago_no_dia;
    private BigDecimal hist_restante_a_pagar;
    private String hist_datapagamento;
    private String hist_nomecliente;
    private String hist_pagou_com;
    private String vendac_chave;
    private String hist_enviado;

    public Integer getHist_codigo() {
        return hist_codigo;
    }

    public void setHist_codigo(Integer hist_codigo) {
        this.hist_codigo = hist_codigo;
    }

    public Integer getHist_numero_parcela() {
        return hist_numero_parcela;
    }

    public void setHist_numero_parcela(Integer hist_numero_parcela) {
        this.hist_numero_parcela = hist_numero_parcela;
    }

    public BigDecimal getHist_valor_real_parcela() {
        return hist_valor_real_parcela;
    }

    public void setHist_valor_real_parcela(BigDecimal hist_valor_real_parcela) {
        this.hist_valor_real_parcela = hist_valor_real_parcela;
    }

    public BigDecimal getHist_valor_pago_no_dia() {
        return hist_valor_pago_no_dia;
    }

    public void setHist_valor_pago_no_dia(BigDecimal hist_valor_pago_no_dia) {
        this.hist_valor_pago_no_dia = hist_valor_pago_no_dia;
    }

    public BigDecimal getHist_restante_a_pagar() {
        return hist_restante_a_pagar;
    }

    public void setHist_restante_a_pagar(BigDecimal hist_restante_a_pagar) {
        this.hist_restante_a_pagar = hist_restante_a_pagar;
    }

    public String getHist_datapagamento() {
        return hist_datapagamento;
    }

    public void setHist_datapagamento(String hist_datapagamento) {
        this.hist_datapagamento = hist_datapagamento;
    }

    public String getHist_nomecliente() {
        return hist_nomecliente;
    }

    public void setHist_nomecliente(String hist_nomecliente) {
        this.hist_nomecliente = hist_nomecliente;
    }

    public String getHist_pagou_com() {
        return hist_pagou_com;
    }

    public void setHist_pagou_com(String hist_pagou_com) {
        this.hist_pagou_com = hist_pagou_com;
    }

    public String getVendac_chave() {
        return vendac_chave;
    }

    public void setVendac_chave(String vendac_chave) {
        this.vendac_chave = vendac_chave;
    }

    public String getHist_enviado() {
        return hist_enviado;
    }

    public void setHist_enviado(String hist_enviado) {
        this.hist_enviado = hist_enviado;
    }
}
