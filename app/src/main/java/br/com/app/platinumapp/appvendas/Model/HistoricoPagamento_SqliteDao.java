package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAVA on 04/04/2015.
 */
public class HistoricoPagamento_SqliteDao {


    private static final String TABLE_NAME = "HISTORICO_PAGAMENTO";
    private Context ctx;
    private String Sql;
    private boolean Gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;


    public HistoricoPagamento_SqliteDao(Context ctx) {
        this.ctx = ctx;
    }


    public boolean grava_historico(HistoricoPagamento_SqliteBean pagamento) {

        db = new Db(ctx).getWritableDatabase();
        Gravacao = false;
        Sql = "insert into HISTORICO_PAGAMENTO (" +
                "hist_numero_parcela," +
                "hist_valor_real_parcela," +
                "hist_valor_pago_no_dia," +
                "hist_restante_a_pagar," +
                "hist_datapagamento," +
                "hist_nomecliente," +
                "hist_pagou_com," +
                "vendac_chave," +
                "hist_enviado) values (?,?,?,?,?,?,?,?,?)";

        try {
            stmt = db.compileStatement(Sql);

            stmt.bindLong(1, pagamento.getHist_numero_parcela());
            stmt.bindDouble(2, pagamento.getHist_valor_real_parcela().doubleValue());
            stmt.bindDouble(3, pagamento.getHist_valor_pago_no_dia().doubleValue());

            stmt.bindDouble(4, pagamento.getHist_restante_a_pagar().doubleValue());
            stmt.bindString(5, pagamento.getHist_datapagamento().toString());
            stmt.bindString(6, pagamento.getHist_nomecliente());

            stmt.bindString(7, pagamento.getHist_pagou_com());
            stmt.bindString(8, pagamento.getVendac_chave());
            stmt.bindString(9, pagamento.getHist_enviado());

            if (stmt.executeInsert() > 0) {
                Gravacao = true;
                Sql = "";
            }

            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("grava_historico", e.getMessage());
            Gravacao = false;
        } finally {
            db.close();

        }

        return Gravacao;

    }



    public List<HistoricoPagamento_SqliteBean> busca_historicos_por_vendac_chave(String CHAVEDAVENDA,String numero_parcela) {
        List<HistoricoPagamento_SqliteBean> historicos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            Sql = "select * from HISTORICO_PAGAMENTO where vendac_chave = '"+ CHAVEDAVENDA + "' and hist_numero_parcela = "+numero_parcela + " order by hist_datapagamento desc ";

            Log.i("script",Sql);
            cursor = db.rawQuery(Sql, null);

            while (cursor.moveToNext()) {

                HistoricoPagamento_SqliteBean hist = new HistoricoPagamento_SqliteBean();

                hist.setHist_codigo(cursor.getInt(cursor.getColumnIndex(hist.CODIGO_HISTORICOPGTO)));
                hist.setHist_numero_parcela(cursor.getInt(cursor.getColumnIndex(hist.NUMERODAPARCELA)));
                hist.setHist_valor_real_parcela(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(hist.VALORREALDAPARCELA))));
                hist.setHist_valor_pago_no_dia(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(hist.VALORPAGONODIA))));

                hist.setHist_restante_a_pagar(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(hist.RESTANTEAPAGAR))));
                hist.setHist_datapagamento(cursor.getString(cursor.getColumnIndex(hist.DATAQUEFOIREALIZADOPAGAMENTO)));
                hist.setHist_nomecliente(cursor.getString(cursor.getColumnIndex(hist.NOMEDOCLIENTE)));

                hist.setHist_pagou_com(cursor.getString(cursor.getColumnIndex(hist.PAGOUCOM)));
                hist.setVendac_chave(cursor.getString(cursor.getColumnIndex(hist.CHAVEDAVENDA)));


                historicos.add(hist);
            }

        } catch (SQLiteException e) {
            Log.d("busca_historicos_de_pagame", e.getMessage());
        } finally {
            db.close();
        }
        return historicos;
    }







    public List<HistoricoPagamento_SqliteBean> busca_historicos_de_pagamento_para_exportar() {
        List<HistoricoPagamento_SqliteBean> historicos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            Sql = "select * from HISTORICO_PAGAMENTO";
            cursor = db.rawQuery(Sql, null);

            while (cursor.moveToNext()) {

                HistoricoPagamento_SqliteBean hist = new HistoricoPagamento_SqliteBean();

                hist.setHist_codigo(cursor.getInt(cursor.getColumnIndex(hist.CODIGO_HISTORICOPGTO)));
                hist.setHist_numero_parcela(cursor.getInt(cursor.getColumnIndex(hist.NUMERODAPARCELA)));
                hist.setHist_valor_real_parcela(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(hist.VALORREALDAPARCELA))));
                hist.setHist_valor_pago_no_dia(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(hist.VALORPAGONODIA))));

                hist.setHist_restante_a_pagar(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(hist.RESTANTEAPAGAR))));
                hist.setHist_datapagamento(cursor.getString(cursor.getColumnIndex(hist.DATAQUEFOIREALIZADOPAGAMENTO)));
                hist.setHist_nomecliente(cursor.getString(cursor.getColumnIndex(hist.NOMEDOCLIENTE)));

                hist.setHist_pagou_com(cursor.getString(cursor.getColumnIndex(hist.PAGOUCOM)));
                hist.setVendac_chave(cursor.getString(cursor.getColumnIndex(hist.CHAVEDAVENDA)));


                historicos.add(hist);
            }

        } catch (SQLiteException e) {
            Log.d("busca_historicos_de_pagame", e.getMessage());
        } finally {
            db.close();
        }
        return historicos;
    }



}


