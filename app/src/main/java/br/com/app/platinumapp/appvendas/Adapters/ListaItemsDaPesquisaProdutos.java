package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.util.List;

import br.com.app.platinumapp.appvendas.Model.VendaD_SqliteBeanTEMP;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class ListaItemsDaPesquisaProdutos extends BaseAdapter {

	private Context ctx;
	private static List<VendaD_SqliteBeanTEMP> items_temporarios;

	public ListaItemsDaPesquisaProdutos(Context ctx, List<VendaD_SqliteBeanTEMP> items_temps) {
		this.items_temporarios = items_temps;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {

		return items_temporarios.size();
	}

	@Override
	public Object getItem(int position) {
		return items_temporarios.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		VendaD_SqliteBeanTEMP venda = (VendaD_SqliteBeanTEMP) getItem(position);

		LayoutInflater layout = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = layout.inflate(R.layout.inflater_produtos_vendidos, null);

		TextView NomeProduto = (TextView) v.findViewById(R.id.inflaterDescricaoProduto);
		TextView Quantidade = (TextView) v.findViewById(R.id.inflaterQuantidadeProduto);
		TextView Preco = (TextView) v.findViewById(R.id.inflaterPrecoProduto);
		TextView totalPrd = (TextView) v.findViewById(R.id.inflaterTotalProduto);

		NomeProduto.setTypeface(Util.SetmyTypeface(ctx, Util.FONT_TYPEFACE_RobotoCondensedBold));
		Quantidade.setTypeface(Util.SetmyTypeface(ctx, Util.FONT_TYPEFACE_RobotoCondensedBold));
		Preco.setTypeface(Util.SetmyTypeface(ctx, Util.FONT_TYPEFACE_RobotoCondensedBold));
		totalPrd.setTypeface(Util.SetmyTypeface(ctx, Util.FONT_TYPEFACE_RobotoCondensedBold));

		NomeProduto.setTextColor(Color.BLACK);
		Quantidade.setTextColor(Color.BLACK);
		Preco.setTextColor(Color.BLACK);
		totalPrd.setTextColor(Color.BLACK);

		NomeProduto.setText(venda.getVendad_descricao_produto());
		Quantidade.setText(String.valueOf(venda.getVendad_quantidade()));
		Preco.setText(venda.getVendad_precovenda().setScale(2, RoundingMode.HALF_UP).toString());
		totalPrd.setText(venda.getSubtotal().setScale(2, RoundingMode.HALF_UP).toString());

		return v;
	}
}
