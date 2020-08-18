package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class VendaC_SqliteBean {

	public static String CODIGO_DA_VENDA = "vendac_id";
	public static String CHAVE_DA_VENDA = "vendac_chave";
	public static String DATA_HORA_DA_VENDA = "vendac_datahoravenda";
	public static String PREVISAO_ENTREGA = "vendac_previsao_entrega";
	public static String CODIGO_DO_CLIENTE = "vendac_cli_codigo";
	public static String CODIGO_DA_FORMA_DE_PAGAMENTO = "vendac_fpgto_codigo";
	public static String VALOR_DA_VENDA = "vendac_valor";
	public static String PESO_TOTAL_DOS_PRODUTOS = "vendac_peso_total";
	public static String OBSERVACAO = "vendac_observacao";
	public static String VENDA_ENVIADA_SERVIDOR = "vendac_enviada";
	public static String LATITUDE = "vendac_latitude";
	public static String LONGITUDE = "vendac_longitude";
	public static String CODIGO_DO_USUARIO_VENDEDOR = "vendac_usu_codigo";
	public static String NOME_DO_USUARIO_VENDEDOR = "vendac_usu_nome";
	public static String NOME_DO_CLIENTE = "vendac_cli_nome";
	public static String FORMA_DE_PAGAMENTO = "vendac_fpgto_tipo";

	private Integer vendac_id;
	private String vendac_chave;
	private String vendac_datahoravenda;
	private String vendac_previsao_entrega;
	private Integer vendac_usu_codigo;
	private String vendac_usu_nome;
	private Integer vendac_cli_codigo;
	private String vendac_cli_nome;
	private Integer vendac_fpgto_codigo;
	private String vendac_fpgto_tipo;
	private BigDecimal vendac_valor;
	private BigDecimal vendac_peso_total;
	private String vendac_observacao;
	private String vendac_enviada;
	private String vendac_latitude;
	private String vendac_longitude;

	// uma venda tem varios items
	public List<VendaD_SqliteBean> itens_da_venda;

	public VendaC_SqliteBean() {
		this.itens_da_venda = new ArrayList<>();
	}

	public List<VendaD_SqliteBean> getItens_da_venda() {
		return itens_da_venda;
	}

	public Integer getVendac_id() {
		return vendac_id;
	}

	public void setVendac_id(Integer vendac_id) {
		this.vendac_id = vendac_id;
	}

	public String getVendac_chave() {
		return vendac_chave;
	}

	public void setVendac_chave(String vendac_chave) {
		this.vendac_chave = vendac_chave;
	}

	public String getVendac_datahoravenda() {
		return vendac_datahoravenda;
	}

	public void setVendac_datahoravenda(String string) {
		this.vendac_datahoravenda = string;
	}

	public String getVendac_previsao_entrega() {
		return vendac_previsao_entrega;
	}

	public void setVendac_previsao_entrega(String pREVISAO_ENTREGA2) {
		this.vendac_previsao_entrega = pREVISAO_ENTREGA2;
	}

	public Integer getVendac_cli_codigo() {
		return vendac_cli_codigo;
	}

	public void setVendac_cli_codigo(Integer vendac_cli_codigo) {
		this.vendac_cli_codigo = vendac_cli_codigo;
	}

	public Integer getVendac_fpgto_codigo() {
		return vendac_fpgto_codigo;
	}

	public void setVendac_fpgto_codigo(Integer vendac_fpgto_codigo) {
		this.vendac_fpgto_codigo = vendac_fpgto_codigo;
	}

	public BigDecimal getVendac_valor() {
		return vendac_valor;
	}

	public void setVendac_valor(BigDecimal vendac_valor) {
		this.vendac_valor = vendac_valor;
	}

	public BigDecimal getVendac_peso_total() {
		return vendac_peso_total;
	}

	public void setVendac_peso_total(BigDecimal vendac_peso_total) {
		this.vendac_peso_total = vendac_peso_total;
	}

	public String getVendac_observacao() {
		return vendac_observacao;
	}

	public void setVendac_observacao(String vendac_observacao) {
		this.vendac_observacao = vendac_observacao;
	}

	public String getVendac_enviada() {
		return vendac_enviada;
	}

	public void setVendac_enviada(String vendac_enviada) {
		this.vendac_enviada = vendac_enviada;
	}

	public String getVendac_latitude() {
		return vendac_latitude;
	}

	public void setVendac_latitude(String vendac_latitude) {
		this.vendac_latitude = vendac_latitude;
	}

	public String getVendac_longitude() {
		return vendac_longitude;
	}

	public void setVendac_longitude(String vendac_longitude) {
		this.vendac_longitude = vendac_longitude;
	}

	public Integer getVendac_usu_codigo() {
		return vendac_usu_codigo;
	}

	public void setVendac_usu_codigo(Integer vendac_usu_codigo) {
		this.vendac_usu_codigo = vendac_usu_codigo;
	}

	public String getVendac_usu_nome() {
		return vendac_usu_nome;
	}

	public void setVendac_usu_nome(String vendac_usu_nome) {
		this.vendac_usu_nome = vendac_usu_nome;
	}

	public String getVendac_cli_nome() {
		return vendac_cli_nome;
	}

	public void setVendac_cli_nome(String vendac_cli_nome) {
		this.vendac_cli_nome = vendac_cli_nome;
	}

	public String getVendac_fpgto_tipo() {
		return vendac_fpgto_tipo;
	}

	public void setVendac_fpgto_tipo(String vendac_fpgto_tipo) {
		this.vendac_fpgto_tipo = vendac_fpgto_tipo;
	}

	public BigDecimal getTotal() {

		BigDecimal total = BigDecimal.ZERO;

		for (VendaD_SqliteBean item : itens_da_venda) {
			// no VendaD_SqliteBean ele chama a qtde * preco e retorna
			// o total deste item e se houver mais de um item na venda ele vai
			// pegando o valor de cada item
			// e colocando na variavel total;

			total = total.add(item.getSubtotal());

		}
		return total.setScale(2);
	}

}
