/**
 *
 */
package br.com.app.platinumapp.appvendas.Controller;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import br.com.app.platinumapp.appvendas.Adapters.ClienteAdapter;
import br.com.app.platinumapp.appvendas.Adapters.ListaPedidosDoClienteAdapter;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.VendaC_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.VendaDAO;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.RecyclerTouchListener;
import br.com.app.platinumapp.appvendas.Util.Util;


public class ListaClientesReceber extends AppCompatActivity implements Spinner.OnItemSelectedListener {

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

    private LayoutInflater inflater;
    private AlertDialog.Builder alerta;
    private AlertDialog alertaDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

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


        mCliente_sqliteBeenList = new Cliente_SqliteDao(this).getAll(" ", "cli_nome");
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



    public void calldialog(final Cliente_SqliteBean cliente) {

        new MaterialDialog.Builder(this)
                .title(R.string.Menuopcoes)
                .content("Você pode escolher entre ver os débitos e pedidos de "+cliente.getCli_nome()+" ou alterar dados do mesmo.")
                .items(R.array.opcoes_receber)
                .iconRes(R.drawable.cmdvic)
                .cancelable(false)
                //.itemsDisabledIndices(agendaBean.getAgd_visitado().equals("S") ?  1 : 0 )
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                        switch (which) {
                            case 0:
                                inflater_lista_pedidos_cliente(cliente.getCli_codigo());
                              //  finish();
                                break;

                            case 1:
                                Intent CadastraCliente = new Intent(getBaseContext(), br.com.app.platinumapp.appvendas.Importacoes.CadastraCliente.class);
                                CadastraCliente.putExtra("CLI_CODIGO", cliente.getCli_codigo());
                                CadastraCliente.putExtra("ALTERAR", true);
                                startActivity(CadastraCliente);
                                finish();
                                break;
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FILTRAR_POR = mSpinner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                mClienteAdapter = new ClienteAdapter(mCliente_sqliteBeenList, ListaClientesReceber.this);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ListaClientesReceber.this));
                mRecyclerView.setAdapter(mClienteAdapter);
                mClienteAdapter.notifyDataSetChanged();
                return true;
            }
        };


    }


    private void inflater_lista_pedidos_cliente(Integer cli_codigo) {

        Cliente_SqliteBean cliente_codigo = new Cliente_SqliteDao(getApplicationContext()).BuscarClientePorCli_codigo(cli_codigo);


        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.inflater_lista_dos_titulos_a_receber, null);
        alerta = new Builder(this);
        alerta.setTitle("Pedido(s) de: " + cliente_codigo.getCli_fantasia());
        alerta.setView(view);

        final ListView listView_titulos_receber = (ListView) view.findViewById(R.id.lista_titulos_receber);

        VendaDAO vendaDao = new VendaDAO(getApplicationContext());

        List<VendaC_SqliteBean> lista_de_pedidos = vendaDao.lista_pedidos_do_cliente(cli_codigo);

        if (lista_de_pedidos.isEmpty()) {
            Util.Mensagem(getApplicationContext(), "Cliente sem pedidos", Util.THEME_CMDV);
            return;
        }


        listView_titulos_receber.setAdapter(new ListaPedidosDoClienteAdapter(getApplicationContext(), lista_de_pedidos));

        listView_titulos_receber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                VendaC_SqliteBean venda = (VendaC_SqliteBean) listView.getAdapter().getItem(position);


                Intent BaixaContasReceber = new Intent(getBaseContext(), BaixaContasReceber.class);
                BaixaContasReceber.putExtra("CLI_CODIGO", venda.getVendac_cli_codigo());
                BaixaContasReceber.putExtra("CHAVE_VENDA", venda.getVendac_chave());
                startActivity(BaixaContasReceber);


            }
        });


        view.findViewById(R.id.btn_fechartitulosreceber).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                alertaDialog.dismiss();

            }
        });

        alertaDialog = alerta.create();
        alertaDialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        mCliente_sqliteBeenList = new Cliente_SqliteDao(getApplicationContext()).getAll(" ", "cli_nome");
        mClienteAdapter = new ClienteAdapter(mCliente_sqliteBeenList, ListaClientesReceber.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListaClientesReceber.this));
        mRecyclerView.setAdapter(mClienteAdapter);
        mClienteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
