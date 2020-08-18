package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Cidades_SqliteDao {

	private static final String TABLE_NAME = "CIDADES";

	private Context ctx;
	private String Sql;
	private SQLiteStatement stmt;
	private SQLiteDatabase db;
	private Cursor cursor;

	public Cidades_SqliteDao(Context ctx) {
		this.ctx = ctx;
	}

	public void grava_cidade(Cidades_SqliteBean cidade) {

		try {

			db = new Db(ctx).getWritableDatabase();

			Sql = "insert into CIDADES (cid_codigo,cid_nome,cid_uf) values (?,?,?)";

			stmt = db.compileStatement(Sql);
			stmt.bindLong(1, cidade.getCid_codigo());
			stmt.bindString(2, cidade.getCid_nome());
			stmt.bindString(3, cidade.getCid_uf());
			stmt.executeInsert();
			stmt.clearBindings();

		} catch (SQLiteException e) {
			Log.d("grava_cidade", e.getMessage());

		} finally {
			db.close();
			stmt.close();
		}

	}


    public Cidades_SqliteBean BuscacidadePorCodigo(Integer cid_codigo) {

        Cidades_SqliteBean cidade =  null;

        db = new Db(ctx).getReadableDatabase();

        try {
            cursor = db.rawQuery("select * from cidades  where cid_codigo = "+ cid_codigo, null);

            if(cursor.moveToFirst()) {
                cidade = new Cidades_SqliteBean();
                cidade.setCid_codigo(cursor.getInt(cursor.getColumnIndex(cidade.CODIGO_CIDADE)));
                cidade.setCid_nome(cursor.getString(cursor.getColumnIndex(cidade.NOME_CIDADE)));
                cidade.setCid_uf(cursor.getString(cursor.getColumnIndex(cidade.ESTADO)));
            }

        } catch (SQLiteException e) {
            Log.d("BuscacidadePorCodigo", e.getMessage());
        }

        return cidade;

    }






	public List<Cidades_SqliteBean> ListaCidades() {
		List<Cidades_SqliteBean> ListaCidades = new ArrayList<>();
		db = new Db(ctx).getReadableDatabase();

		try {
			cursor = db.rawQuery("select * from CIDADES  order by cid_nome asc", null);

			while (cursor.moveToNext()) {

				Cidades_SqliteBean cidade = new Cidades_SqliteBean();				
				cidade.setCid_codigo(cursor.getInt(cursor.getColumnIndex(cidade.CODIGO_CIDADE)));
				cidade.setCid_nome(cursor.getString(cursor.getColumnIndex(cidade.NOME_CIDADE)));
				cidade.setCid_uf(cursor.getString(cursor.getColumnIndex(cidade.ESTADO)));				
				ListaCidades.add(cidade);
			}

		} catch (SQLiteException e) {
			Log.d("ListaCidades", e.getMessage());
		}

		return ListaCidades;

	}
	
	
	public void exclui_cidades() {
		SQLiteDatabase db = new Db(ctx).getWritableDatabase();
		String sql = "delete from CIDADES ";
		SQLiteStatement stmt = db.compileStatement(sql);
		try {
			stmt.executeUpdateDelete();
			stmt.clearBindings();
		} catch (SQLiteException e) {
			Log.d("exclui_cidades", e.getMessage());
		} finally {
			db.close();
			stmt.close();
		}
	}

}
