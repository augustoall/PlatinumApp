package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.app.platinumapp.appvendas.Model.CatalogoProdutoModel;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.ViewHolders.CatalogoProdutosViewHolders;

/**
 * Created by JAVA-NOT-I3 on 14/09/2017.
 */

public class CatalogoProdutosAdapter extends RecyclerView.Adapter<CatalogoProdutosViewHolders> {

    private ArrayList<CatalogoProdutoModel> catalogos_list;
    private Context ctx;

    public CatalogoProdutosAdapter(ArrayList<CatalogoProdutoModel> catalogos_list, Context context) {
        this.catalogos_list = catalogos_list;
        this.ctx = context;
    }


    @Override
    public CatalogoProdutosViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View textview_item = LayoutInflater.from(ctx).inflate(R.layout.catalogo_produto_row, null);
        //buscando o tamanho de width e height no xml
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        textview_item.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CatalogoProdutosViewHolders view_item = new CatalogoProdutosViewHolders(textview_item);
        return view_item;
    }

    @Override
    public void onBindViewHolder(CatalogoProdutosViewHolders holder, int position) {
        holder.txvCodigo.setText("Código.: "+catalogos_list.get(position).getPrd_codigo());
        holder.txvDescricao.setText("Descrição.: "+catalogos_list.get(position).getPrd_descricao());
        holder.txvPreco.setText("Preço.: "+catalogos_list.get(position).getPrd_preco());
        Picasso.with(ctx).load(catalogos_list.get(position).getImg_url()).into(holder.img_produto);
    }

    @Override
    public int getItemCount() {
        return catalogos_list.size();
    }
}
