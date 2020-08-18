package br.com.app.platinumapp.appvendas.FormaPagamento;

import android.content.Context;

import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;


public class ObjetoVenda {

	private String valor;
	private Integer qtde_veses;
	private Integer RecebimentoDiaSemana;
	private boolean venda_com_entrada;
	private Context ctx;
	private Receber_SqliteBean rec;
	private String valor_pago_primeira_parcela;
	private  boolean  distribuir_valor_entrada_nas_parcelas;


	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Integer getQtde_veses() {
		return qtde_veses;
	}

	public void setQtde_veses(Integer qtde_veses) {
		this.qtde_veses = qtde_veses;
	}

	public Integer getRecebimentoDiaSemana() {
		return RecebimentoDiaSemana;
	}

	public void setRecebimentoDiaSemana(Integer recebimentoDiaSemana) {
		RecebimentoDiaSemana = recebimentoDiaSemana;
	}

	public boolean getVenda_com_entrada() {
		return venda_com_entrada;
	}

	public void setVenda_com_entrada(boolean venda_com_entrada) {
		this.venda_com_entrada = venda_com_entrada;
	}

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	public Receber_SqliteBean getRec() {
		return rec;
	}

	public void setRec(Receber_SqliteBean rec) {
		this.rec = rec;
	}

	public String getValor_pago_primeira_parcela() {
		return valor_pago_primeira_parcela;
	}

	public void setValor_pago_primeira_parcela(String valor_pago_primeira_parcela) {
		this.valor_pago_primeira_parcela = valor_pago_primeira_parcela;
	}

	public boolean isDistribuir_valor_entrada_nas_parcelas() {
		return distribuir_valor_entrada_nas_parcelas;
	}

	public void setDistribuir_valor_entrada_nas_parcelas(boolean distribuir_valor_entrada_nas_parcelas) {
		this.distribuir_valor_entrada_nas_parcelas = distribuir_valor_entrada_nas_parcelas;
	}
}
