package br.com.app.platinumapp.appvendas.Controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import br.com.app.platinumapp.appvendas.Adapters.ClienteAdapter;
import br.com.app.platinumapp.appvendas.Importacoes.CadastraCliente;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.RecyclerTouchListener;

public class ListaClientesVenda extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    public static final String NOME_CLIENTE = "Pesquisa por : Nome do cliente :";
    public static final String BAIRRO_CLIENTE = "Pesquisa por : Bairro :";
    public static final String FANTASIA_CLIENTE = "Pesquisa por : Nome Fantasia :";
    public static final String ENDERECO_CLIENTE = "Pesquisa por : Endereco :";

    private ClienteAdapter mClienteAdapter;
    private RecyclerView mRecyclerView;
    private SearchView.OnQueryTextListener clistener;
    private List<Cliente_SqliteBean> mCliente_sqliteBeenList = new ArrayList<>();
    private Spinner mSpinner;
    private List<String> mListString = new ArrayList<>();
    private ArrayAdapter<String> mArrayAdapter;
    private String FILTRAR_POR;
    private SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesquisa_e_lista_clientes);


        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recyclerview);
        search = (SearchView) findViewById(R.id.my_searchview);


        mListString.add(NOME_CLIENTE);
        mListString.add(BAIRRO_CLIENTE);
        mListString.add(FANTASIA_CLIENTE);
        mListString.add(ENDERECO_CLIENTE);

        // populando o spinner com um array de strings
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mListString);
        mSpinner.setAdapter(mArrayAdapter);


        mCliente_sqliteBeenList = new Cliente_SqliteDao(this).getAll("","cli_nome");
        mClienteAdapter = new ClienteAdapter(mCliente_sqliteBeenList, this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mClienteAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Cliente_SqliteBean cliente = mCliente_sqliteBeenList.get(position);
                calldialog(cliente);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getSearch();

        search.setOnQueryTextListener(clistener);
        mSpinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FILTRAR_POR = mSpinner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void calldialog(final Cliente_SqliteBean cliente) {
        new MaterialDialog.Builder(this)
                .title(R.string.Menuopcoes)
                .items(R.array.opcoes_venda)
                .content("Você pode escolher entre realizar uma venda para " + cliente.getCli_nome() + " ou alterar dados do mesmo.")
                .iconRes(R.drawable.cmdvic)
                .cancelable(false)
                //.itemsDisabledIndices(cliente.getCli_codigo() = 10 ?  1 : 0 )
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                        switch (which) {
                            case 0:
                                Intent VenderProduto = new Intent(getBaseContext(), VenderProduto.class);
                                VenderProduto.putExtra("CLI_CODIGO", cliente.getCli_codigo());
                                startActivity(VenderProduto);
                                finish();
                                break;

                            case 1:
                                Intent CadastraCliente = new Intent(getBaseContext(), CadastraCliente.class);
                                CadastraCliente.putExtra("CLI_CODIGO", cliente.getCli_codigo());
                                CadastraCliente.putExtra("ALTERAR", true);
                                startActivity(CadastraCliente);
                                finish();
                                break;
                        }
                        return true;
                    }
                })

                .positiveText("ok")
                .show();
    }


    public void getSearch() {

        clistener = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                String field = "";

                switch (FILTRAR_POR) {
                    case NOME_CLIENTE:
                        field = "cli_nome";
                        break;

                    case BAIRRO_CLIENTE:
                        field = "cli_bairro";
                        break;

                    case ENDERECO_CLIENTE:
                        field = "cli_endereco";
                        break;

                    case FANTASIA_CLIENTE:
                        field = "cli_fantasia";
                        break;

                }
                mCliente_sqliteBeenList = new Cliente_SqliteDao(getApplicationContext()).getAll(query, field);
                mClienteAdapter = new ClienteAdapter(mCliente_sqliteBeenList, ListaClientesVenda.this);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ListaClientesVenda.this));
                mRecyclerView.setAdapter(mClienteAdapter);
                mClienteAdapter.notifyDataSetChanged();
                return true;
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        mCliente_sqliteBeenList = new Cliente_SqliteDao(getApplicationContext()).getAll("","cli_nome");
        mClienteAdapter = new ClienteAdapter(mCliente_sqliteBeenList, ListaClientesVenda.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListaClientesVenda.this));
        mRecyclerView.setAdapter(mClienteAdapter);
        mClienteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
