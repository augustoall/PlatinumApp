package br.com.app.platinumapp.appvendas.Exportacoes;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.ConfPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.ConfPagamento_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;


public class Exporta_confFpgto {

    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_CHAVEVENDA = "vendac_chave";
    private static String URL_EXPORTA_CONFPAGAMENO;
    private static Map<String, String> params;

    public static void exportaConfFormaPagamento(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {
        URL_EXPORTA_CONFPAGAMENO = Util.getBaseUrl(ctx) + "/JsonExport/ExportaConfFormaPagamento.php";
        List<ConfPagamento_SqliteBean> lista_todos_confPagamentos = new ConfPagamento_SqliteDao(ctx).busca_todos_confPagamentos();

        if (lista_todos_confPagamentos.isEmpty()) {
            Log.i("cmdv", "NAO HA CONFIGURACOES DE PAGAEMENTOS PARA SEREM EXPORTADOS");
            return;
        }

        for (final ConfPagamento_SqliteBean conf : lista_todos_confPagamentos) {
            params = new HashMap<>();
            params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
            params.put("pag_sementrada_comentrada", conf.getPag_sementrada_comentrada());
            params.put("pag_tipo_pagamento", conf.getPag_tipo_pagamento());
            params.put("pag_recebeucom_din_chq_cart", conf.getPag_recebeucom_din_chq_cart());
            params.put("pag_valor_recebido", conf.getPag_valor_recebido().toString());
            params.put("pag_parcelas_normal", conf.getPag_parcelas_normal().toString());
            params.put("pag_parcelas_cartao", conf.getPag_parcelas_cartao().toString());
            params.put("vendac_chave", conf.getVendac_chave());
            params.put("ID_CPF_EMPRESA", empBean.getEmp_cpf());

            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXPORTA_CONFPAGAMENO, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        int sucesso = (Integer) response.get(TAG_SUCCESSO);

                        if (sucesso == 1) {
                            String vendac_chave = (String) response.get(TAG_CHAVEVENDA);
                            new ConfPagamento_SqliteDao(ctx).AtualizaConfPagamentoParaEnviado(vendac_chave);
                            Log.i("cmdv", "CONFPAGAMENTO " + vendac_chave + " EXPORTADA");
                        }
                        if (sucesso == 2) {
                            Log.i("cmdv", "nao existem configuracoes de pagamentos a serem exportados");
                        }

                    } catch (JSONException e) {
                        Log.i("cmdv", "erro na classe Exporta_confFpgto -> JSONException :: " + e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("cmdv", "erro na classe Exporta_confFpgto -> onErrorResponse :: " + error.getMessage());
                }
            });
            request.setTag("tag");
            rq.add(request);
        }
    }
}
