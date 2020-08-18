package br.com.app.platinumapp.appvendas.Importacoes;

import android.content.Context;
import android.provider.Settings;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteDao;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

public class Importa_produtos {

    private static JSONArray produtos_array = null;
    private static String URL_GETPRODUTOS = "";

    public static void importaProdutos(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {

        URL_GETPRODUTOS = Util.getBaseUrl(ctx) + "/json/exporta_produtos.php";
        Map<String, String> params = new HashMap<String, String>();

        params.put("emp_cpf", empBean.getEmp_cpf());
        params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Method.POST, URL_GETPRODUTOS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    produtos_array = response.getJSONArray("produtos_array");

                    for (int i = 0; i < produtos_array.length(); i++) {
                        JSONObject produtoweb = produtos_array.getJSONObject(i);

                        Produto_SqliteDao produtoDao = new Produto_SqliteDao(ctx);
                        Produto_SqliteBean produtoBean = new Produto_SqliteBean();

                        String prd_codigo = produtoweb.getString(produtoBean.CODIGO_PRODUTO);
                        String prd_EAN = produtoweb.getString(produtoBean.CODIGO_BARRAS_EAN_13);
                        String prd_descricao = produtoweb.getString(produtoBean.DESCRICAO_PRODUTO);
                        String prd_descr_red = produtoweb.getString(produtoBean.DESCRICAO_REDUZIDA);
                        String prd_unmed = produtoweb.getString(produtoBean.UNIDADE_MEDIDA);
                        String prd_custo = produtoweb.getString(produtoBean.PRECO_DE_CUSTO);
                        String prd_preco = produtoweb.getString(produtoBean.PRECO_DE_VENDA);
                        String prd_quant = produtoweb.getString(produtoBean.QUANTIDADE_ESTOQUE);
                        String prd_categoria = produtoweb.getString(produtoBean.CATEGORIA_WEB_IMPORT);

                        produtoBean.setPrd_codigo(Integer.parseInt(prd_codigo));
                        produtoBean.setPrd_EAN(prd_EAN);
                        produtoBean.setPrd_descricao(prd_descricao);
                        produtoBean.setPrd_descr_red(prd_descr_red);
                        produtoBean.setPrd_unmed(prd_unmed);
                        produtoBean.setPrd_custo(new BigDecimal(prd_custo).setScale(2, RoundingMode.HALF_UP));
                        produtoBean.setPrd_preco(new BigDecimal(prd_preco).setScale(2, RoundingMode.HALF_UP));
                        produtoBean.setPrd_quant(new BigDecimal(prd_quant).setScale(2, RoundingMode.HALF_UP));
                        produtoBean.setPrd_categoria(prd_categoria);

                        produtoDao.GravarProduto_Sqlite(produtoBean);
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
