package br.com.app.platinumapp.appvendas.Exportacoes;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteDao;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

/**
 * Created by JAVA on 18/06/2015.
 */
public class Exporta_histPgto {

    private static String URL_EXPORTA_HISTORICOS_DE_PAGAMENTOS;
    private static Map<String, String> params;

    public static void exportaHistoricoPgto(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {

        URL_EXPORTA_HISTORICOS_DE_PAGAMENTOS = Util.getBaseUrl(ctx) + "/JsonExport/Exporta_histPgto.php";
        List<HistoricoPagamento_SqliteBean> hist = new HistoricoPagamento_SqliteDao(ctx).busca_historicos_de_pagamento_para_exportar();

        for (HistoricoPagamento_SqliteBean historicos : hist) {

            params = new HashMap<>();
            params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
            // enviando o codigo para recebe-lo como retorno de resposta para atualizar o registro
            params.put("hist_codigo", historicos.getHist_codigo().toString());
            params.put("hist_numero_parcela", historicos.getHist_numero_parcela().toString());
            params.put("hist_valor_real_parcela", historicos.getHist_valor_real_parcela().toString());
            params.put("hist_valor_pago_no_dia", historicos.getHist_valor_pago_no_dia().toString());

            params.put("hist_restante_a_pagar", historicos.getHist_restante_a_pagar().toString());
            params.put("hist_datapagamento", historicos.getHist_datapagamento());
            params.put("hist_nomecliente", historicos.getHist_nomecliente());

            params.put("hist_pagou_com", historicos.getHist_pagou_com());
            params.put("vendac_chave", historicos.getVendac_chave());
            params.put("usu_celularkey", empBean.getEmp_celularkey());
            params.put("ID_CPF_EMPRESA", empBean.getEmp_cpf());


            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXPORTA_HISTORICOS_DE_PAGAMENTOS, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // esta classe nao precisa de resposta por enquanto , apenas envia os historicos
                    Log.i("cmdv", "HISTORICO DE PAGAMENTOS ENVIADOS" + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("cmdv", "erro em classe Exporta_histPgto -> onErrorResponse :: " + error.getMessage());
                }
            });

            request.setTag("tag");
            rq.add(request);


        }

    }


}
