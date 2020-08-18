package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.R;

/**
 * Created by JAVA-NOT-I3 on 21/09/2017.
 */

public class ListaVendasRealizadasViewHolders extends RecyclerView.ViewHolder {

    public TextView listavendas_cliente;
    public TextView listavendas_datavenda;
    public TextView listavendas_valorvenda;
    public TextView listavendas_fpagamento;
    public TextView listavendas_status;

    public ListaVendasRealizadasViewHolders(View itemView) {
        super(itemView);

        listavendas_cliente = (TextView)itemView.findViewById(R.id.listavendas_cliente);
        listavendas_datavenda = (TextView)itemView.findViewById(R.id.listavendas_datavenda);
        listavendas_valorvenda = (TextView)itemView.findViewById(R.id.listavendas_valorvenda);
        listavendas_fpagamento = (TextView)itemView.findViewById(R.id.listavendas_fpagamento);
        listavendas_status = (TextView)itemView.findViewById(R.id.listavendas_status);

    }
}
