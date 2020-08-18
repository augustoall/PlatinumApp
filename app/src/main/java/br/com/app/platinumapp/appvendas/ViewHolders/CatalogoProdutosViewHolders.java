package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.R;

/**
 * Created by JAVA-NOT-I3 on 14/09/2017.
 */

public class CatalogoProdutosViewHolders extends RecyclerView.ViewHolder {

    public TextView txvCodigo;
    public TextView txvDescricao;
    public TextView txvPreco;
    public ImageView img_produto;

    public CatalogoProdutosViewHolders(View itemView) {
        super(itemView);
        txvCodigo = (TextView) itemView.findViewById(R.id.txvCodigo);
        txvDescricao = (TextView) itemView.findViewById(R.id.txvDescricao);
        txvPreco = (TextView) itemView.findViewById(R.id.txvPreco);
        img_produto = (ImageView) itemView.findViewById(R.id.img_produto);
    }
}
