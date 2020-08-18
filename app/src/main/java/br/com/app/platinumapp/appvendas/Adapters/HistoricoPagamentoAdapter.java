package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;
import br.com.app.platinumapp.appvendas.ViewHolders.HistoricoPagViewHolders;


// esta classe nao esta sendo usada no projeto... tentei usar ela dentro do click do recyclerview mas nao deu certo


public class HistoricoPagamentoAdapter extends RecyclerView.Adapter<HistoricoPagViewHolders> {

    private List<HistoricoPagamento_SqliteBean> mListHistoricoPagamento_sqliteBeen;
    private Context ctx;

    public HistoricoPagamentoAdapter(List<HistoricoPagamento_SqliteBean> mListHistoricoPagamento_sqliteBeen, Context ctx) {
        this.mListHistoricoPagamento_sqliteBeen = mListHistoricoPagamento_sqliteBeen;
        this.ctx = ctx;
    }

    @Override
    public HistoricoPagViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_row, null);
        // WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        HistoricoPagViewHolders viewHolder = new HistoricoPagViewHolders(itemLayoutView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final HistoricoPagViewHolders view_item, int position) {
        view_item.txv_histparcela.setText(mListHistoricoPagamento_sqliteBeen.get(position).getHist_numero_parcela().toString());
        view_item.txv_histvalorparc.setText(Util.FormataDataDDMMAAAA(mListHistoricoPagamento_sqliteBeen.get(position).getHist_valor_real_parcela().toString()));
        view_item.txv_histvalorpago.setText(mListHistoricoPagamento_sqliteBeen.get(position).getHist_valor_pago_no_dia().toString());
        view_item.txv_histdatapagamento.setText(mListHistoricoPagamento_sqliteBeen.get(position).getHist_datapagamento().toString());
        view_item.txv_histrestante.setText(mListHistoricoPagamento_sqliteBeen.get(position).getHist_restante_a_pagar().toString());

    }

    @Override
    public int getItemCount() {
        return mListHistoricoPagamento_sqliteBeen.size();
    }
}
