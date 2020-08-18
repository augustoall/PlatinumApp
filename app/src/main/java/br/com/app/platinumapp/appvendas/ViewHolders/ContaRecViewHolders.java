package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.R;

/**
 * Created by JAVA-PC on 09/05/2017.
 */

public class ContaRecViewHolders extends RecyclerView.ViewHolder {

    public TextView txv_parcela;
    public TextView txv_vencimento;
    public TextView txv_valorreceber;
    public TextView txv_valorpago;
    public CheckBox chkSelected;

    public Receber_SqliteBean mReceber_sqliteBean;

    public ContaRecViewHolders(View itemView) {
        super(itemView);

        txv_parcela = (TextView) itemView.findViewById(R.id.txv_parcela);
        txv_vencimento = (TextView) itemView.findViewById(R.id.txv_vencimento);
        txv_valorreceber = (TextView) itemView.findViewById(R.id.txv_valorreceber);
        txv_valorpago = (TextView) itemView.findViewById(R.id.txv_valorpago);
        chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);

    }



}
