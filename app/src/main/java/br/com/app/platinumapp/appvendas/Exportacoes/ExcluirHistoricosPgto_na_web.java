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
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

/**
 * Created by JAVA on 19/06/2015.
 */
public class ExcluirHistoricosPgto_na_web {

    private static final String TAG_SUCCESSO = "sucesso";


    private static String URL_EXCLUIR_HISTORICOS_DE_PAGAMENTOS;
    private static String ID_CPF_EMPRESA;
    private static Map<String, String> params;


    public static void excluir_historicos_inseridos_na_web(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {
        ID_CPF_EMPRESA = empBean.getEmp_cpf();
        URL_EXCLUIR_HISTORICOS_DE_PAGAMENTOS = Util.getBaseUrl(ctx) + "/JsonExport/ExcluirHistoricoPgto.php";
        params = new HashMap<>();
        params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
        params.put("usu_celularKey",Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
        params.put("ID_CPF_EMPRESA", ID_CPF_EMPRESA);


        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXCLUIR_HISTORICOS_DE_PAGAMENTOS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // esta classe nao precisa de resposta por enquanto , apenas envia os historicos
                Log.i("cmdv", "HISTORICO DE PAGAMENTOS EXCLUIDOS" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("cmdv", "erro em classe ExcluirHistoricosPgto_na_web -> onErrorResponse :: " + error.getMessage());
            }
        });
        request.setTag("tag");
        rq.add(request);
    }
}





