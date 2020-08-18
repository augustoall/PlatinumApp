package br.com.app.platinumapp.appvendas.Importacoes;

import android.content.Context;
import android.provider.Settings;
import android.view.View;

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

import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;


public class Importa_clientes {

    private static String URL_GET_CLIENTES = "";
    private static JSONArray clientes_array = null;


    public static void importaClientes(View view, String URL, boolean import_clientes_do_vendedor, final String usuario, RequestQueue rq, final Context ctx, Empresa_SqliteBean empBean) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("ID_CPF_EMPRESA", empBean.getEmp_cpf());
        params.put("emp_celularkey", Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));


        // se tiver configurado pra importar apenas os clientes deste
        // vendedor.
        if (import_clientes_do_vendedor) {
            params.put("usu_codigo", usuario);
        }

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Method.POST, URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    clientes_array = response.getJSONArray("clientes_array");

                    for (int i = 0; i < clientes_array.length(); i++) {
                        JSONObject c = clientes_array.getJSONObject(i);

                        Cliente_SqliteBean clienteBean = new Cliente_SqliteBean();
                        Cliente_SqliteDao clienteDao = new Cliente_SqliteDao(ctx);

                        String cli_codigo = c.getString(clienteBean.CODIGO_DO_CLIENTE);
                        String cli_nome = c.getString(clienteBean.NOME_DO_CLIENTE);
                        String cli_fantasia = c.getString(clienteBean.NOME_FANTASIA);
                        String cli_endereco = c.getString(clienteBean.ENDERECO_CLIENTE);

                        String cli_bairro = c.getString(clienteBean.BAIRRO_CLIENTE);
                        String cli_cep = c.getString(clienteBean.CEP_CIDADE);
                        String cid_codigo = c.getString(clienteBean.CODIGO_CIDADE);

                        String cli_contato1 = c.getString(clienteBean.CONTATO_CLIENTE1);
                        String cli_contato2 = c.getString(clienteBean.CONTATO_CLIENTE2);
                        String cli_contato3 = c.getString(clienteBean.CONTATO_CLIENTE3);

                        String cli_nascimento = c.getString(clienteBean.DATA_NASCIMETO);
                        String cli_cpfcnpj = c.getString(clienteBean.CPF_CNPJ_CLIENTE);
                        String cli_rginscricaoest = c.getString(clienteBean.RG_INSCRICAO_ESTADUAL);

                        String cli_limite = c.getString(clienteBean.LIMITE_CLIENTE);
                        String cli_email = c.getString(clienteBean.EMAIL_CLIENTE);
                        String cli_observacao = c.getString(clienteBean.OBSERVACAO);

                        String usu_codigo = c.getString(clienteBean.CODIGO_VENDEDOR_USUARIO);
                        String cli_senha = c.getString(clienteBean.SENHA_DO_CLIENTE);

                        String cli_chave = c.getString(clienteBean.CHAVE_CLIENTE);

                        String cli_enviado = "S";

                        clienteBean.setCli_codigo(Integer.parseInt(cli_codigo));
                        clienteBean.setCli_nome(cli_nome);
                        clienteBean.setCli_fantasia(cli_fantasia);
                        clienteBean.setCli_endereco(cli_endereco);

                        clienteBean.setCli_bairro(cli_bairro);
                        clienteBean.setCli_cep(cli_cep);
                        clienteBean.setCid_codigo(Integer.parseInt(cid_codigo));

                        clienteBean.setCli_contato1(cli_contato1);
                        clienteBean.setCli_contato2(cli_contato2);
                        clienteBean.setCli_contato3(cli_contato3);

                        clienteBean.setCli_nascimento(cli_nascimento);
                        clienteBean.setCli_cpfcnpj(cli_cpfcnpj);
                        clienteBean.setCli_rginscricaoest(cli_rginscricaoest);

                        clienteBean.setCli_limite(new BigDecimal(cli_limite).setScale(2, RoundingMode.HALF_UP));
                        clienteBean.setCli_email(cli_email);
                        clienteBean.setCli_observacao(cli_observacao);

                        clienteBean.setUsu_codigo(Integer.parseInt(usu_codigo));
                        clienteBean.setCli_senha(cli_senha);
                        clienteBean.setCli_enviado(cli_enviado);
                        clienteBean.setCli_chave("");

                        clienteDao.GravarCliente_Sqlite(clienteBean);

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
