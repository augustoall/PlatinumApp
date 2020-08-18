package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.R;

/**
 * Created by JAVA-PC on 12/05/2017.
 */




// esta classe nao esta sendo usada no projeto... tentei usar ela dentro do click do recyclerview mas nao deu certo



public class HistoricoPagViewHolders extends RecyclerView.ViewHolder {

    public TextView txv_histparcela;
    public TextView txv_histvalorparc;
    public TextView txv_histvalorpago;
    public TextView txv_histdatapagamento;
    public TextView txv_histrestante;


    public HistoricoPagViewHolders(View itemView) {
        super(itemView);

        txv_histparcela = (TextView) itemView.findViewById(R.id.txv_histparcela);
        txv_histvalorparc = (TextView) itemView.findViewById(R.id.txv_histvalorparc);
        txv_histvalorpago = (TextView) itemView.findViewById(R.id.txv_histvalorpago);
        txv_histdatapagamento = (TextView) itemView.findViewById(R.id.txv_histdatapagamento);
        txv_histrestante = (TextView) itemView.findViewById(R.id.txv_histrestante);
    }


}
