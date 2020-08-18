package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;

public class VendaD_SqliteBean {

	public static String CHAVE_DA_VENDA = "vendac_chave";
	public static String CODIGO_DE_BARRAS = "vendad_ean";
	public static String CODIGO_DO_PRODUTO = "vendad_codigo_produto";
	public static String DESCRICAO_PRODUTO = "vendad_descricao_produto";
	public static String NUMERO_DO_ITEM = "vendad_nro_item";
	public static String QUANTIDADE = "vendad_quantidade";
	public static String PRECO_DE_VENDA = "vendad_precovenda";
	public static String TOTAL_QTDE_X_PRECOVENDA = "vendad_total";

	private String vendac_chave;
	private String vendad_ean;
	private Integer vendad_codigo_produto;
	private String vendad_descricao_produto;
	private Integer vendad_nro_item;
	private BigDecimal vendad_quantidade;
	private BigDecimal vendad_precovenda;
	private BigDecimal vendad_total;

	public String getVendac_chave() {
		return vendac_chave;
	}

	public void setVendac_chave(String vendac_chave) {
		this.vendac_chave = vendac_chave;
	}

	public String getVendad_ean() {
		return vendad_ean;
	}

	public void setVendad_ean(String vendad_ean) {
		this.vendad_ean = vendad_ean;
	}

	public Integer getVendad_codigo_produto() {
		return vendad_codigo_produto;
	}

	public void setVendad_codigo_produto(Integer vendad_codigo_produto) {
		this.vendad_codigo_produto = vendad_codigo_produto;
	}

	public String getVendad_descricao_produto() {
		return vendad_descricao_produto;
	}

	public void setVendad_descricao_produto(String vendad_descricao_produto) {
		this.vendad_descricao_produto = vendad_descricao_produto;
	}

	public Integer getVendad_nro_item() {
		return vendad_nro_item;
	}

	public void setVendad_nro_item(Integer vendad_nro_item) {
		this.vendad_nro_item = vendad_nro_item;
	}

	public BigDecimal getVendad_quantidade() {
		return vendad_quantidade;
	}

	public void setVendad_quantidade(BigDecimal vendad_quantidade) {
		this.vendad_quantidade = vendad_quantidade;
	}

	public BigDecimal getVendad_precovenda() {
		return vendad_precovenda;
	}

	public void setVendad_precovenda(BigDecimal vendad_precovenda) {
		this.vendad_precovenda = vendad_precovenda;
	}

	public BigDecimal getVendad_total() {
		return vendad_total;
	}

	public void setVendad_total(BigDecimal vendad_total) {
		this.vendad_total = vendad_total;
	}

	public BigDecimal getSubtotal() {
		return this.vendad_quantidade.multiply(this.vendad_precovenda);
	}

}
