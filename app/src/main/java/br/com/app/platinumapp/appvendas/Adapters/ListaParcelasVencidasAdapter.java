package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.RoundingMode;
import java.util.List;

import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.ViewHolders.ListaParcelasVencidasViewHolders;


public class ListaParcelasVencidasAdapter extends RecyclerView.Adapter<ListaParcelasVencidasViewHolders> {

    private List<Receber_SqliteBean> mReceber_sqliteBeanList;
    private Context ctx;

    public ListaParcelasVencidasAdapter(List<Receber_SqliteBean> mReceber_sqliteBeanList, Context ctx) {
        this.mReceber_sqliteBeanList = mReceber_sqliteBeanList;
        this.ctx = ctx;
    }

    @Override
    public ListaParcelasVencidasViewHolders onCreateViewHolder(ViewGroup viewGroup, int i) {
        View textview_item = LayoutInflater.from(ctx).inflate(R.layout.lista_parcelas_vencidas_row, null);
        textview_item.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        ListaParcelasVencidasViewHolders view_item = new ListaParcelasVencidasViewHolders(textview_item);
        return view_item;
    }

    @Override
    public void onBindViewHolder(ListaParcelasVencidasViewHolders holder, int i) {
        holder.txt_lista_parcelas_vencidas_rec_cli_nome.setText("Cliente.: " + mReceber_sqliteBeanList.get(i).getRec_cli_nome());
        holder.txt_lista_parcelas_vencidas_rec_datavecimento.setText("Venc.: " + mReceber_sqliteBeanList.get(i).getRec_datavencimento());
        holder.txt_lista_parcelas_vencidas_rec_valorreceber.setText("Valor.: R$" + mReceber_sqliteBeanList.get(i).getRec_valorreceber().setScale(2, RoundingMode.HALF_UP).toString());
    }

    @Override
    public int getItemCount() {
        return mReceber_sqliteBeanList.size();
    }
}
