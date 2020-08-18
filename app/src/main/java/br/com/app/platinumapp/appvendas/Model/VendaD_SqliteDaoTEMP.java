package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class VendaD_SqliteDaoTEMP {

    private Context ctx;
    private String Sql;
    private boolean Gravacao;
    private boolean AchouProduto;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public VendaD_SqliteDaoTEMP(Context ctx) {
        this.ctx = ctx;
    }


    // ****REGRAS__VENDAD************REGRAS__VENDAD************************REGRAS__VENDAD*****//
    // ********************************************REGRAS__VENDAD*****************************//

    // REGRAS__VENDAD

    public boolean insere_um_item_na_venda(VendaD_SqliteBeanTEMP item) {

        db = new Db(ctx).getWritableDatabase();
        Gravacao = false;
        Sql = "insert into VENDAD_TEMP (vendad_ean,vendad_codigo_produto,vendad_descricao_produto,vendad_quantidade,vendad_precovenda,vendad_total) values (?,?,?,?,?,?)";

        try {

            stmt = db.compileStatement(Sql);
            stmt.bindString(1, item.getVendad_ean());
            stmt.bindLong(2, item.getVendad_codigo_produto());
            stmt.bindString(3, item.getVendad_descricao_produto());
            stmt.bindDouble(4, item.getVendad_quantidade().doubleValue());
            stmt.bindDouble(5, item.getVendad_precovenda().doubleValue());
            stmt.bindDouble(6, item.getSubtotal().doubleValue());

            if (stmt.executeInsert() > 0) {
                Gravacao = true;
                Sql = "";
            }

            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("insere_um_item_na_venda", e.getMessage());
            Gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }

        return Gravacao;

    }

    // ****************************************************************

    public void exclui_um_item_da_venda(VendaD_SqliteBeanTEMP item) {

        db = new Db(ctx).getWritableDatabase();
        try {
            db.delete("VENDAD_TEMP", "vendad_codigo_produto=?", new String[]{item.getVendad_codigo_produto().toString()});
        } catch (SQLiteException e) {
            Log.d("exclui_um_item_da_venda", "erro exclui_um_item_da_venda " + e.getMessage());
        } finally {
            db.close();
        }

    }

    public void exclui_itens_da_venda() {
        db = new Db(ctx).getWritableDatabase();
        try {
            db.delete("VENDAD_TEMP", null, null);
        } catch (SQLiteException e) {
            Log.d("exclui_itens_da_venda", "erro exclui_um_itens_da_venda " + e.getMessage());
        } finally {
            db.close();
        }

    }


    // ****************************************************************

    public VendaD_SqliteBeanTEMP busca_item_vendaD(VendaD_SqliteBeanTEMP produto) {

        VendaD_SqliteBeanTEMP item = null;

        db = new Db(ctx).getReadableDatabase();

        try {

            String sql = " select * from VENDAD_TEMP where vendad_codigo_produto = ? ";

            cursor = db.rawQuery(sql, new String[]{produto.getVendad_codigo_produto().toString()});

            if (cursor.moveToFirst()) {

                item = new VendaD_SqliteBeanTEMP();

                item.setVendad_ean(cursor.getString(cursor.getColumnIndex(item.CODIGO_DE_BARRAS)));
                item.setVendad_codigo_produto(cursor.getInt(cursor.getColumnIndex(item.CODIGO_DO_PRODUTO)));
                item.setVendad_descricao_produto(cursor.getString(cursor.getColumnIndex(item.DESCRICAO_PRODUTO)));
                item.setVendad_quantidade(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(item.QUANTIDADE))));
                item.setVendad_precovenda(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(item.PRECO_DE_VENDA))));
                item.setVendad_total(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(item.TOTAL_QTDE_X_PRECOVENDA))));

            }

        } catch (SQLiteException e) {
            Log.d("busca_item_vendaD", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return item;

    }

    // ********************************************************************************


    public List<VendaD_SqliteBeanTEMP> busca_todos_items_na_vendad() {

        List<VendaD_SqliteBeanTEMP> lista = new ArrayList<>();

        db = new Db(ctx).getReadableDatabase();

        try {

            String sql = "select * from VENDAD_TEMP";

            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {

                VendaD_SqliteBeanTEMP item = new VendaD_SqliteBeanTEMP();

                item = new VendaD_SqliteBeanTEMP();


                item.setVendad_ean(cursor.getString(cursor.getColumnIndex(item.CODIGO_DE_BARRAS)));
                item.setVendad_codigo_produto(cursor.getInt(cursor.getColumnIndex(item.CODIGO_DO_PRODUTO)));
                item.setVendad_descricao_produto(cursor.getString(cursor.getColumnIndex(item.DESCRICAO_PRODUTO)));
                item.setVendad_quantidade(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(item.QUANTIDADE))));
                item.setVendad_precovenda(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(item.PRECO_DE_VENDA))));
                item.setVendad_total(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(item.TOTAL_QTDE_X_PRECOVENDA))));

                lista.add(item);

            }

        } catch (SQLiteException e) {
            Log.d("busca_todos_items_na_vendad", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return lista;

    }
    //********************************************************************************


}
