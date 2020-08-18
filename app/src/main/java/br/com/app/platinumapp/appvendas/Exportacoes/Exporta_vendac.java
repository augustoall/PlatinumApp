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

import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.VendaC_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.VendaDAO;
import br.com.app.platinumapp.appvendas.Util.Util;

/**
 * Created by JAVA on 08/05/2015.
 */
public class Exporta_vendac {


    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_NUMPARCELA = "rec_num_parcela";
    private static final String TAG_CHAVEVENDA = "vendac_chave";
    private static final String TAG_MENSAGEM = "mensagem";
    private static String URL_EXPORTA_VENDA_C;
    private static String ID_CPF_EMPRESA;
    private static Map<String, String> params;


    public static void exporta_venda_c(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {

        ID_CPF_EMPRESA = empBean.getEmp_cpf();
        URL_EXPORTA_VENDA_C = Util.getBaseUrl(ctx) + "/JsonExport/exporta_vendaC.php";


        List<VendaC_SqliteBean> todas_vendac_nao_enviadas = new VendaDAO(ctx).busca_todas_vendac_nao_enviadas();

        for (VendaC_SqliteBean venda : todas_vendac_nao_enviadas) {


            params = new HashMap<String, String>();
            params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
            params.put("vendac_chave", venda.getVendac_chave());
            params.put("vendac_datahoravenda", venda.getVendac_datahoravenda());
            params.put("vendac_previsao_entrega", venda.getVendac_previsao_entrega());
            params.put("vendac_usu_codigo", venda.getVendac_usu_codigo().toString());
            params.put("vendac_usu_nome", venda.getVendac_usu_nome());
            params.put("vendac_cli_codigo", venda.getVendac_cli_codigo().toString());
            params.put("vendac_cli_nome", venda.getVendac_cli_nome());
            params.put("vendac_fpgto_codigo", venda.getVendac_fpgto_codigo().toString());
            params.put("vendac_fpgto_tipo", venda.getVendac_fpgto_tipo());
            params.put("vendac_valor", venda.getVendac_valor().toString());
            params.put("vendac_peso_total", venda.getVendac_peso_total().toString());
            params.put("vendac_observacao", venda.getVendac_observacao());
            params.put("vendac_latitude", String.valueOf(venda.getVendac_latitude()));
            params.put("vendac_longitude", String.valueOf(venda.getVendac_longitude()));
            params.put("ID_CPF_EMPRESA", ID_CPF_EMPRESA);


            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXPORTA_VENDA_C, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    try {

                        int sucesso = (Integer) response.get(TAG_SUCCESSO);

                        if (sucesso == 1) {
                            String vendac_chave = (String) response.get(TAG_CHAVEVENDA);
                            new VendaDAO(ctx).atualiza_vendac_para_enviada(vendac_chave);

                        }

                        if (sucesso == 2) {
                            String mensagem = (String) response.get(TAG_MENSAGEM);
                        }


                        if (sucesso == 0) {
                            String mensagem = (String) response.get(TAG_MENSAGEM);
                        }

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
