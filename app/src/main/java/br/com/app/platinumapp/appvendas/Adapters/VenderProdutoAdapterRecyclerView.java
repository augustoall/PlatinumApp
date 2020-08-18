package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import java.math.RoundingMode;
import java.util.List;
import br.com.app.platinumapp.appvendas.Model.VendaD_SqliteBeanTEMP;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.ViewHolders.VenderProdutoViewHolders;

/**
 * Created by JAVA-NOT-I3 on 16/09/2017.
 */

public class VenderProdutoAdapterRecyclerView extends RecyclerView.Adapter<VenderProdutoViewHolders> {

    private static List<VendaD_SqliteBeanTEMP> items_temporarios;
    private Context ctx;

    public VenderProdutoAdapterRecyclerView(Context ctx, List<VendaD_SqliteBeanTEMP> items_temps) {
        this.items_temporarios = items_temps;
        this.ctx = ctx;
    }


    @Override
    public VenderProdutoViewHolders onCreateViewHolder(ViewGroup viewGroup, int i) {
        View textview_item = LayoutInflater.from(ctx).inflate(R.layout.venderproduto_recyclerview_row, null);
        //buscando o tamanho de width e height no xml
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        textview_item.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        VenderProdutoViewHolders view_item = new VenderProdutoViewHolders(textview_item);
        return view_item;
    }

    @Override
    public void onBindViewHolder(VenderProdutoViewHolders venderProdutoViewHolders, int i) {
        venderProdutoViewHolders.venderproduto_inflaterDescricaoProduto.setText(items_temporarios.get(i).getVendad_descricao_produto());
        venderProdutoViewHolders.venderproduto_inflaterQuantidadeProduto.setText(items_temporarios.get(i).getVendad_quantidade().toString());
        venderProdutoViewHolders.venderproduto_inflaterPrecoProduto.setText(items_temporarios.get(i).getVendad_precovenda().setScale(2, RoundingMode.UP).toString());
        venderProdutoViewHolders.venderproduto_inflaterTotalProduto.setText(items_temporarios.get(i).getSubtotal().setScale(2, RoundingMode.UP).toString());

    }

    @Override
    public int getItemCount() {
        return items_temporarios.size();
    }
}
