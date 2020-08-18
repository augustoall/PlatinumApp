package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;
import br.com.app.platinumapp.appvendas.ViewHolders.ContaRecViewHolders;


public class ContaRecAdapter extends RecyclerView.Adapter<ContaRecViewHolders> {


    private List<Receber_SqliteBean> myList_contarec = new ArrayList<>();
    private Context ctx;

    public ContaRecAdapter(List<Receber_SqliteBean> mReceber_sqliteBeanList, Context ctx) {
        this.myList_contarec = mReceber_sqliteBeanList;
        this.ctx = ctx;

    }


    @Override
    public ContaRecViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_row, null);

        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        ContaRecViewHolders viewHolder = new ContaRecViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContaRecViewHolders view_item, final int position) {


        if (myList_contarec.get(position).getRec_valor_pago().doubleValue() > 0) {

            view_item.txv_parcela.setTextColor(Color.BLUE);
            view_item.txv_parcela.setText(myList_contarec.get(position).getRec_num_parcela().toString());

            view_item.txv_vencimento.setTextColor(Color.BLUE);
            view_item.txv_vencimento.setText(Util.FormataDataDDMMAAAA(myList_contarec.get(position).getRec_datavencimento()));

            view_item.txv_valorreceber.setTextColor(Color.BLUE);
            view_item.txv_valorreceber.setText(myList_contarec.get(position).getRec_valorreceber().toString());

            view_item.txv_valorpago.setTextColor(Color.BLUE);
            view_item.txv_valorpago.setText(myList_contarec.get(position).getRec_valor_pago().toString());

            view_item.chkSelected.setChecked(myList_contarec.get(position).isSelected());
            view_item.chkSelected.setTag(myList_contarec.get(position));


        } else {

            view_item.txv_parcela.setText(myList_contarec.get(position).getRec_num_parcela().toString());
            view_item.txv_vencimento.setText(Util.FormataDataDDMMAAAA(myList_contarec.get(position).getRec_datavencimento()));
            view_item.txv_valorreceber.setText(myList_contarec.get(position).getRec_valorreceber().toString());
            view_item.txv_valorpago.setText(myList_contarec.get(position).getRec_valor_pago().toString());
            view_item.chkSelected.setChecked(myList_contarec.get(position).isSelected());
            view_item.chkSelected.setTag(myList_contarec.get(position));

        }

        view_item.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CheckBox mCheckBox = (CheckBox) view;
                Receber_SqliteBean conrec = (Receber_SqliteBean) mCheckBox.getTag();
                conrec.setSelected(mCheckBox.isChecked());
                myList_contarec.get(position).setSelected(mCheckBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return myList_contarec.size();
    }

    public List<Receber_SqliteBean> getMyList_contarec() {
        return myList_contarec;
    }


}
