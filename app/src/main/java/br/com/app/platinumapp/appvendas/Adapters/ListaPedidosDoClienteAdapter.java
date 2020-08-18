package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.util.List;
import br.com.app.platinumapp.appvendas.Model.VendaC_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class ListaPedidosDoClienteAdapter extends BaseAdapter {

	private Context ctx;
	private List<VendaC_SqliteBean> lista_titulos;

	public ListaPedidosDoClienteAdapter(Context c, List<VendaC_SqliteBean> l) {
		this.ctx = c;
		this.lista_titulos = l;

	}

	@Override
	public int getCount() {

		return lista_titulos.size();
	}

	@Override
	public Object getItem(int position) {

		return lista_titulos.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		VendaC_SqliteBean venda = (VendaC_SqliteBean) getItem(position);

		LayoutInflater layout = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = layout.inflate(R.layout.inflater_lista_titulos_a_receber_info, null);

		TextView IdVenda = (TextView) v.findViewById(R.id.txvIdVenda);
		TextView NumeroPedido = (TextView) v.findViewById(R.id.txvNumPedido);
		TextView DataVenda = (TextView) v.findViewById(R.id.txvDataVenda);
		TextView Parcelamento = (TextView) v.findViewById(R.id.txvParcelamento);
		TextView ValorVenda = (TextView) v.findViewById(R.id.txvValorVenda);

		IdVenda.setText(venda.getVendac_id().toString());
		NumeroPedido.setText(venda.getVendac_chave());		
		DataVenda.setText(Util.FormataDataDDMMAAAA_ComHoras(venda.getVendac_datahoravenda()));
		Parcelamento.setText(venda.getVendac_fpgto_tipo());
		ValorVenda.setText(venda.getVendac_valor().setScale(2, RoundingMode.HALF_UP).toString());

		return v;
	}

}
