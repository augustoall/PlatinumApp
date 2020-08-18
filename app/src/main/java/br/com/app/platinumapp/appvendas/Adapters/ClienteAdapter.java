package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;

import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.ViewHolders.ClienteViewHolders;


public class ClienteAdapter extends RecyclerView.Adapter<ClienteViewHolders> {

    private List<Cliente_SqliteBean> mListClieentBean;
    private Context ctx;

    public ClienteAdapter(List<Cliente_SqliteBean> mListClieentBean, Context ctx) {
        this.mListClieentBean = mListClieentBean;
        this.ctx = ctx;
    }

    @Override
    public ClienteViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View textview_item = LayoutInflater.from(ctx).inflate(R.layout.cliente_row, null);
        //buscando o tamanho de width e height no xml
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        textview_item.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        ClienteViewHolders view_item = new ClienteViewHolders(textview_item);
        return view_item;
    }

    @Override
    public void onBindViewHolder(final ClienteViewHolders cliholder, final int position) {
        final Cliente_SqliteBean cli = mListClieentBean.get(position);
        cliholder.cli_codigo.setText(mListClieentBean.get(position).getCli_codigo().toString());
        cliholder.cli_nome.setText(mListClieentBean.get(position).getCli_nome().toString());
        cliholder.cli_fantasia.setText(mListClieentBean.get(position).getCli_fantasia().toString());
        cliholder.cli_contato.setText(mListClieentBean.get(position).getCli_contato1().toString() + " * " + mListClieentBean.get(position).getCli_contato2().toString());
    }

    @Override
    public int getItemCount() {
        return mListClieentBean.size();
    }

}