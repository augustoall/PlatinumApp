package br.com.app.platinumapp.appvendas.FormaPagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteDao;


/*
 * 
 *  ESTA CLASSE RESPONSAVEL POR GERAR AS PARCELAS MENSAIS 
 *  E POSSUI 2 FORMAS DE GERAR AS PARCELAS
 *  
 *  1 : GERAR PARCELAS INFORMANDO O VALOR DE ENTRADA ONDE A 1� PARCELA FICA PAGA , E SE O CLIENTE 
 *  PAGAR VALOR ACIMA DO VALOR DA PRIMEIRA PARCELA , O RESTANTE DESCONTADO NA PROXIMA PARCELA, CASO 
 *  O CLIENTE PAGUE VALOR MENOR QUE O DA PARCELA ATUAL ENTAO O VALOR NEGATIVO ACRESCENTADO NA PROXIMA PARCELA
 *  
 *  2 : GERAR PARCELAS COM O VALOR TOTAL DA VENDA JOGANDO A 1� PARCELA PARA O PROXIMO MES
 * 
 * 
 */
public class PagamentoMensal implements iPagamento {

	private HistoricoPagamento_SqliteBean HistBean;
	private HistoricoPagamento_SqliteDao HistDao;

	@Override
	public void pagar(ObjetoVenda pagamento_mensal) {

		Integer QUANTIDADE_DE_PARCELAS = pagamento_mensal.getQtde_veses();
		boolean VENDA_FOI_COM_ENTRADA = pagamento_mensal.getVenda_com_entrada();
		HistBean = new HistoricoPagamento_SqliteBean();
		HistDao = new HistoricoPagamento_SqliteDao(pagamento_mensal.getCtx());

		String ENTRADA = pagamento_mensal.getValor_pago_primeira_parcela();
		Calendar calendario = Calendar.getInstance(new Locale("pt", "BR"));
		BigDecimal divisor = new BigDecimal(pagamento_mensal.getQtde_veses());
		BigDecimal valorVenda = new BigDecimal(pagamento_mensal.getValor());
		BigDecimal VALOR_PARCELAS = valorVenda.divide(divisor, RoundingMode.HALF_UP);
		BigDecimal VALOR_ENTRADA = new BigDecimal(ENTRADA);
		BigDecimal VALOR_DE_PAGAR_AS_PARCELAS = new BigDecimal(ENTRADA);
		Receber_SqliteDao RecDao = new Receber_SqliteDao(pagamento_mensal.getCtx());

		Date data1 = calendario.getTime();
		SimpleDateFormat datasimples1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dataporextenso = DateFormat.getDateInstance(DateFormat.FULL);

		for (int parcela = 1; parcela < QUANTIDADE_DE_PARCELAS + 1; parcela++) {

			// adicionando 1 mes na data atual
			calendario.add(Calendar.MONTH, 1);

			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				calendario.add(Calendar.DATE, 2);
			}

			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				calendario.add(Calendar.DATE, 1);
			}

			Date data2 = calendario.getTime();
			Receber_SqliteBean recBean = new Receber_SqliteBean();
			Receber_SqliteDao recDao = new Receber_SqliteDao(pagamento_mensal.getCtx());
			recBean.setRec_num_parcela(parcela);
			recBean.setRec_cli_codigo(pagamento_mensal.getRec().getRec_cli_codigo());
			recBean.setRec_cli_nome(pagamento_mensal.getRec().getRec_cli_nome());
			recBean.setVendac_chave(pagamento_mensal.getRec().getVendac_chave());
			recBean.setRec_datamovimento(pagamento_mensal.getRec().getRec_datamovimento());
			recBean.setRec_valorreceber(VALOR_PARCELAS);
			SimpleDateFormat datasimples2 = new SimpleDateFormat("yyyy-MM-dd");
			recBean.setRec_datavencimento(datasimples2.format(data2));
			recBean.setRec_datavencimento_extenso(dataporextenso.format(data2));
			Calendar c = Calendar.getInstance(new Locale("pt", "BR"));
			c.set(Calendar.YEAR, 1980);
			c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			c.set(Calendar.DAY_OF_MONTH, 14);
			Date dataquepagou = c.getTime();
			SimpleDateFormat datasimples = new SimpleDateFormat("yyyy-MM-dd");
			recBean.setRec_data_que_pagou(datasimples.format(dataquepagou));
			recBean.setRec_valor_pago(BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP));
			recBean.setRec_recebeu_com("");
			recBean.setRec_parcelas_cartao(pagamento_mensal.getRec().getRec_parcelas_cartao());
			recBean.setRec_enviado("N");
			recDao.grava_receber(recBean);

		}
		
		if (VENDA_FOI_COM_ENTRADA) {

			BaixarContaComEntrada.baixar_titulos(pagamento_mensal);

		}

	}

}
