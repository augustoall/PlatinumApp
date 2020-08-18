package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.R;

public class ProdutoViewHolders extends RecyclerView.ViewHolder {

    public TextView prd_codigo;
    public TextView prd_descricao;
    public TextView prd_precovenda;

    public ProdutoViewHolders(View itemView) {
        super(itemView);
        prd_codigo = (TextView) itemView.findViewById(R.id.prd_codigo);
        prd_descricao = (TextView) itemView.findViewById(R.id.prd_descricao);
        prd_precovenda = (TextView) itemView.findViewById(R.id.prd_precovenda);
    }
}