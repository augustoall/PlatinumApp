package br.com.app.platinumapp.appvendas.Importacoes;

import android.content.Context;
import android.view.View;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.Cidades_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cidades_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

public class Importa_cidades {


    // importa apenas as cidades que estao atribuidas aos clientes da empresa
    //SELECT * from cidades WHERE cid_codigo IN (SELECT DISTINCT cid_codigo from clientes WHERE ID_CPF_EMPRESA = ?)

    private static String GETCID = "";
    private static JSONArray cidade_array = null;
    private static Cidades_SqliteBean cBean;
    private static Cidades_SqliteDao cDao;

    public static void importa_cidades(View view, RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {

        cDao = new Cidades_SqliteDao(ctx);
        cDao.exclui_cidades();
        GETCID = Util.getBaseUrl(ctx) + "/json/exporta_cidades.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("emp_cpf", empBean.getEmp_cpf());
        cBean = new Cidades_SqliteBean();

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Method.POST, GETCID, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    cidade_array = response.getJSONArray("cidade_array");

                    for (int i = 0; i < cidade_array.length(); i++) {
                        JSONObject c = cidade_array.getJSONObject(i);

                        cBean = new Cidades_SqliteBean();

                        String CODIGO_CIDADE = c.getString(cBean.CODIGO_CIDADE);
                        String NOME_CIDADE = c.getString(cBean.NOME_CIDADE);
                        String ESTADO = c.getString(cBean.ESTADO);

                        cBean.setCid_codigo(Integer.valueOf(CODIGO_CIDADE));
                        cBean.setCid_nome(NOME_CIDADE);
                        cBean.setCid_uf(ESTADO);

                        cDao.grava_cidade(cBean);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        request.setTag("tag");
        rq.add(request);

    }

}
