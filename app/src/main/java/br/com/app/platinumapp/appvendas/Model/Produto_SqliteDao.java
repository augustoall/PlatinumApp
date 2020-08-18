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

public class Produto_SqliteDao {

    public static final int CODIGOPRODUTO = 1;
    public static final int DESCRICAOPRODUTO = 2;
    public static final int CATEGORIA = 3;
    public static final int CODBARRAS = 4;

    private static final String TABLE_NAME = "PRODUTOS";
    private Context ctx;
    private String Sql;
    private boolean Gravacao;
    private boolean AchouProduto;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public Produto_SqliteDao(Context ctx) {
        this.ctx = ctx;
    }

    public List<Produto_SqliteBean> ListaDeProdutos() {
        List<Produto_SqliteBean> listadeprodutos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {
            cursor = db.rawQuery("select * from produtos order by prd_descricao", null);


            while (cursor.moveToNext()) {
                Produto_SqliteBean prd = new Produto_SqliteBean();

                prd.setPrd_codigo(cursor.getInt(cursor.getColumnIndex(prd.CODIGO_PRODUTO)));
                prd.setPrd_EAN(cursor.getString(cursor.getColumnIndex(prd.CODIGO_BARRAS_EAN_13)));
                prd.setPrd_descricao(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_PRODUTO)));
                prd.setPrd_descr_red(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_REDUZIDA)));
                prd.setPrd_unmed(cursor.getString(cursor.getColumnIndex(prd.UNIDADE_MEDIDA)));
                prd.setPrd_custo(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_CUSTO))));
                prd.setPrd_preco(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_VENDA))));
                prd.setPrd_quant(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.QUANTIDADE_ESTOQUE))));
                prd.setPrd_categoria(cursor.getString(cursor.getColumnIndex(prd.CATEGORIA)));
                listadeprodutos.add(prd);
            }
        } catch (SQLiteException e) {
            Log.d("ListaDeProdutos", e.getMessage());
        } finally {
            db.close();
        }
        return listadeprodutos;
    }


    public List<Produto_SqliteBean> getAll(String field, String query) {
        List<Produto_SqliteBean> listadeprodutos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {
            cursor = db.rawQuery("select * from PRODUTOS where " + field + " like '%" + query + "%'", null);

            while (cursor.moveToNext()) {
                Produto_SqliteBean prd = new Produto_SqliteBean();

                prd.setPrd_codigo(cursor.getInt(cursor.getColumnIndex(prd.CODIGO_PRODUTO)));
                prd.setPrd_EAN(cursor.getString(cursor.getColumnIndex(prd.CODIGO_BARRAS_EAN_13)));
                prd.setPrd_descricao(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_PRODUTO)));
                prd.setPrd_descr_red(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_REDUZIDA)));
                prd.setPrd_unmed(cursor.getString(cursor.getColumnIndex(prd.UNIDADE_MEDIDA)));
                prd.setPrd_custo(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_CUSTO))));
                prd.setPrd_preco(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_VENDA))));
                prd.setPrd_quant(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.QUANTIDADE_ESTOQUE))));
                prd.setPrd_categoria(cursor.getString(cursor.getColumnIndex(prd.CATEGORIA)));
                listadeprodutos.add(prd);
            }
        } catch (SQLiteException e) {
            Log.d("getAll", e.getMessage());
        } finally {
            db.close();
        }
        return listadeprodutos;
    }


    public List<Produto_SqliteBean> getAll() {
        List<Produto_SqliteBean> listadeprodutos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {
            cursor = db.rawQuery("select * from PRODUTOS where prd_categoria = 'categoria'", null);

            while (cursor.moveToNext()) {
                Produto_SqliteBean prd = new Produto_SqliteBean();

                prd.setPrd_codigo(cursor.getInt(cursor.getColumnIndex(prd.CODIGO_PRODUTO)));
                prd.setPrd_EAN(cursor.getString(cursor.getColumnIndex(prd.CODIGO_BARRAS_EAN_13)));
                prd.setPrd_descricao(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_PRODUTO)));
                prd.setPrd_descr_red(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_REDUZIDA)));
                prd.setPrd_unmed(cursor.getString(cursor.getColumnIndex(prd.UNIDADE_MEDIDA)));
                prd.setPrd_custo(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_CUSTO))));
                prd.setPrd_preco(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_VENDA))));
                prd.setPrd_quant(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.QUANTIDADE_ESTOQUE))));
                prd.setPrd_categoria(cursor.getString(cursor.getColumnIndex(prd.CATEGORIA)));
                listadeprodutos.add(prd);
            }
        } catch (SQLiteException e) {
            Log.d("getAll", e.getMessage());
        } finally {
            db.close();
        }
        return listadeprodutos;
    }


    public boolean GravarProduto_Sqlite(Produto_SqliteBean prod) {

        db = new Db(ctx).getWritableDatabase();
        Gravacao = false;
        Sql = "insert into produtos (prd_codigo,prd_EAN,prd_descricao,prd_descr_red,prd_unmed,prd_custo,prd_preco,prd_quant,prd_categoria) values (?,?,?,?,?,?,?,?,?)";

        try {
            stmt = db.compileStatement(Sql);

            // convertendo de Bigdecimal para double
            String custo = String.valueOf(prod.getPrd_custo());
            BigDecimal custoBig = new BigDecimal(custo);
            double custoProd = custoBig.doubleValue();

            // convertendo de Bigdecimal para double
            String preco = String.valueOf(prod.getPrd_preco());
            BigDecimal precoBig = new BigDecimal(preco);
            double precoProd = precoBig.doubleValue();

            // convertendo de Bigdecimal para double
            String quanEstoque = String.valueOf(prod.getPrd_quant());
            BigDecimal quantBig = new BigDecimal(quanEstoque);
            double quantProd = quantBig.doubleValue();

            stmt.bindLong(1, prod.getPrd_codigo());
            stmt.bindString(2, prod.getPrd_EAN());

            stmt.bindString(3, prod.getPrd_descricao());
            stmt.bindString(4, prod.getPrd_descr_red());

            stmt.bindString(5, prod.getPrd_unmed());
            stmt.bindDouble(6, custoProd);

            stmt.bindDouble(7, precoProd);
            stmt.bindDouble(8, quantProd);
            stmt.bindString(9, prod.getPrd_categoria());

            if (stmt.executeInsert() > 0) {
                Gravacao = true;
                Sql = "";
            }

            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("GravarProduto_Sqlite", e.getMessage());
            Gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }

        return Gravacao;

    }

    public Produto_SqliteBean BuscarProdutoPelo_ID(Produto_SqliteBean produto) {
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        Produto_SqliteBean prd = null;
        try {

            cursor = db.rawQuery("select * from produtos where prd_codigo = ?", new String[]{produto.getPrd_codigo().toString()});
            if (cursor.moveToFirst()) {

                prd = new Produto_SqliteBean();
                prd.setPrd_codigo(cursor.getInt(cursor.getColumnIndex(produto.CODIGO_PRODUTO)));
                prd.setPrd_EAN(cursor.getString(cursor.getColumnIndex(produto.CODIGO_BARRAS_EAN_13)));
                prd.setPrd_descricao(cursor.getString(cursor.getColumnIndex(produto.DESCRICAO_PRODUTO)));
                prd.setPrd_descr_red(cursor.getString(cursor.getColumnIndex(produto.DESCRICAO_REDUZIDA)));
                prd.setPrd_unmed(cursor.getString(cursor.getColumnIndex(produto.UNIDADE_MEDIDA)));
                prd.setPrd_custo(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_CUSTO))));
                prd.setPrd_preco(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_VENDA))));
                prd.setPrd_quant(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.QUANTIDADE_ESTOQUE))));
                prd.setPrd_categoria(cursor.getString(cursor.getColumnIndex(prd.CATEGORIA)));

            }
        } catch (SQLiteException e) {
            Log.d("BuscarProdutoPelo_ID", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return prd;
    }


    public void AtualizaEstoqueProduto(Produto_SqliteBean produto) {

        try {

            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            String sql = "update produtos set prd_quant = ? where prd_codigo = ? ";
            SQLiteStatement stmt = db.compileStatement(sql);

            stmt.bindDouble(1, produto.getPrd_quant().doubleValue());
            stmt.bindLong(2, produto.getPrd_codigo());

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("AtualizaEstoqueProduto", e.getMessage());
        }
    }

    public void AtualizaProduto_Sqlite(Produto_SqliteBean produto) {

        try {

            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            String sql = "update produtos set prd_EAN = ? , prd_descricao = ? , prd_descr_red = ? , prd_unmed = ? ,prd_custo = ?, prd_preco = ? ,prd_quant = ? ,  prd_categoria = ?   where prd_codigo = ? ";
            SQLiteStatement stmt = db.compileStatement(sql);

            // convertendo de Bigdecimal para double
            String custo = String.valueOf(produto.getPrd_custo());
            BigDecimal custoBig = new BigDecimal(custo);
            double custoProd = custoBig.doubleValue();

            // convertendo de Bigdecimal para double
            String preco = String.valueOf(produto.getPrd_preco());
            BigDecimal precoBig = new BigDecimal(preco);
            double precoProd = precoBig.doubleValue();

            // convertendo de Bigdecimal para double
            String quanEstoque = String.valueOf(produto.getPrd_quant());
            BigDecimal quantBig = new BigDecimal(quanEstoque);
            double quantProd = quantBig.doubleValue();

            stmt.bindString(1, produto.getPrd_EAN());
            stmt.bindString(2, produto.getPrd_descricao());
            stmt.bindString(3, produto.getPrd_descr_red());
            stmt.bindString(4, produto.getPrd_unmed());

            stmt.bindDouble(5, custoProd);
            stmt.bindDouble(6, precoProd);
            stmt.bindDouble(7, quantProd);
            stmt.bindString(8, produto.getPrd_categoria());

            stmt.bindLong(9, produto.getPrd_codigo());

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("AtualizaProduto_Sqlite", e.getMessage());
        }
    }


    public void excluir_produtos() {
        try {
            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            String sql = "delete from produtos";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeUpdateDelete();
        } catch (SQLiteException e) {
            Log.d("excluir_produtos", e.getMessage());
        }
    }


    public Produto_SqliteBean BuscaProdutoPorNome(String produto) {

        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        Produto_SqliteBean prd = null;
        try {

            String sql = "select * from produtos where prd_descricao = " + "'" + produto.trim() + "'";

            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                prd = new Produto_SqliteBean();
                prd.setPrd_codigo(cursor.getInt(cursor.getColumnIndex(prd.CODIGO_PRODUTO)));
                prd.setPrd_EAN(cursor.getString(cursor.getColumnIndex(prd.CODIGO_BARRAS_EAN_13)));
                prd.setPrd_descricao(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_PRODUTO)));
                prd.setPrd_descr_red(cursor.getString(cursor.getColumnIndex(prd.DESCRICAO_REDUZIDA)));
                prd.setPrd_unmed(cursor.getString(cursor.getColumnIndex(prd.UNIDADE_MEDIDA)));
                prd.setPrd_custo(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_CUSTO))));
                prd.setPrd_preco(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.PRECO_DE_VENDA))));
                prd.setPrd_quant(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.QUANTIDADE_ESTOQUE))));
                prd.setPrd_categoria(cursor.getString(cursor.getColumnIndex(prd.CATEGORIA)));
            }
        } catch (SQLiteException e) {
            Log.d("BuscaProdutoPorNome", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return prd;

    }

    public Cursor buscaprodutos() {
        Produto_SqliteBean produto = new Produto_SqliteBean();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {

            cursor = db.query(TABLE_NAME, new String[]{produto.CODIGO_PRODUTO + " as  _id", produto.DESCRICAO_PRODUTO, produto.PRECO_DE_VENDA, produto.QUANTIDADE_ESTOQUE, produto.CODIGO_BARRAS_EAN_13}, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLiteException e) {
            Log.d("script", e.getMessage());
        } finally {
            db.close();

        }

        return cursor;
    }

    public Cursor busca_produto_where_field(String inputText, int field) throws SQLiteException {
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        Produto_SqliteBean produto = new Produto_SqliteBean();
        Cursor resultset = null;
        try {

            if (inputText == null || inputText.length() == 0) {
                resultset = db.query(TABLE_NAME, new String[]{produto.CODIGO_PRODUTO + " as  _id", produto.DESCRICAO_PRODUTO, produto.PRECO_DE_VENDA, produto.QUANTIDADE_ESTOQUE, produto.CODIGO_BARRAS_EAN_13}, null, null, null, null, null);

            } else {

                switch (field) {

                    case CODIGOPRODUTO:

                        resultset = db.query(true, TABLE_NAME, new String[]{produto.CODIGO_PRODUTO + " as  _id", produto.DESCRICAO_PRODUTO, produto.PRECO_DE_VENDA, produto.QUANTIDADE_ESTOQUE, produto.CODIGO_BARRAS_EAN_13}, produto.CODIGO_PRODUTO + " like '%" + inputText + "%'", null, null, null, null, null);

                        break;

                    case DESCRICAOPRODUTO:

                        resultset = db.query(true, TABLE_NAME, new String[]{produto.CODIGO_PRODUTO + " as  _id", produto.DESCRICAO_PRODUTO, produto.PRECO_DE_VENDA, produto.QUANTIDADE_ESTOQUE, produto.CODIGO_BARRAS_EAN_13}, produto.DESCRICAO_PRODUTO + " like '%" + inputText + "%'", null, null, null, null, null);

                        break;

                    case CATEGORIA:

                        resultset = db.query(true, TABLE_NAME, new String[]{produto.CODIGO_PRODUTO + " as  _id", produto.DESCRICAO_PRODUTO, produto.PRECO_DE_VENDA, produto.QUANTIDADE_ESTOQUE, produto.CODIGO_BARRAS_EAN_13}, produto.CATEGORIA + " like '%" + inputText + "%'", null, null, null, null, null);

                        break;

                    case CODBARRAS:

                        resultset = db.query(true, TABLE_NAME, new String[]{produto.CODIGO_PRODUTO + " as  _id", produto.DESCRICAO_PRODUTO, produto.PRECO_DE_VENDA, produto.QUANTIDADE_ESTOQUE, produto.CODIGO_BARRAS_EAN_13}, produto.CODIGO_BARRAS_EAN_13 + " like '%" + inputText + "%'", null, null, null, null, null);

                        break;

                }

            }
            if (resultset != null) {
                resultset.moveToFirst();
            }

        } catch (SQLiteException e) {
            Log.d("Script", e.getMessage());
        } finally {

            db.close();

        }

        return resultset;

    }
}
