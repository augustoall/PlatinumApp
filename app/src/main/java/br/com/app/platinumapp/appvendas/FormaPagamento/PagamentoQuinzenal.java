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


public class PagamentoQuinzenal implements iPagamento {

	private HistoricoPagamento_SqliteBean HistBean;
	private HistoricoPagamento_SqliteDao HistDao;

	@Override
	public void pagar(ObjetoVenda pagamento_quinzenal) {

		Integer QUANTIDADE_DE_PARCELAS = pagamento_quinzenal.getQtde_veses();
		boolean VENDA_FOI_COM_ENTRADA = pagamento_quinzenal.getVenda_com_entrada();
		HistBean = new HistoricoPagamento_SqliteBean();
		HistDao = new HistoricoPagamento_SqliteDao(pagamento_quinzenal.getCtx());

		String ENTRADA = pagamento_quinzenal.getValor_pago_primeira_parcela();
		Calendar calendario = Calendar.getInstance(new Locale("pt", "BR"));
		BigDecimal divisor = new BigDecimal(pagamento_quinzenal.getQtde_veses());
		BigDecimal valorVenda = new BigDecimal(pagamento_quinzenal.getValor());
		BigDecimal VALOR_PARCELAS = valorVenda.divide(divisor, RoundingMode.HALF_UP);
		BigDecimal VALOR_ENTRADA = new BigDecimal(ENTRADA);
		BigDecimal VALOR_DE_PAGAR_AS_PARCELAS = new BigDecimal(ENTRADA);
		Receber_SqliteDao RecDao = new Receber_SqliteDao(pagamento_quinzenal.getCtx());

		Date data1 = calendario.getTime();
		SimpleDateFormat datasimples1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dataporextenso = DateFormat.getDateInstance(DateFormat.FULL);

		for (int parcela = 1; parcela < QUANTIDADE_DE_PARCELAS + 1; parcela++) {

			calendario.add(Calendar.DATE, 15);

			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				calendario.add(Calendar.DATE, 2);
			}

			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				calendario.add(Calendar.DATE, 1);
			}

			Calendar c = Calendar.getInstance(new Locale("pt", "BR"));
			c.set(Calendar.YEAR, 1980);
			c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			c.set(Calendar.DAY_OF_MONTH, 14);
			Date data_padrao = c.getTime();
			SimpleDateFormat datasimples = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dataporextenso2 = DateFormat.getDateInstance(DateFormat.FULL);
			Date data_vencimento = calendario.getTime();
			SimpleDateFormat datasimples2 = new SimpleDateFormat("yyyy-MM-dd");
			Receber_SqliteBean recBean2 = new Receber_SqliteBean();
			recBean2.setRec_num_parcela(parcela);
			recBean2.setRec_cli_codigo(pagamento_quinzenal.getRec().getRec_cli_codigo());
			recBean2.setRec_cli_nome(pagamento_quinzenal.getRec().getRec_cli_nome());
			recBean2.setVendac_chave(pagamento_quinzenal.getRec().getVendac_chave());
			recBean2.setRec_datamovimento(pagamento_quinzenal.getRec().getRec_datamovimento());
			recBean2.setRec_valorreceber(VALOR_PARCELAS);
			recBean2.setRec_datavencimento(datasimples2.format(data_vencimento));
			recBean2.setRec_datavencimento_extenso(dataporextenso2.format(data_vencimento));
			recBean2.setRec_data_que_pagou(datasimples.format(data_padrao));
			recBean2.setRec_valor_pago(BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP));
			recBean2.setRec_recebeu_com("");
			recBean2.setRec_parcelas_cartao(pagamento_quinzenal.getRec().getRec_parcelas_cartao());
			recBean2.setRec_enviado("N");
			RecDao.grava_receber(recBean2);

		}

		if (VENDA_FOI_COM_ENTRADA) {

			BaixarContaComEntrada.baixar_titulos(pagamento_quinzenal);

		}

	}

}
