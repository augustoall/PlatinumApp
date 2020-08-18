package br.com.app.platinumapp.appvendas.Importacoes;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;

public class Importa_dias_licenca {


    private static String URL_ATUALIZA_LICENCA = "";



    public static void atualiza_dias_de_licenca(final Context ctx, Empresa_SqliteBean empBean, RequestQueue rq) {

        Configuracoes_SqliteDao confdao = new Configuracoes_SqliteDao(ctx);
        Configuracoes_SqliteBean confBean = confdao.BuscaParamentrosEmpresa();

        if (confBean != null) {
            URL_ATUALIZA_LICENCA = confBean.getIP_SERVER() + "/json/atualiza_dias_licenca.php";
        }

        Map<String, String> licenca = new HashMap<>();
        licenca.put("emp_celularkey", empBean.getEmp_celularkey().trim());

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Method.POST, URL_ATUALIZA_LICENCA, licenca, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    int dias = (Integer) response.get("dias");

                    Log.i("cmdv","DIAS DE USO ATUALIZADOS PARA (" + String.valueOf(dias) + ") dias");

                    Empresa_SqliteBean empBean = new Empresa_SqliteBean();
                    Empresa_SqliteDao empDao = new Empresa_SqliteDao(ctx);

                    if (dias > 0) {
                        empBean.setEmp_totalemdias(String.valueOf(dias));
                        empDao.EditarEmpresa(empBean);
                    }

                    if (dias <= 0) {
                        empBean.setEmp_totalemdias(String.valueOf(0));
                        empDao.EditarEmpresa(empBean);
                    }

                } catch (JSONException e) {
                    Log.i("cmdv","erro na classe Importa_dias_licenca -> JSONException :: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("cmdv","erro na classe Importa_dias_licenca -> onErrorResponse :: " + error.getMessage());
            }
        });

        request.setTag("tag");
        rq.add(request);
    }

}
