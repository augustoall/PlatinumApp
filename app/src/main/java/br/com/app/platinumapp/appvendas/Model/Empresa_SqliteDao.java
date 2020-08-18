package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class Empresa_SqliteDao {

	private static final String TABLE_NAME = "CONFIGURACOES";

	private Context ctx;
	private String Sql;
	private boolean Gravacao;
	private boolean AchouConfig;

	public Empresa_SqliteDao(Context ctx) {
		this.ctx = ctx;
	}

	public boolean GravaParamsEmpresa(Empresa_SqliteBean empresa) {

		SQLiteDatabase db = new Db(ctx).getWritableDatabase();
		Gravacao = false;

		Sql = "insert into empresa (emp_nome,emp_cpf,emp_celularkey,usu_codigo,emp_totalemdias,emp_numerocelular,emp_email) values (?,?,?,?,?,?,?)";
		SQLiteStatement stmt = db.compileStatement(Sql);
		try {

			stmt.bindString(1, empresa.getEmp_nome());
			stmt.bindString(2, empresa.getEmp_cpf());
			stmt.bindString(3, empresa.getEmp_celularkey());
			stmt.bindString(4, empresa.getUsu_codigo());
			stmt.bindString(5, empresa.getEmp_totalemdias());
			stmt.bindString(6, empresa.getEmp_numerocelular());
			stmt.bindString(7, empresa.getEmp_email());

			if (stmt.executeInsert() > 0) {
				Gravacao = true;
				Sql = "";
			}

			stmt.clearBindings();

		} catch (SQLiteException e) {
			Log.d("Script", "GravaEmpresa: " + e.getMessage());
			Gravacao = false;
		} finally {
			db.close();
			stmt.close();
		}

		return Gravacao;
	}

	public Empresa_SqliteBean buscarEmpresa() {
		SQLiteDatabase db = new Db(ctx).getReadableDatabase();
		Empresa_SqliteBean emp = null;
		try {

			String sql = "select * from empresa";
			Cursor rs = db.rawQuery(sql, null);
			if (rs.moveToFirst()) {
				emp = new Empresa_SqliteBean();

				emp.setEmp_nome(rs.getString(rs.getColumnIndex("emp_nome")));
				emp.setEmp_cpf(rs.getString(rs.getColumnIndex("emp_cpf")));
				emp.setEmp_celularkey(rs.getString(rs.getColumnIndex("emp_celularkey")));
				emp.setUsu_codigo(rs.getString(rs.getColumnIndex("usu_codigo")));
				emp.setEmp_totalemdias(rs.getString(rs.getColumnIndex("emp_totalemdias")));
				emp.setEmp_numerocelular(rs.getString(rs.getColumnIndex("emp_numerocelular")));
				emp.setEmp_email(rs.getString(rs.getColumnIndex("emp_email")));

			}
		} catch (SQLiteException e) {
			Log.d("Script", "buscarEmpresa: " + e.getMessage());
		} finally {
			db.close();
		}
		return emp;
	}

	public void EditarEmpresa(Empresa_SqliteBean empresa) {
		SQLiteDatabase db = new Db(ctx).getWritableDatabase();
		String sql = "UPDATE EMPRESA SET emp_totalemdias =? ";
		SQLiteStatement stmt = db.compileStatement(sql);
		try {
			stmt.bindString(1, empresa.getEmp_totalemdias());
			stmt.executeUpdateDelete();
			stmt.clearBindings();
		} catch (SQLiteException e) {
			Log.d("Script", e.getMessage());
		} finally {
			db.close();
			stmt.close();
		}
	}

}
