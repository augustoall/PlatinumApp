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

import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteDao;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

/**
 * Created by JAVA on 12/05/2015.
 */
public class Exporta_receber {


    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_NUMPARCELA = "rec_num_parcela";
    private static final String TAG_CHAVEVENDA = "vendac_chave";
    private static final String TAG_CODIGO_NOVO = "codigo_novo";
    private static final String TAG_CODIGO_ANTIGO = "codigo_antigo";


    private static final String TAG_MENSAGEM = "mensagem";


    private static String URL_EXPORTA_RECEBER;
    private static String ID_CPF_EMPRESA;
    private static Map<String, String> params;


    public static void exporta_receber(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {

        ID_CPF_EMPRESA = empBean.getEmp_cpf();
        URL_EXPORTA_RECEBER = Util.getBaseUrl(ctx) + "/JsonExport/exporta_receber.php";


        List<Receber_SqliteBean> lista_de_contas_receber = new Receber_SqliteDao(ctx).busca_contas_receber_para_exportar();

        for (Receber_SqliteBean rec : lista_de_contas_receber) {

            params = new HashMap<String, String>();
            params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
            params.put("rec_num_parcela", rec.getRec_num_parcela().toString());
            params.put("rec_cli_codigo", rec.getRec_cli_codigo().toString());
            params.put("rec_cli_nome", rec.getRec_cli_nome());
            params.put("vendac_chave", rec.getVendac_chave());
            params.put("rec_datamovimento", rec.getRec_datamovimento());
            params.put("rec_valorreceber", rec.getRec_valorreceber().toString());
            params.put("rec_datavencimento", rec.getRec_datavencimento());
            params.put("rec_datavencimento_extenso", rec.getRec_datavencimento_extenso());
            params.put("rec_data_que_pagou", rec.getRec_data_que_pagou());
            params.put("rec_valor_pago", rec.getRec_valor_pago().toString());
            params.put("rec_recebeu_com", rec.getRec_recebeu_com());
            params.put("rec_parcelas_cartao", rec.getRec_parcelas_cartao().toString());
            params.put("ID_CPF_EMPRESA", ID_CPF_EMPRESA);


            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXPORTA_RECEBER, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    try {


                        int sucesso = (Integer) response.get(TAG_SUCCESSO);


                        if (sucesso == 1) {
                            String vendac_chave = (String) response.get(TAG_CHAVEVENDA);
                            String rec_num_parcela = (String) response.get(TAG_NUMPARCELA);
                            new Receber_SqliteDao(ctx).atualiza_parcela_para_enviada(vendac_chave, Integer.parseInt(rec_num_parcela));

                        }
                        if (sucesso == 2) {
                            String vendac_chave = (String) response.get(TAG_CHAVEVENDA);
                            String rec_num_parcela = (String) response.get(TAG_NUMPARCELA);
                            new Receber_SqliteDao(ctx).atualiza_parcela_para_enviada(vendac_chave, Integer.parseInt(rec_num_parcela));

                        }
/*
                        if (sucesso == 3) {
                            String vendac_chave = (String) response.get(TAG_CHAVEVENDA);
                            String rec_num_parcela = (String) response.get(TAG_NUMPARCELA);
                            String codigo_novo = (String) response.get(TAG_CODIGO_NOVO);
                            String codigo_antigo = (String) response.get(TAG_CODIGO_ANTIGO);

                            new Receber_SqliteDao(ctx).atualiza_parcela_para_enviada(vendac_chave, Integer.parseInt(rec_num_parcela));

                            new Receber_SqliteDao(ctx).atualiza_codigo_cliente_offline
                                    (Integer.parseInt(codigo_novo), Integer.parseInt(codigo_antigo), vendac_chave);

                        }
                        */


                    } catch (JSONException e) {
                        Log.i("Script", "JSONException: " + e.getMessage());

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.i("Script", "VolleyError: " + error.getMessage());
                    Log.i("Script", "VolleyError: " + error.getCause());
                }
            });

            request.setTag("tag");
            rq.add(request);


        }


    }
}
