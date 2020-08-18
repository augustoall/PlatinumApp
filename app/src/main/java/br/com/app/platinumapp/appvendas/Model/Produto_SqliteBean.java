package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;

public class Produto_SqliteBean {

	public static final String ID_SIMPLECURSORADAPTER = "_id";
	public static final String CODIGO_PRODUTO = "prd_codigo";
	public static final String CODIGO_BARRAS_EAN_13 = "prd_EAN";
	public static final String DESCRICAO_PRODUTO = "prd_descricao";
	public static final String DESCRICAO_REDUZIDA = "prd_descr_red";
	public static final String UNIDADE_MEDIDA = "prd_unmed";
	public static final String PRECO_DE_CUSTO = "prd_custo";
	public static final String PRECO_DE_VENDA = "prd_preco";
	public static final String QUANTIDADE_ESTOQUE = "prd_quant";
	public static final String CATEGORIA = "prd_categoria";
	public static final String CATEGORIA_WEB_IMPORT = "cat_descricao";

	

	private Integer prd_codigo;
	private String prd_EAN;
	private String prd_descricao;
	private String prd_descr_red;
	private String prd_unmed;
	private BigDecimal prd_custo;
	private BigDecimal prd_preco;
	private BigDecimal prd_quant;
	private String imageUrl;
	private String prd_categoria;

	public Produto_SqliteBean(Integer prd_codigo, String prd_EAN, String prd_descricao, String prd_descr_red, String prd_unmed, BigDecimal prd_custo, BigDecimal prd_preco, BigDecimal prd_quant) {
		this.prd_codigo = prd_codigo;
		this.prd_EAN = prd_EAN;
		this.prd_descricao = prd_descricao;
		this.prd_descr_red = prd_descr_red;
		this.prd_unmed = prd_unmed;
		this.prd_custo = prd_custo;
		this.prd_preco = prd_preco;
		this.prd_quant = prd_quant;
	}

	public Produto_SqliteBean() {

	}

	public Integer getPrd_codigo() {
		return prd_codigo;
	}

	public void setPrd_codigo(Integer prd_codigo) {
		this.prd_codigo = prd_codigo;
	}

	public String getPrd_EAN() {
		return prd_EAN;
	}

	public void setPrd_EAN(String prd_EAN) {
		this.prd_EAN = prd_EAN;
	}

	public String getPrd_descricao() {
		return prd_descricao;
	}

	public void setPrd_descricao(String prd_descricao) {
		this.prd_descricao = prd_descricao;
	}

	public String getPrd_descr_red() {
		return prd_descr_red;
	}

	public void setPrd_descr_red(String prd_descr_red) {
		this.prd_descr_red = prd_descr_red;
	}

	public String getPrd_unmed() {
		return prd_unmed;
	}

	public void setPrd_unmed(String prd_unmed) {
		this.prd_unmed = prd_unmed;
	}

	public BigDecimal getPrd_custo() {
		return prd_custo;
	}

	public void setPrd_custo(BigDecimal prd_custo) {
		this.prd_custo = prd_custo;
	}

	public BigDecimal getPrd_preco() {
		return prd_preco;
	}

	public void setPrd_preco(BigDecimal prd_preco) {
		this.prd_preco = prd_preco;
	}

	public BigDecimal getPrd_quant() {
		return prd_quant;
	}

	public void setPrd_quant(BigDecimal prd_quant) {
		this.prd_quant = prd_quant;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPrd_categoria() {
		return prd_categoria;
	}

	public void setPrd_categoria(String prd_categoria) {
		this.prd_categoria = prd_categoria;
	}
	
	
	
	
	
	
}
