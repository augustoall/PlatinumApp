package br.com.app.platinumapp.appvendas.Importacoes;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;


public class AtualizaDadosVendedor {

    private static JSONArray usuario_array = null;
    private static String url = "";
    private static Empresa_SqliteBean mEmpresaBean = new Empresa_SqliteBean();
    private static Configuracoes_SqliteBean mConfigBean = new Configuracoes_SqliteBean();

    public static void atualiza_dados_do_vendedor(RequestQueue rq, final Context ctx) {


        Empresa_SqliteDao mEmpresa_sqliteDao = new Empresa_SqliteDao(ctx);
        Configuracoes_SqliteDao mConfiguracoes_sqliteDao = new Configuracoes_SqliteDao(ctx);

        mEmpresaBean = mEmpresa_sqliteDao.buscarEmpresa();
        mConfigBean = mConfiguracoes_sqliteDao.BuscaParamentrosEmpresa();
        url = mConfigBean.getIP_SERVER() + "/json/atualiza_dados_vendedor.php";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("emp_celularkey", mEmpresaBean.getEmp_celularkey());

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    usuario_array = response.getJSONArray("usuario_array");

                    for (int i = 0; i < usuario_array.length(); i++) {
                        JSONObject usuario = usuario_array.getJSONObject(i);

                        Configuracoes_SqliteDao mConfiguracoes_sqliteDao = new Configuracoes_SqliteDao(ctx);
                        Configuracoes_SqliteBean confbean = new Configuracoes_SqliteBean();

                        //pegando o codigo do usuario na tabela de configuracoes do app
                        confbean.setUSU_CODIGO(usuario.getInt("usu_codigo"));
                        confbean.setNOME_VENDEDOR(usuario.getString("nome_vendedor"));
                        confbean.setDESCONTO_VENDEDOR(new BigDecimal(usuario.getDouble("usu_desconto")));

                        mConfiguracoes_sqliteDao.atualiza_dados_vendedor(confbean);
                        Log.i("cmdv","DADOS DO VENDEDOR "+usuario.getString("nome_vendedor")+ " ATUALIZADOS");

                    }

                } catch (JSONException e) {
                    Log.i("cmdv","erro em classe AtualizaDadosVendedor -> JSONException :: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("cmdv","erro na classe AtualizaDadosVendedor -> onErrorResponse :: " + error.getMessage());
            }
        });
        request.setTag("tag");
        rq.add(request);
    }


}
