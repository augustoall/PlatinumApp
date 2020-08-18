package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.math.RoundingMode;
import java.util.List;

import br.com.app.platinumapp.appvendas.Model.VendaC_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.ViewHolders.ListaVendasRealizadasViewHolders;

/**
 * Created by JAVA-NOT-I3 on 21/09/2017.
 */

public class ListaVendasRealizadasAdapter extends RecyclerView.Adapter<ListaVendasRealizadasViewHolders> {

    private List<VendaC_SqliteBean> mListVendas;
    private Context ctx;

    public ListaVendasRealizadasAdapter(List<VendaC_SqliteBean> lista, Context ctx) {
        this.mListVendas = lista;
        this.ctx = ctx;
    }

    @Override
    public ListaVendasRealizadasViewHolders onCreateViewHolder(ViewGroup viewGroup, int i) {
        View textview_item = LayoutInflater.from(ctx).inflate(R.layout.lista_vendas_realizadas_row, null);
        //buscando o tamanho de width e height no xml
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        textview_item.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        ListaVendasRealizadasViewHolders view_item = new ListaVendasRealizadasViewHolders(textview_item);
        return view_item;
    }

    @Override
    public void onBindViewHolder(ListaVendasRealizadasViewHolders holders, int position) {

        if (mListVendas.get(position).getVendac_enviada().equals("N")) {
            holders.listavendas_status.setTextColor(Color.RED);
            holders.listavendas_status.setText("Não enviada servidor");
        } else {
            holders.listavendas_status.setTextColor(Color.GREEN);
            holders.listavendas_status.setText("Enviada servidor");
        }
        holders.listavendas_cliente.setText("" + mListVendas.get(position).getVendac_cli_nome().toString());
        holders.listavendas_datavenda.setText("" + mListVendas.get(position).getVendac_datahoravenda().toString());
        holders.listavendas_fpagamento.setText("" + mListVendas.get(position).getVendac_fpgto_tipo().toString());
        holders.listavendas_valorvenda.setText("" + mListVendas.get(position).getVendac_valor().setScale(2, RoundingMode.HALF_UP).toString());
    }

    @Override
    public int getItemCount() {
        return mListVendas.size();
    }
}
