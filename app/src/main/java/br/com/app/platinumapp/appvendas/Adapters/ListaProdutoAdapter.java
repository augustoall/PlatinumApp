package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.RoundingMode;
import java.util.List;

import br.com.app.platinumapp.appvendas.Model.Produto_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.ViewHolders.ProdutoViewHolders;

public class ListaProdutoAdapter extends RecyclerView.Adapter<ProdutoViewHolders> {

    private List<Produto_SqliteBean> mListProduto_sqliteBeen;
    private Context ctx;

    public ListaProdutoAdapter(List<Produto_SqliteBean> mListProduto_sqliteBeen, Context ctx) {
        this.mListProduto_sqliteBeen = mListProduto_sqliteBeen;
        this.ctx = ctx;
    }

    @Override
    public ProdutoViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View textview_item = LayoutInflater.from(ctx).inflate(R.layout.produto_row, null);
        textview_item.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        ProdutoViewHolders produto = new ProdutoViewHolders(textview_item);
        return produto;
    }

    @Override
    public void onBindViewHolder(ProdutoViewHolders produtoViewHolders, int position) {
        produtoViewHolders.prd_codigo.setText("( " + mListProduto_sqliteBeen.get(position).getPrd_codigo().toString() + " )  EAN.: " + mListProduto_sqliteBeen.get(position).getPrd_EAN());
        produtoViewHolders.prd_descricao.setText(mListProduto_sqliteBeen.get(position).getPrd_descricao());
        produtoViewHolders.prd_precovenda.setText(mListProduto_sqliteBeen.get(position).getPrd_preco().setScale(2, RoundingMode.HALF_UP).toString());
    }

    @Override
    public int getItemCount() {
        return mListProduto_sqliteBeen.size();
    }
}
