package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class Usuarios_SqliteDao {

	private static final String TABLE_NAME = "USUARIOS";

	private Context ctx;
	private String Sql;
	private boolean Gravacao;
	private boolean AchouUsuario;

	public Usuarios_SqliteDao(Context ctx) {
		this.ctx = ctx;
	}

	// 12 FIELDS

	public boolean GravarUsuarios_Sqlite(Usuarios_SqliteBean usuario) {

		SQLiteDatabase db = new Db(ctx).getWritableDatabase();
		Gravacao = false;

		Sql = "insert into usuarios (usu_codigo, usu_nome, usu_telefone, usu_email, usu_desconto, usu_validadedias, usu_comissao, usu_bloqueado, usu_cpf, cid_codigo, usu_permissao, usu_dispositivo) values (?,?,?,?,?,?,?,?,?,?,?,?)";

		SQLiteStatement stmt = db.compileStatement(Sql);
		try {


			stmt.bindLong(1, usuario.getUsu_codigo());
			stmt.bindString(2, usuario.getUsu_nome());
			stmt.bindString(3, usuario.getUsu_telefone());
			stmt.bindString(4, usuario.getUsu_email());

			stmt.bindDouble(5, usuario.getUsu_desconto());
			stmt.bindLong(6, usuario.getUsu_validadedias());
			stmt.bindDouble(7, usuario.getUsu_comissao());

			stmt.bindString(8, usuario.getUsu_bloqueado());
			stmt.bindString(9, usuario.getUsu_cpf());
			stmt.bindLong(10, usuario.getCid_codigo());

			stmt.bindString(11, usuario.getUsu_permissao());
			stmt.bindString(12, usuario.getUsu_dispositivo());

			if (stmt.executeInsert() > 0) {
				Gravacao = true;
				Sql = "";
			}

			stmt.clearBindings();

		} catch (SQLiteException e) {
			Log.d("GravarUsuarios_Sqlite", e.getMessage());
			Gravacao = false;
		} finally {
			db.close();
			stmt.close();
		}

		return Gravacao;
	}

}
