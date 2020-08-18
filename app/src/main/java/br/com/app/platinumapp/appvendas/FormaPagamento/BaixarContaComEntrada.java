package br.com.app.platinumapp.appvendas.FormaPagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteDao;
import br.com.app.platinumapp.appvendas.Util.Util;

public class BaixarContaComEntrada {

    public static void baixar_titulos(ObjetoVenda TipoPagamento_mens_qui_sem) {


        if (TipoPagamento_mens_qui_sem.isDistribuir_valor_entrada_nas_parcelas()) {

            Receber_SqliteDao RecDao = new Receber_SqliteDao(TipoPagamento_mens_qui_sem.getCtx());
            String ENTRADA = TipoPagamento_mens_qui_sem.getValor_pago_primeira_parcela();
            BigDecimal VALOR_ENTRADA = new BigDecimal(ENTRADA);
            BigDecimal QUANTIDADE_PARCELAS = new BigDecimal(TipoPagamento_mens_qui_sem.getQtde_veses());
            BigDecimal VALOR_A_DESCONTAR_NAS_PARCELAS = VALOR_ENTRADA.divide(QUANTIDADE_PARCELAS, RoundingMode.HALF_UP);

            List<Receber_SqliteBean> lista_de_contas_geradas = RecDao.busca_parcelas_geradas_na_compra(TipoPagamento_mens_qui_sem.getRec().getVendac_chave());

            for (Receber_SqliteBean parcela : lista_de_contas_geradas) {

                BigDecimal VALOR_DESCONTADO = parcela.getRec_valorreceber().subtract(VALOR_A_DESCONTAR_NAS_PARCELAS);

                Receber_SqliteBean rec = new Receber_SqliteBean();
                rec.setRec_valorreceber(VALOR_DESCONTADO);
                rec.setRec_num_parcela(parcela.getRec_num_parcela());
                rec.setVendac_chave(parcela.getVendac_chave());
                RecDao.atualiza_valor_parcela_recvaloreceber(rec);

                Util.gerarHistoricoPagamento(
                        TipoPagamento_mens_qui_sem.getCtx(),
                        parcela.getRec_num_parcela(),
                        TipoPagamento_mens_qui_sem.getRec().getRec_cli_nome(),
                        TipoPagamento_mens_qui_sem.getRec().getRec_recebeu_com(),
                        parcela.getVendac_chave(),
                        parcela.getRec_valorreceber(),
                        VALOR_A_DESCONTAR_NAS_PARCELAS.setScale(2, RoundingMode.HALF_UP),
                        VALOR_DESCONTADO.setScale(2, RoundingMode.HALF_UP), "N");

            }


        } else {


            Integer QUANTIDADE_DE_PARCELAS = TipoPagamento_mens_qui_sem.getQtde_veses();
            boolean VENDA_FOI_COM_ENTRADA = TipoPagamento_mens_qui_sem.getVenda_com_entrada();
            String ENTRADA = TipoPagamento_mens_qui_sem.getValor_pago_primeira_parcela();
            Calendar CALENDARIO = Calendar.getInstance(new Locale("pt", "BR"));
            BigDecimal DIVISOR_DE_PARCELAS = new BigDecimal(TipoPagamento_mens_qui_sem.getQtde_veses());
            BigDecimal VALOR_TOTAL_VENDA = new BigDecimal(TipoPagamento_mens_qui_sem.getValor());
            BigDecimal VALOR_DA_PARCELA = VALOR_TOTAL_VENDA.divide(DIVISOR_DE_PARCELAS, RoundingMode.HALF_UP);
            BigDecimal VALOR_ENTRADA = new BigDecimal(ENTRADA);
            BigDecimal VALOR_DE_PAGAR_AS_PARCELAS = new BigDecimal(ENTRADA);
            Receber_SqliteDao RecDao = new Receber_SqliteDao(TipoPagamento_mens_qui_sem.getCtx());

            // QUANDO O VALOR DA ENTRADA FOR IGUAL AO VALOR DA ENTRADA
            if (VALOR_ENTRADA.doubleValue() == VALOR_DA_PARCELA.doubleValue()) {

                Receber_SqliteBean recBean = new Receber_SqliteBean();

                Calendar cal = Calendar.getInstance(new Locale("pt", "BR"));
                Date data_pagamento = cal.getTime();
                SimpleDateFormat data_paga = new SimpleDateFormat("yyyy-MM-dd");
                Receber_SqliteBean parcela = new Receber_SqliteBean();
                Receber_SqliteBean parcela_pesquisada = new Receber_SqliteBean();
                parcela.setVendac_chave(TipoPagamento_mens_qui_sem.getRec().getVendac_chave());
                parcela_pesquisada = RecDao.busca_numero_parcela(parcela);
                recBean.setVendac_chave(parcela_pesquisada.getVendac_chave());
                recBean.setRec_num_parcela(parcela_pesquisada.getRec_num_parcela());
                recBean.setRec_data_que_pagou(data_paga.format(data_pagamento));
                recBean.setRec_valor_pago(VALOR_DA_PARCELA);
                recBean.setRec_recebeu_com(TipoPagamento_mens_qui_sem.getRec().getRec_recebeu_com());
                RecDao.baixa_parcela_cliente(recBean);

                Util.gerarHistoricoPagamento(TipoPagamento_mens_qui_sem.getCtx(), parcela_pesquisada.getRec_num_parcela(), TipoPagamento_mens_qui_sem.getRec().getRec_cli_nome(), TipoPagamento_mens_qui_sem.getRec().getRec_recebeu_com(), parcela_pesquisada.getVendac_chave(),VALOR_DA_PARCELA.setScale(2,RoundingMode.HALF_UP), VALOR_ENTRADA.setScale(2,RoundingMode.HALF_UP), BigDecimal.ZERO, "N");

            }

            // QUANDO O VALOR DA ENTRADA FOR MENOR QUE O VALOR DA PARCELA
            if (VALOR_ENTRADA.doubleValue() < VALOR_DA_PARCELA.doubleValue()) {

                Receber_SqliteBean recBean = new Receber_SqliteBean();
                Calendar cal = Calendar.getInstance(new Locale("pt", "BR"));
                Date data_pagamento = cal.getTime();
                SimpleDateFormat data_paga = new SimpleDateFormat("yyyy-MM-dd");
                Receber_SqliteBean parcela = new Receber_SqliteBean();
                Receber_SqliteBean parcela_pesquisada = new Receber_SqliteBean();
                parcela.setVendac_chave(TipoPagamento_mens_qui_sem.getRec().getVendac_chave());
                parcela_pesquisada = RecDao.busca_numero_parcela(parcela);
                recBean.setVendac_chave(parcela_pesquisada.getVendac_chave());
                recBean.setRec_num_parcela(parcela_pesquisada.getRec_num_parcela());
                BigDecimal VALOR_DESCONTADO = parcela_pesquisada.getRec_valorreceber().subtract(VALOR_ENTRADA);
                recBean.setRec_valorreceber(VALOR_DESCONTADO);
                RecDao.atualiza_valor_parcela_recvaloreceber(recBean);
                // gerando historico de pagamento
                Util.gerarHistoricoPagamento(TipoPagamento_mens_qui_sem.getCtx(), parcela_pesquisada.getRec_num_parcela(), TipoPagamento_mens_qui_sem.getRec().getRec_cli_nome(), TipoPagamento_mens_qui_sem.getRec().getRec_recebeu_com(), parcela_pesquisada.getVendac_chave(), VALOR_DA_PARCELA.setScale(2,RoundingMode.HALF_UP),VALOR_ENTRADA.setScale(2,RoundingMode.HALF_UP), VALOR_DESCONTADO.setScale(2,RoundingMode.HALF_UP), "N");

            }

            // QUANDO O VALOR DA ENTRADA FOR MAIOR QUE O VALOR DA PARCELA
            if (VALOR_ENTRADA.doubleValue() > VALOR_DA_PARCELA.doubleValue()) {

                Receber_SqliteBean recBean = new Receber_SqliteBean();
                Calendar cal = Calendar.getInstance(new Locale("pt", "BR"));
                Date data_pagamento = cal.getTime();
                SimpleDateFormat data_paga = new SimpleDateFormat("yyyy-MM-dd");
                List<Receber_SqliteBean> lista_de_contas_geradas = RecDao.busca_parcelas_geradas_na_compra(TipoPagamento_mens_qui_sem.getRec().getVendac_chave());
                int posicao_do_registro = 0;

                while (VALOR_DE_PAGAR_AS_PARCELAS.doubleValue() >= VALOR_DA_PARCELA.doubleValue()) {

                    BigDecimal VALOR_A_RECEBER = lista_de_contas_geradas.get(posicao_do_registro).getRec_valorreceber();

                    if (VALOR_DE_PAGAR_AS_PARCELAS.doubleValue() >= VALOR_A_RECEBER.doubleValue()) {

                        Receber_SqliteBean rec = new Receber_SqliteBean();
                        rec.setVendac_chave(lista_de_contas_geradas.get(posicao_do_registro).getVendac_chave());
                        rec.setRec_num_parcela(lista_de_contas_geradas.get(posicao_do_registro).getRec_num_parcela());
                        rec.setRec_data_que_pagou(data_paga.format(data_pagamento));
                        rec.setRec_valor_pago(VALOR_A_RECEBER);
                        rec.setRec_recebeu_com(TipoPagamento_mens_qui_sem.getRec().getRec_recebeu_com());
                        RecDao.baixa_parcela_cliente(rec);
                        Util.gerarHistoricoPagamento(TipoPagamento_mens_qui_sem.getCtx(), lista_de_contas_geradas.get(posicao_do_registro).getRec_num_parcela(), TipoPagamento_mens_qui_sem.getRec().getRec_cli_nome(), TipoPagamento_mens_qui_sem.getRec().getRec_recebeu_com(), lista_de_contas_geradas.get(posicao_do_registro).getVendac_chave(), VALOR_DA_PARCELA.setScale(2,RoundingMode.HALF_UP),VALOR_ENTRADA.setScale(2,RoundingMode.HALF_UP), BigDecimal.ZERO, "N");
                        VALOR_DE_PAGAR_AS_PARCELAS = VALOR_DE_PAGAR_AS_PARCELAS.subtract(VALOR_A_RECEBER);
                    }

                    if (VALOR_DE_PAGAR_AS_PARCELAS.doubleValue() < VALOR_A_RECEBER.doubleValue()) {

                        String CHAVE_DA_VENDA = lista_de_contas_geradas.get(posicao_do_registro).getVendac_chave();
                        Receber_SqliteBean receber = new Receber_SqliteBean();
                        Receber_SqliteBean parcela = new Receber_SqliteBean();
                        Receber_SqliteBean parcela_pesquisada = new Receber_SqliteBean();
                        parcela.setVendac_chave(CHAVE_DA_VENDA);
                        parcela_pesquisada = RecDao.busca_numero_parcela(parcela);
                        receber.setRec_num_parcela(parcela_pesquisada.getRec_num_parcela());
                        receber.setVendac_chave(parcela_pesquisada.getVendac_chave());
                        BigDecimal VALOR_DESCONTADO = parcela_pesquisada.getRec_valorreceber().subtract(VALOR_DE_PAGAR_AS_PARCELAS);
                        receber.setRec_valorreceber(VALOR_DESCONTADO);
                        RecDao.atualiza_valor_parcela_recvaloreceber(receber);
                        Util.gerarHistoricoPagamento(TipoPagamento_mens_qui_sem.getCtx(), parcela_pesquisada.getRec_num_parcela(), TipoPagamento_mens_qui_sem.getRec().getRec_cli_nome(), TipoPagamento_mens_qui_sem.getRec().getRec_recebeu_com(), parcela_pesquisada.getVendac_chave(), VALOR_DA_PARCELA.setScale(2,RoundingMode.HALF_UP), VALOR_DE_PAGAR_AS_PARCELAS.setScale(2,RoundingMode.HALF_UP), VALOR_DESCONTADO, "N");
                        VALOR_DE_PAGAR_AS_PARCELAS = BigDecimal.ZERO;

                    }

                    posicao_do_registro++;

                }
            }

        }
    }

}
