package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.R;

/**
 * Created by JAVA-NOT-I3 on 25/09/2017.
 */

public class ListaParcelasVencidasViewHolders extends RecyclerView.ViewHolder {

    public TextView txt_lista_parcelas_vencidas_rec_cli_nome;
    public TextView txt_lista_parcelas_vencidas_rec_valorreceber;
    public TextView txt_lista_parcelas_vencidas_rec_datavecimento;

    public ListaParcelasVencidasViewHolders(View itemView) {
        super(itemView);

        txt_lista_parcelas_vencidas_rec_cli_nome = (TextView) itemView.findViewById(R.id.txt_lista_parcelas_vencidas_rec_cli_nome);
        txt_lista_parcelas_vencidas_rec_valorreceber = (TextView) itemView.findViewById(R.id.txt_lista_parcelas_vencidas_rec_valorreceber);
        txt_lista_parcelas_vencidas_rec_datavecimento = (TextView) itemView.findViewById(R.id.txt_lista_parcelas_vencidas_rec_datavecimento);


    }
}
