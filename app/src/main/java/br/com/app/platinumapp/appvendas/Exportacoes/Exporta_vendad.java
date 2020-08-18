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
import br.com.app.platinumapp.appvendas.Model.VendaDAO;
import br.com.app.platinumapp.appvendas.Model.VendaD_SqliteBean;
import br.com.app.platinumapp.appvendas.Util.Util;

/**
 * Created by JAVA on 11/05/2015.
 */
public class Exporta_vendad {


    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_NUMPARCELA = "rec_num_parcela";
    private static final String TAG_CHAVEVENDA = "vendac_chave";
    private static final String TAG_MENSAGEM = "mensagem";


    private static String URL_EXPORTA_VENDA_D;
    private static String ID_CPF_EMPRESA;
    private static Map<String, String> params;


    public static void exporta_venda_D(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {

        ID_CPF_EMPRESA = empBean.getEmp_cpf();
        URL_EXPORTA_VENDA_D = Util.getBaseUrl(ctx) + "/JsonExport/exporta_vendaD.php";


        List<VendaD_SqliteBean> itens_venda = new VendaDAO(ctx).busca_todas_vendad();
        for (VendaD_SqliteBean itens : itens_venda) {


            params = new HashMap<String, String>();
            params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
            params.put("vendac_chave", itens.getVendac_chave());
            params.put("vendad_nro_item", itens.getVendad_nro_item().toString());
            params.put("vendad_ean", itens.getVendad_ean());
            params.put("vendad_codigo_produto", itens.getVendad_codigo_produto().toString());
            params.put("vendad_descricao_produto", itens.getVendad_descricao_produto());
            params.put("vendad_quantidade", itens.getVendad_quantidade().toString());
            params.put("vendad_precovenda", itens.getVendad_precovenda().toString());
            params.put("vendad_total", itens.getVendad_total().toString());
            params.put("ID_CPF_EMPRESA", ID_CPF_EMPRESA);

            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXPORTA_VENDA_D, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    try {


                        int sucesso = (Integer) response.get(TAG_SUCCESSO);

                        if (sucesso == 1) {
                            String vendac_chave = (String) response.get(TAG_CHAVEVENDA);
                        }

                        if (sucesso == 2) {
                            String mensagem = (String) response.get(TAG_MENSAGEM);
                        }

                        if (sucesso == 3) {
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
