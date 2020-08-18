package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;

public class ConfPagamento_SqliteBean {

	public static String CODIGO_PAGAMENTO = "pag_codigo";
	public static String ENTRADA = "pag_sementrada_comentrada";
	public static String TIPO_PAGAMENTO = "pag_tipo_pagamento";
	public static String RECEBEU_COM = "pag_recebeucom_din_chq_cart";
	public static String VALOR_RECEBIDO = "pag_valor_recebido";
	public static String PARCELA_NORMAL = "pag_parcelas_normal";
	public static String PARCELA_CARTAO = "pag_parcelas_cartao";
	public static String VENDAC_CHAVE = "vendac_chave";
	public static String ENVIADO = "pag_enviado";

	private Integer pag_codigo;
	private String pag_sementrada_comentrada;
	private String pag_tipo_pagamento;
	private String pag_recebeucom_din_chq_cart;
	private BigDecimal pag_valor_recebido;
	private Integer pag_parcelas_normal;
	private Integer pag_parcelas_cartao;
	private String vendac_chave;
	private String pag_enviado;

	public Integer getPag_codigo() {
		return pag_codigo;
	}

	public void setPag_codigo(Integer pag_codigo) {
		this.pag_codigo = pag_codigo;
	}

	public String getPag_sementrada_comentrada() {
		return pag_sementrada_comentrada;
	}

	public void setPag_sementrada_comentrada(String pag_sementrada_comentrada) {
		this.pag_sementrada_comentrada = pag_sementrada_comentrada;
	}

	public String getPag_tipo_pagamento() {
		return pag_tipo_pagamento;
	}

	public void setPag_tipo_pagamento(String pag_tipo_pagamento) {
		this.pag_tipo_pagamento = pag_tipo_pagamento;
	}

	public String getPag_recebeucom_din_chq_cart() {
		return pag_recebeucom_din_chq_cart;
	}

	public void setPag_recebeucom_din_chq_cart(String pag_recebeucom_din_chq_cart) {
		this.pag_recebeucom_din_chq_cart = pag_recebeucom_din_chq_cart;
	}

	public BigDecimal getPag_valor_recebido() {
		return pag_valor_recebido;
	}

	public void setPag_valor_recebido(BigDecimal pag_valor_recebido) {
		this.pag_valor_recebido = pag_valor_recebido;
	}

	public Integer getPag_parcelas_normal() {
		return pag_parcelas_normal;
	}

	public void setPag_parcelas_normal(Integer pag_parcelas_normal) {
		this.pag_parcelas_normal = pag_parcelas_normal;
	}

	public Integer getPag_parcelas_cartao() {
		return pag_parcelas_cartao;
	}

	public void setPag_parcelas_cartao(Integer pag_parcelas_cartao) {
		this.pag_parcelas_cartao = pag_parcelas_cartao;
	}

	public String getVendac_chave() {
		return vendac_chave;
	}

	public void setVendac_chave(String vendac_chave) {
		this.vendac_chave = vendac_chave;
	}

	public String getPag_enviado() {
		return pag_enviado;
	}

	public void setPag_enviado(String pag_enviado) {
		this.pag_enviado = pag_enviado;
	}
}
