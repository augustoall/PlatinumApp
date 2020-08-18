package br.com.app.platinumapp.appvendas.Controller;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Adapters.CatalogoProdutosAdapter;
import br.com.app.platinumapp.appvendas.Importacoes.VolleySingleton;
import br.com.app.platinumapp.appvendas.Model.CatalogoProdutoModel;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.CustomJsonObjectRequest;
import br.com.app.platinumapp.appvendas.Util.Util;


public class CatalogoProdutosRecyclerView extends AppCompatActivity {

    private static final String TAG_IMAGENS_PRODUTOS = "imagens_produtos";
    private static String ID_CPF_EMPRESA;
    private static String URL_IMAGES;
    private static Map<String, String> params;
    private static JSONArray imagens_produtos = null;
    private ListView lvPosts;
    private List<CatalogoProdutoModel> list;
    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogo_recyclerview);

        list = new ArrayList<>();

        empDao = new Empresa_SqliteDao(this);
        empBean = new Empresa_SqliteBean();
        empBean = empDao.buscarEmpresa();
        rq = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        if (empBean != null) {
            ID_CPF_EMPRESA = empBean.getEmp_cpf();
            URL_IMAGES = Util.getBaseUrl(getApplicationContext()) + "/JsonExport/imagens_produtos.php";
            params = new HashMap<String, String>();
            params.put("emp_celularkey", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            params.put("ID_CPF_EMPRESA", ID_CPF_EMPRESA);
            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, URL_IMAGES, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        imagens_produtos = response.getJSONArray(TAG_IMAGENS_PRODUTOS);
                        for (int i = 0; i < imagens_produtos.length(); i++) {
                            JSONObject imagem = imagens_produtos.getJSONObject(i);
                            String Img_descricao = imagem.getString("Img_descricao");
                            String Prd_codigo = imagem.getString("Prd_codigo");
                            String prd_descricao = imagem.getString("prd_descricao");
                            String prd_preco = imagem.getString("prd_preco");
                            CatalogoProdutoModel catalogo = new CatalogoProdutoModel();
                            catalogo.setPrd_codigo(Prd_codigo);
                            catalogo.setPrd_descricao(prd_descricao);
                            catalogo.setImg_url(Util.getBaseUrl(getApplicationContext()) + "/view/Assets/produtosimg/" + Img_descricao);
                            catalogo.setPrd_preco(prd_preco);
                            list.add(catalogo);
                        }

                        if (!list.isEmpty()) {
                            CatalogoProdutosAdapter adapter = new CatalogoProdutosAdapter((ArrayList<CatalogoProdutoModel>) list, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }else{
                            Util.Mensagem(CatalogoProdutosRecyclerView.this,"Nenhuma imagem cadastrada", Util.THEME_CMDV);
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




    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
