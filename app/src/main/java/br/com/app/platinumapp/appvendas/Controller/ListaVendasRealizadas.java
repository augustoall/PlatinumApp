package br.com.app.platinumapp.appvendas.Controller;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.app.platinumapp.appvendas.Adapters.ListaVendasRealizadasAdapter;
import br.com.app.platinumapp.appvendas.Model.VendaC_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.VendaDAO;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;
import br.com.app.platinumapp.appvendas.Util.RecyclerTouchListener;

public class ListaVendasRealizadas extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private RecyclerView lista_vendas_recyclerview;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextInputEditText txt_listavendas_dataini;
    private TextInputEditText txt_listavendas_datafim;
    private String DataFim;
    private String DataIni;
    private List<VendaC_SqliteBean> mVendaC_sqliteBeenList;
    private ListaVendasRealizadasAdapter mListaVendasRealizadasAdapter;
    private TextView txv_listavendas_subtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_vendas_realizadas);

        lista_vendas_recyclerview = (RecyclerView) findViewById(R.id.lista_vendas_recyclerview);
        txt_listavendas_dataini = (TextInputEditText) findViewById(R.id.txt_listavendas_dataini);
        txt_listavendas_datafim = (TextInputEditText) findViewById(R.id.txt_listavendas_datafim);
        txv_listavendas_subtotal = (TextView) findViewById(R.id.txv_listavendas_subtotal);

        txt_listavendas_dataini.setOnClickListener(this);
        txt_listavendas_datafim.setOnClickListener(this);

        txt_listavendas_dataini.setOnTouchListener(this);
        txt_listavendas_datafim.setOnTouchListener(this);


        //  addTextChangedListener
        txt_listavendas_datafim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                toDatePickerDialog.show();  // se tentar alterar a data chama o dialog
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_listavendas_dataini.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fromDatePickerDialog.show(); // se tentar alterar a data chama o dialog
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        toDatePickerListener();
        fromDatePickerListener();

        DataIni = "2017-01-01";
        DataFim = "2020-01-01"; 
        mVendaC_sqliteBeenList = new VendaDAO(this).busca_vendas_realizadas(DataIni, DataFim);
        mListaVendasRealizadasAdapter = new ListaVendasRealizadasAdapter(mVendaC_sqliteBeenList, this);

        lista_vendas_recyclerview.setHasFixedSize(true);
        lista_vendas_recyclerview.setAdapter(mListaVendasRealizadasAdapter);
        lista_vendas_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        lista_vendas_recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), lista_vendas_recyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Cliente_SqliteBean cliente = mCliente_sqliteBeenList.get(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        soma_vendas_pesquisadas();
    }

    public void soma_vendas_pesquisadas() {
        BigDecimal total = BigDecimal.ZERO;
        for (VendaC_SqliteBean item : mVendaC_sqliteBeenList) {
            total = total.add(item.getVendac_valor());
        }
        txv_listavendas_subtotal.setText(total.setScale(2, RoundingMode.HALF_UP).toString());
    }

    public void filtrarvendas(View v) {
        mVendaC_sqliteBeenList = new VendaDAO(this).busca_vendas_realizadas(Util.FormataDataAAAAMMDD(DataIni), Util.FormataDataAAAAMMDD(DataFim));

        if (mVendaC_sqliteBeenList.isEmpty()) {
            Util.Mensagem(getApplicationContext(),"Nenhuma venda encontrada para esta data", Util.THEME_CMDV);
        }

        mListaVendasRealizadasAdapter = new ListaVendasRealizadasAdapter(mVendaC_sqliteBeenList, this);
        lista_vendas_recyclerview.setAdapter(mListaVendasRealizadasAdapter);
        lista_vendas_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mListaVendasRealizadasAdapter.notifyDataSetChanged();
        soma_vendas_pesquisadas();
    }

    private void toDatePickerListener() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataescolhida = Calendar.getInstance();
                dataescolhida.set(year, monthOfYear, dayOfMonth);
                DataFim = dateFormatter.format(dataescolhida.getTime());
                txt_listavendas_datafim.setText(dateFormatter.format(dataescolhida.getTime()));
                Log.i("script", "DataFim data escolhida .: " + dateFormatter.format(dataescolhida.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void fromDatePickerListener() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataescolhida = Calendar.getInstance();
                dataescolhida.set(year, monthOfYear, dayOfMonth);
                DataIni = dateFormatter.format(dataescolhida.getTime());
                txt_listavendas_dataini.setText(dateFormatter.format(dataescolhida.getTime()));
                Log.i("script", "DataIni data escolhida .: " + dateFormatter.format(dataescolhida.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_listavendas_dataini:
                fromDatePickerDialog.show();
                break;
            case R.id.txt_listavendas_datafim:
                toDatePickerDialog.show();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.txt_listavendas_dataini:
                fromDatePickerDialog.show();
                break;
            case R.id.txt_listavendas_datafim:
                toDatePickerDialog.show();
                break;

        }
        return true;
    }
}
