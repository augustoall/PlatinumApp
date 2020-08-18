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

import br.com.app.platinumapp.appvendas.Model.Cheque_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Cheques_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;

public class Exporta_cheques {
    private static String URL_EXPORTA_CHEQUES;
    private static Map<String, String> params;

    public static void exporta_cheques_nao_exportados(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {
        URL_EXPORTA_CHEQUES = Util.getBaseUrl(ctx) + "/JsonExport/exporta_cheques.php";
        Cheque_SqliteDao chequeDao = new Cheque_SqliteDao(ctx);
        List<Cheques_SqliteBean> lista_de_cheques_nao_enviados = chequeDao.busca_todos_cheques_nao_enviados();
        if (lista_de_cheques_nao_enviados.isEmpty()) {
            Log.i("cmdv", "NAO HA CHEQUES PARA SEREM EXPORTADOS");
            return;
        }
        for (final Cheques_SqliteBean cheque : lista_de_cheques_nao_enviados) {
            params = new HashMap<>();
            params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
            params.put("chq_codigo", cheque.getChq_codigo().toString());
            params.put("chq_cli_codigo", cheque.getChq_cli_codigo().toString());
            params.put("chq_numerocheque", cheque.getChq_numerocheque());
            params.put("chq_telefone1", cheque.getChq_telefone1());
            params.put("chq_telefone2", cheque.getChq_telefone2());
            params.put("chq_cpf_dono", cheque.getChq_cpf_dono());
            params.put("chq_nomedono", cheque.getChq_nomedono());
            params.put("chq_nomebanco", cheque.getChq_nomebanco());
            params.put("chq_vencimento", cheque.getChq_vencimento());
            params.put("chq_valorcheque", cheque.getChq_valorcheque().toString());
            params.put("chq_terceiro", cheque.getChq_terceiro());
            params.put("vendac_chave", cheque.getVendac_chave());
            params.put("chq_dataCadastro", cheque.getChq_dataCadastro());
            params.put("ID_CPF_EMPRESA", empBean.getEmp_cpf());
            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXPORTA_CHEQUES, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // atualiza cheque para enviado
                    new Cheque_SqliteDao(ctx).atualiza_cheque_para_enviado(
                            new Cheques_SqliteBean(
                                    cheque.getChq_codigo(),
                                    cheque.getChq_numerocheque(),
                                    cheque.getVendac_chave()));
                    Log.i("cmdv", "CHEQUE NUMERO " + cheque.getChq_numerocheque() + " ENVIADO");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("cmdv", "erro na classe Exporta_cheques -> onErrorResponse :: " + error.getMessage());
                }
            });
            request.setTag("tag");
            rq.add(request);
        }
    }
}
