package br.com.app.platinumapp.appvendas.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import br.com.app.platinumapp.appvendas.Adapters.ListaItemsDaPesquisaProdutos;
import br.com.app.platinumapp.appvendas.Adapters.ListaProdutoAdapter;
import br.com.app.platinumapp.appvendas.Model.ConfPagamento_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.VendaD_SqliteBeanTEMP;
import br.com.app.platinumapp.appvendas.Model.VendaD_SqliteDaoTEMP;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.RecyclerTouchListener;
import br.com.app.platinumapp.appvendas.Util.Util;


public class AddProdutoVenda extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    public static final String DESCRICAOPRD = "Pesquisa produto por Descricao";
    public static final String CATEGPRD = "Pesquisa produto por Categoria";
    public static final String EANPRD = "Pesquisa produto por Codigo de Barras";
    public static final String CODPRD = "Pesquisa produto por Codigo";
    private ListaProdutoAdapter mProdutoAdapter;
    private RecyclerView mRecyclerView;
    private SearchView.OnQueryTextListener clistener;
    private List<Produto_SqliteBean> mProduto_SqliteBeanList = new ArrayList<>();
    private Spinner mSpinner;
    private List<String> mListString = new ArrayList<>();
    private ArrayAdapter<String> mArrayAdapter;
    private String FILTRAR_POR;
    private SearchView search;
    private LayoutInflater inflater;
    private AlertDialog.Builder alerta;
    private VendaD_SqliteBeanTEMP ProdutoBeanTemp;
    private VendaD_SqliteDaoTEMP ProdutoDaoTemp;
    private Configuracoes_SqliteBean mConfiguracoes_sqliteBean;
    private Intent INTENT_CLI_CODIGO;
    private int CODIGO_CLIENTE;
    private List<VendaD_SqliteBeanTEMP> lista_de_todos_items_da_venda = new ArrayList<>();
    private AlertDialog alertaDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText txt_dlg_quantidade_informada;
    private View positiveAction;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.addprodutovenda);

        mSpinner = (Spinner) findViewById(R.id.mSpinnerprod);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recyclerviewprod);
        search = (SearchView) findViewById(R.id.my_searchviewprod);

        mListString.add(DESCRICAOPRD);
        mListString.add(CATEGPRD);
        mListString.add(EANPRD);
        mListString.add(CODPRD);

        mConfiguracoes_sqliteBean = new Configuracoes_SqliteDao(getApplicationContext()).BuscaParamentrosEmpresa();
        ProdutoBeanTemp = new VendaD_SqliteBeanTEMP();
        ProdutoDaoTemp = new VendaD_SqliteDaoTEMP(getApplicationContext());

        INTENT_CLI_CODIGO = getIntent();
        CODIGO_CLIENTE = INTENT_CLI_CODIGO.getIntExtra("CODIGO_CLIENTE", 0);

        // populando o spinner com um array de strings
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mListString);
        mSpinner.setAdapter(mArrayAdapter);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProduto_SqliteBeanList = new Produto_SqliteDao(getApplicationContext()).getAll("prd_descricao", "");
        mProdutoAdapter = new ListaProdutoAdapter(mProduto_SqliteBeanList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mProdutoAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Produto_SqliteBean produto = mProduto_SqliteBeanList.get(position);
                informa_produto_na_venda_dialog(produto);
            }

            @Override
            public void onLongClick(View view, int position) {
                inflater_lista_itens_comprados();
            }
        }));

        getSearch();

        search.setOnQueryTextListener(clistener);
        mSpinner.setOnItemSelectedListener(this);

        final Button btn_concluir = (Button) findViewById(R.id.btn_concluir);
        btn_concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("CLI_CODIGO", CODIGO_CLIENTE);
                Intent VenderProduto = new Intent();
                VenderProduto.putExtras(bundle);
                setResult(RESULT_OK, VenderProduto);
                finish();
            }
        });
    }


    private void inflater_lista_itens_comprados() {

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.inflater_lista_dos_itens_comprados, null);
        alerta = new AlertDialog.Builder(this);

        lista_de_todos_items_da_venda = new VendaD_SqliteDaoTEMP(getApplicationContext()).busca_todos_items_na_vendad();
        BigDecimal soma = BigDecimal.ZERO;
        for (VendaD_SqliteBeanTEMP item : lista_de_todos_items_da_venda) {
            soma = soma.add(item.getVendad_precovenda().multiply(item.getVendad_quantidade()));
        }

        alerta.setTitle("Total.: " + soma.setScale(2, RoundingMode.HALF_UP).toString());
        alerta.setView(view);

        final ListView listview_dos_itens_comprados = (ListView) view.findViewById(R.id.listview_dos_itens_comprados);


        if (lista_de_todos_items_da_venda.isEmpty()) {
            Util.Mensagem(getApplicationContext(), "nenhum item foi comprado", Util.THEME_CMDV);
            return;
        }

        listview_dos_itens_comprados.setAdapter(new ListaItemsDaPesquisaProdutos(getApplicationContext(), lista_de_todos_items_da_venda));


        view.findViewById(R.id.btn_fecharitenscomprados).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                alertaDialog.dismiss();

            }
        });

        alertaDialog = alerta.create();
        alertaDialog.show();

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
                    case DESCRICAOPRD:
                        field = "prd_descricao";
                        break;

                    case CATEGPRD:
                        field = "prd_categoria";
                        break;

                    case EANPRD:
                        field = "prd_EAN";
                        break;

                    case CODPRD:
                        field = "prd_codigo";
                        break;

                }

                mProduto_SqliteBeanList = new Produto_SqliteDao(getApplicationContext()).getAll(field, query.trim());
                mProdutoAdapter = new ListaProdutoAdapter(mProduto_SqliteBeanList, AddProdutoVenda.this);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRecyclerView.setAdapter(mProdutoAdapter);
                mProdutoAdapter.notifyDataSetChanged();

                return true;
            }
        };


    }


    private void informa_produto_na_venda_dialog(final Produto_SqliteBean produto) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.dlg_adiciona_produto)
                .customView(R.layout.dlg_addprodutovenda, true)
                .positiveText(R.string.dlg_positiveText_confirmar)
                .negativeText(R.string.dlg_negativeText_cancelar)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        String VENDER_SEM_ESTOQUE = mConfiguracoes_sqliteBean.getVENDER_SEM_ESTOQUE();
                        Double QUANTIDADE_ESTOQUE = produto.getPrd_quant().doubleValue();
                        Double QUANTIDADE_DIGITADA = Double.parseDouble(txt_dlg_quantidade_informada.getText().toString().replace(',', '.'));
                        txt_dlg_quantidade_informada = (EditText) dialog.getCustomView().findViewById(R.id.txt_dlg_quantidade_informada);


                        if (QUANTIDADE_DIGITADA <= 0) {
                            Util.Mensagem(AddProdutoVenda.this, "quantidade não informada", Util.THEME_CMDV);
                            return;
                        }

                        // 10 / 5
                        if (QUANTIDADE_DIGITADA > QUANTIDADE_ESTOQUE && VENDER_SEM_ESTOQUE.equals("N")) {
                            Util.Mensagem(AddProdutoVenda.this, "configure o app para vender o produto com estoque negativo", Util.THEME_CMDV);
                            return;
                        }

                        VendaD_SqliteBeanTEMP item_na_venda = new VendaD_SqliteBeanTEMP();
                        item_na_venda.setVendad_codigo_produto(produto.getPrd_codigo());
                        ProdutoBeanTemp = ProdutoDaoTemp.busca_item_vendaD(item_na_venda);

                        // verificando se este produto ja esta adicionado
                        if (ProdutoBeanTemp != null) {
                            Util.Mensagem(AddProdutoVenda.this, ProdutoBeanTemp.getVendad_descricao_produto() + " ja foi adicionado", Util.THEME_CMDV);
                            return;
                        }

                        Produto_SqliteBean add_produto = new Produto_SqliteBean();
                        add_produto.setPrd_EAN(produto.getPrd_EAN().toString());
                        add_produto.setPrd_codigo(produto.getPrd_codigo());
                        add_produto.setPrd_descricao(produto.getPrd_descricao().toString());
                        add_produto.setPrd_preco(produto.getPrd_preco());
                        adiciona_um_produto_na_venda(add_produto, QUANTIDADE_DIGITADA);
                    }
                })
                .build();

        TextView txv_dlg_descricao = (TextView) dialog.getCustomView().findViewById(R.id.txv_dlg_descricao);
        txv_dlg_descricao.setText(produto.getPrd_descricao().toString());

        TextView txv_dlg_preco = (TextView) dialog.getCustomView().findViewById(R.id.txv_dlg_preco);
        txv_dlg_preco.setText(produto.getPrd_preco().setScale(2, RoundingMode.HALF_UP).toString());

        TextView txv_dlg_estoque = (TextView) dialog.getCustomView().findViewById(R.id.txv_dlg_estoque);
        txv_dlg_estoque.setText(produto.getPrd_quant().setScale(2).toString());

        if (produto.getPrd_quant().doubleValue() <= 20) {
            txv_dlg_estoque.setTextColor(Color.parseColor("#DD2C00"));
        }

        txt_dlg_quantidade_informada = (EditText) dialog.getCustomView().findViewById(R.id.txt_dlg_quantidade_informada);
        txt_dlg_quantidade_informada.requestFocus();

        // determina qual o botao de acao positiva , botao OK
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

        txt_dlg_quantidade_informada.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // listener pra detectar se existe algo no campo
                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


        int widgetColor = ThemeSingleton.get().widgetColor;

        MDTintHelper.setTint(
                (EditText) txt_dlg_quantidade_informada,
                widgetColor == 0 ? ContextCompat.getColor(this, R.color.colorAccent) : widgetColor);

        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }


    private void adiciona_um_produto_na_venda(Produto_SqliteBean produto, double quantidade) {
        VendaD_SqliteBeanTEMP vendaTemp = new VendaD_SqliteBeanTEMP();
        vendaTemp.setVendad_ean(produto.getPrd_EAN());
        vendaTemp.setVendad_codigo_produto(produto.getPrd_codigo());
        vendaTemp.setVendad_descricao_produto(produto.getPrd_descricao());
        vendaTemp.setVendad_quantidade(new BigDecimal(quantidade));
        vendaTemp.setVendad_total(new BigDecimal(quantidade).multiply(produto.getPrd_preco()));
        vendaTemp.setVendad_precovenda(produto.getPrd_preco());
        new VendaD_SqliteDaoTEMP(getApplicationContext()).insere_um_item_na_venda(vendaTemp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("script", "onResume called");
        mProduto_SqliteBeanList = new Produto_SqliteDao(getApplicationContext()).getAll("prd_descricao", "");
        mProdutoAdapter = new ListaProdutoAdapter(mProduto_SqliteBeanList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mProdutoAdapter);
        mProdutoAdapter.notifyDataSetChanged();
    }


    public void calldialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.Menuopcoes)
                .items(R.array.opcoes_venda_addprodutovenda)
                .content("você esta saindo sem concluir o pedido... o que deseja fazer ?")
                .iconRes(R.drawable.cmdvic)
                .cancelable(false)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                        Bundle bundle = new Bundle();
                        Intent VenderProduto = new Intent();

                        switch (which) {

                            case 0:
                                dialog.dismiss();
                                break;

                            case 1:
                                new VendaD_SqliteDaoTEMP(getApplicationContext()).exclui_itens_da_venda();
                                new ConfPagamento_SqliteDao(getApplicationContext()).p_excluir_confpagamento();

                                bundle.putInt("CLI_CODIGO", CODIGO_CLIENTE);
                                VenderProduto.putExtras(bundle);
                                setResult(RESULT_OK, VenderProduto);

                                finish();
                                break;

                            case 2:

                                bundle.putInt("CLI_CODIGO", CODIGO_CLIENTE);
                                VenderProduto.putExtras(bundle);
                                setResult(RESULT_OK, VenderProduto);

                                finish();
                                break;
                        }
                        return true;
                    }
                })

                .positiveText("Confirmar")
                .show();
    }


    @Override
    public void onBackPressed() {

        lista_de_todos_items_da_venda = new VendaD_SqliteDaoTEMP(getApplicationContext()).busca_todos_items_na_vendad();

        if (lista_de_todos_items_da_venda.size() > 0) {
            calldialog();
        } else {

            Bundle bundle = new Bundle();
            bundle.putInt("CLI_CODIGO", CODIGO_CLIENTE);
            Intent VenderProduto = new Intent();
            VenderProduto.putExtras(bundle);
            setResult(RESULT_OK, VenderProduto);
            finish();

        }
    }
}
