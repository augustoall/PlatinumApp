package br.com.app.platinumapp.appvendas.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.app.platinumapp.appvendas.R;


public class ClienteViewHolders extends RecyclerView.ViewHolder {

    public TextView cli_codigo;
    public TextView cli_fantasia;
    public TextView cli_nome;
    public TextView cli_contato;
    public ImageView thumbnail;
    public ImageView overflow;

    public ClienteViewHolders(View itemView) {
        super(itemView);

        overflow = (ImageView) itemView.findViewById(R.id.overflow);
        cli_codigo = (TextView) itemView.findViewById(R.id.cli_codigo);
        cli_nome = (TextView) itemView.findViewById(R.id.cli_nome);
        cli_contato = (TextView) itemView.findViewById(R.id.cli_contato);
        cli_fantasia = (TextView) itemView.findViewById(R.id.cli_fantasia);
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);


    }
}
