package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {
/*
    public static String DBname = "cmdv.db";

*/
    public static String DBname = "u232035487_cmdv_db";
    public static int version = 2;

    public Db(Context ctx) {
        super(ctx, DBname, null, version);

    }

    private static String SQL_CONFIG = "CREATE TABLE [CONFIGURACOES] ( USU_CODIGO INTEGER , NOME_VENDEDOR  VARCHAR DEFAULT 30 ,IMPORTAR_TODOS_CLIENTES  VARCHAR DEFAULT 10, IP_SERVER VARCHAR DEFAULT 100,DESCONTO_VENDEDOR INTEGER,VENDER_SEM_ESTOQUE  CHAR DEFAULT 1,PEDIR_SENHA_NA_VENDA  CHAR DEFAULT 1,PERMITIR_VENDER_ACIMA_DO_LIMITE CHAR DEFAULT 1,JUROS_VENDA_PRAZO DECIMAL(10,2) )";
    private static String SQL_CLIENTE = "CREATE TABLE [CLIENTES] (cli_codigo INTEGER ,cli_nome VARCHAR DEFAULT 50,cli_fantasia VARCHAR DEFAULT 50,cli_endereco VARCHAR DEFAULT 50,cli_bairro  VARCHAR DEFAULT 50,cli_cep VARCHAR DEFAULT 50,cid_codigo INTEGER,cli_contato1 VARCHAR DEFAULT 50,cli_contato2 VARCHAR DEFAULT 50,cli_contato3 VARCHAR DEFAULT 50,cli_nascimento DATE,  cli_cpfcnpj  VARCHAR DEFAULT 50,  cli_rginscricaoest VARCHAR DEFAULT 50,  cli_limite DECIMAL(10,2),  cli_email  VARCHAR DEFAULT 50,  cli_observacao  VARCHAR DEFAULT 50 ,  usu_codigo  INTEGER,  cli_enviado VARCHAR DEFAULT 20,  cli_senha   VARCHAR DEFAULT 60 ,cli_chave VARCHAR DEFAULT 100 )";
    private static String SQL_USUARIOS = "CREATE TABLE [USUARIOS] (usu_codigo INTEGER,usu_nome  VARCHAR DEFAULT 50,usu_telefone VARCHAR DEFAULT 50,usu_email VARCHAR DEFAULT 70,usu_desconto DECIMAL(10,2),usu_validadedias INTEGER,usu_comissao DECIMAL(10,2),usu_bloqueado VARCHAR DEFAULT 1,usu_cpf VARCHAR DEFAULT 50,cid_codigo INTEGER,usu_permissao VARCHAR DEFAULT 1,usu_dispositivo VARCHAR DEFAULT 50)";
    private static String SQL_EMPRESA = "CREATE TABLE [EMPRESA] (emp_nome VARCHAR DEFAULT 50,emp_cpf VARCHAR DEFAULT 50,emp_celularkey VARCHAR DEFAULT 50,usu_codigo VARCHAR DEFAULT 50,emp_totalemdias VARCHAR DEFAULT 50,emp_numerocelular VARCHAR DEFAULT 50,emp_email VARCHAR DEFAULT 50)";
    private static String SQL_PRODUTOS = "CREATE TABLE [PRODUTOS] ( prd_codigo INTEGER,  prd_EAN VARCHAR DEFAULT 50,  prd_descricao VARCHAR DEFAULT 50,  prd_descr_red VARCHAR DEFAULT 50,  prd_unmed VARCHAR DEFAULT 10,  prd_custo DECIMAL(10,2),  prd_preco DECIMAL(10,2) ,  prd_quant DECIMAL, prd_categoria VARCHAR DEFAULT 50)";
    private static String SQL_CORES_TAMANHOS = "CREATE TABLE [CORES_TAMANHOS] ( prd_codigo INTEGER,prd_descricao VARCHAR DEFAULT 50, cor_do_produto VARCHAR DEFAULT 20 ,tamanho_do_produto VARCHAR DEFAULT 20, vendac chave da venda  )";
    private static String SQL_ESTOQUE = "CREATE TABLE [ESTOQUE] ( prd_codigo INTEGER, prd_descricao VARCHAR DEFAULT 50, data_ultima_importacao DATETIME , quantidade_importada  INTEGER, quantidade_vendida INTEGER , estoque_real_do_produto INTEGER , estoque_enviado CHAR DEFAULT 1)";
    private static String SQL_VENDA_C = "CREATE TABLE [VENDAC] (vendac_id  INTEGER PRIMARY KEY AUTOINCREMENT , vendac_chave VARCHAR DEFAULT 70 , vendac_datahoravenda DATETIME ,vendac_previsao_entrega DATE ,vendac_usu_codigo INTEGER,vendac_usu_nome  VARCHAR DEFAULT 50, vendac_cli_codigo INTEGER ,vendac_cli_nome VARCHAR DEFAULT 50, vendac_fpgto_codigo INTEGER ,vendac_fpgto_tipo VARCHAR DEFAULT 70, vendac_valor DECIMAL(10,2) , vendac_peso_total DECIMAL(10,2) , vendac_observacao VARCHAR DEFAULT 50  ,vendac_enviada CHAR DEFAULT 1 , vendac_latitude VARCHAR DEFAULT 50 , vendac_longitude VARCHAR DEFAULT 50 )";
    private static String SQL_VENDA_D = "CREATE TABLE [VENDAD] (vendac_chave VARCHAR DEFAULT 70, vendad_nro_item INTEGER ,vendad_ean VARCHAR DEFAULT 50 , vendad_codigo_produto INTEGER,  vendad_descricao_produto VARCHAR DEFAULT 50 ,vendad_quantidade DECIMAL ,vendad_precovenda  DECIMAL(10,2),vendad_total DECIMAL(10,2) )";
    private static String SQL_VENDAD_TEMP = "CREATE TABLE [VENDAD_TEMP] (vendad_ean VARCHAR DEFAULT 50 , vendad_codigo_produto INTEGER,  vendad_descricao_produto VARCHAR DEFAULT 50 ,vendad_quantidade DECIMAL ,vendad_precovenda  DECIMAL(10,2),vendad_total DECIMAL(10,2))";
    private static String SQL_CIDADES = "CREATE TABLE [CIDADES] (cid_codigo INTEGER, cid_nome VARCHAR DEFAULT 50 ,cid_uf CHAR DEFAULT 2)";
    private static String SQL_CHEQUES = "CREATE TABLE [CHEQUE] (chq_codigo  INTEGER PRIMARY KEY AUTOINCREMENT ,chq_cli_codigo INTEGER,chq_numerocheque VARCHAR DEFAULT 20,chq_telefone1 VARCHAR DEFAULT 20,chq_telefone2 VARCHAR DEFAULT 20,chq_cpf_dono VARCHAR DEFAULT 50,chq_nomedono VARCHAR DEFAULT 50  , chq_nomebanco  VARCHAR DEFAULT 50,chq_vencimento DATE,chq_valorcheque DECIMAL(10,2),chq_terceiro CHAR DEFAULT 1,vendac_chave VARCHAR DEFAULT 70 ,chq_enviado CHAR DEFAULT 1,chq_dataCadastro DATE) ";
    private static String SQL_RECEBER = "CREATE TABLE [RECEBER] (rec_codigo INTEGER PRIMARY KEY AUTOINCREMENT ,rec_num_parcela INTEGER,rec_cli_codigo INTEGER,rec_cli_nome VARCHAR DEFAULT 50,vendac_chave VARCHAR DEFAULT 60,rec_datamovimento DATE,rec_valorreceber DECIMAL(10,2),rec_datavencimento DATE,rec_datavencimento_extenso VARCHAR DEFAULT 50  ,rec_data_que_pagou DATE,rec_valor_pago DECIMAL(10,2) ,rec_recebeu_com VARCHAR DEFAULT 50,rec_parcelas_cartao INTEGER,rec_enviado CHAR DEFAULT 1)";
    private static String SQL_CONF_PAGAMENTO = "CREATE TABLE [CONF_PAGAMENTO] (pag_codigo INTEGER PRIMARY KEY AUTOINCREMENT ,pag_sementrada_comentrada char default 1,pag_tipo_pagamento varchar default 30,pag_recebeucom_din_chq_cart varchar default 20,pag_valor_recebido DECIMAL(10,2),pag_parcelas_normal INTEGER,pag_parcelas_cartao INTEGER,vendac_chave VARCHAR DEFAULT 70,pag_enviado CHAR DEFAULT 1) ;";
    private static String SQL_HISTORICO_PAGAMENTO = "CREATE TABLE [HISTORICO_PAGAMENTO] (hist_codigo INTEGER PRIMARY KEY AUTOINCREMENT ,hist_numero_parcela INTEGER ,hist_valor_real_parcela DECIMAL(10,2), hist_valor_pago_no_dia DECIMAL(10,2),hist_restante_a_pagar DECIMAL(10,2),hist_datapagamento DATE ,hist_nomecliente VARCHAR DEFAULT 50, hist_pagou_com VARCHAR DEFAULT 20,vendac_chave VARCHAR DEFAULT 60, hist_enviado CHAR DEFAULT 1 )";
    private static String SQL_FIREBASE = "CREATE TABLE [FIREBASE] (fbs_codigo INTEGER PRIMARY KEY AUTOINCREMENT , fbs_mensagem TEXT, fbs_titulo varchar default 100, fbs_datahora DATETIME,fbs_type char default 3) ;";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CLIENTE);
        db.execSQL(SQL_USUARIOS);
        db.execSQL(SQL_CONFIG);
        db.execSQL(SQL_EMPRESA);
        db.execSQL(SQL_PRODUTOS);
        db.execSQL(SQL_VENDA_C);
        db.execSQL(SQL_VENDA_D);
        db.execSQL(SQL_VENDAD_TEMP);
        db.execSQL(SQL_RECEBER);
        db.execSQL(SQL_CIDADES);
        db.execSQL(SQL_CHEQUES);
        db.execSQL(SQL_CONF_PAGAMENTO);
        db.execSQL(SQL_HISTORICO_PAGAMENTO);
        db.execSQL(SQL_FIREBASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int VersaoAntiga, int VersaoNova) {

    }

}
