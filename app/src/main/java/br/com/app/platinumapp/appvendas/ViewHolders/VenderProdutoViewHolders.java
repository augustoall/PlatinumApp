package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.R;

/**
 * Created by JAVA-NOT-I3 on 16/09/2017.
 */

public class VenderProdutoViewHolders extends RecyclerView.ViewHolder {

    public TextView venderproduto_inflaterDescricaoProduto;
    public TextView venderproduto_inflaterQuantidadeProduto;
    public TextView venderproduto_inflaterPrecoProduto;
    public TextView venderproduto_inflaterTotalProduto;

    public VenderProdutoViewHolders(View itemView) {
        super(itemView);
        venderproduto_inflaterDescricaoProduto = (TextView) itemView.findViewById(R.id.venderproduto_inflaterDescricaoProduto);
        venderproduto_inflaterQuantidadeProduto = (TextView) itemView.findViewById(R.id.venderproduto_inflaterQuantidadeProduto);
        venderproduto_inflaterPrecoProduto = (TextView) itemView.findViewById(R.id.venderproduto_inflaterPrecoProduto);
        venderproduto_inflaterTotalProduto = (TextView) itemView.findViewById(R.id.venderproduto_inflaterTotalProduto);

    }
}
