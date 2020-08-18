package br.com.app.platinumapp.appvendas.Exportacoes;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;


public class Exporta_clientes {

    private static String URL_EXPORTA_CLIENTES;
    private static Map<String, String> params;
    private static JSONArray cli_array = null;

    public static void exporta_clientes_nao_enviados(RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {


        URL_EXPORTA_CLIENTES = Util.getBaseUrl(ctx) + "/JsonExport/exporta_clientes.php";
        Cliente_SqliteDao cliDao = new Cliente_SqliteDao(ctx);
        List<Cliente_SqliteBean> clientes_nao_enviados = cliDao.busca_clientes_nao_enviados_pro_servidor();

        if (clientes_nao_enviados.isEmpty()) {
            Log.i("script", "NAO HA CLIENTES A SEREM EXPORTADOS");
            return;
        }


        for (Cliente_SqliteBean cliente : clientes_nao_enviados) {

            Log.i("script", "CLIENTE " + cliente.getCli_nome() + " ENVIADO");

            params = new HashMap<>();
            params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));

            params.put("codigo_do_cliente", cliente.getCli_codigo().toString());
            params.put("cli_nome", cliente.getCli_nome());
            params.put("cli_fantasia", cliente.getCli_fantasia());
            params.put("cli_endereco", cliente.getCli_endereco());

            params.put("cli_bairro", cliente.getCli_bairro());
            params.put("cli_cep", cliente.getCli_cep());
            params.put("cid_codigo", cliente.getCid_codigo().toString());

            params.put("cli_contato1", cliente.getCli_contato1());
            params.put("cli_contato2", cliente.getCli_contato2());
            params.put("cli_contato3", cliente.getCli_contato3());

            params.put("cli_nascimento", cliente.getCli_nascimento());
            params.put("cli_cpfcnpj", cliente.getCli_cpfcnpj());
            params.put("cli_rginscricaoest", cliente.getCli_rginscricaoest());

            params.put("cli_limite", cliente.getCli_limite().toString());
            params.put("cli_email", cliente.getCli_email());
            params.put("cli_observacao", cliente.getCli_observacao());

            params.put("usu_codigo", cliente.getUsu_codigo().toString());
            params.put("cli_senha", cliente.getCli_senha());
            params.put("cli_chave", cliente.getCli_chave());

            params.put("ID_CPF_EMPRESA", empBean.getEmp_cpf());

            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_EXPORTA_CLIENTES, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Integer sucesso =  response.getInt("sucesso");

                        if (sucesso ==1){
                            Log.i("script", "cliente alterado");
                        }

                        if (sucesso ==2){

                            Integer codigo_off_line = response.getInt("codigo_off_line");
                            Integer novo_codigo = response.getInt("novo_codigo");

                            new Cliente_SqliteDao(ctx)
                                    .atualiza_CLI_CODIGO_pela_chave_ao_exportar(novo_codigo,codigo_off_line);

                        }


                    } catch (JSONException e) {
                        Log.i("script", "erro na classe Exporta_clientes -> JSONException :: " + e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("script", "erro na classe Exporta_clientes -> onErrorResponse :: " + error.getMessage());
                }
            });

            request.setTag("tag");
            rq.add(request);
        }
    }
}
