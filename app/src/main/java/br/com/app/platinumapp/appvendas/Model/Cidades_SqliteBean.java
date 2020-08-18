package br.com.app.platinumapp.appvendas.Model;

public class Cidades_SqliteBean {

    public static String CODIGO_CIDADE = "cid_codigo";
    public static String NOME_CIDADE = "cid_nome";
    public static String ESTADO = "cid_uf";

    private Integer cid_codigo;
    private String cid_nome;
    private String cid_uf;


    public Cidades_SqliteBean(Integer cid_codigo, String cid_nome, String cid_uf) {
        this.cid_codigo = cid_codigo;
        this.cid_nome = cid_nome;
        this.cid_uf = cid_uf;
    }

    public Cidades_SqliteBean() {
    }

    public Integer getCid_codigo() {
        return cid_codigo;
    }

    public void setCid_codigo(Integer cid_codigo) {
        this.cid_codigo = cid_codigo;
    }

    public String getCid_nome() {
        return cid_nome;
    }

    public void setCid_nome(String cid_nome) {
        this.cid_nome = cid_nome;
    }

    public String getCid_uf() {
        return cid_uf;
    }

    public void setCid_uf(String cid_uf) {
        this.cid_uf = cid_uf;
    }

    // aqui acontece a magica
    public String toString() {
        return (this.getCid_nome().toUpperCase() + " - " + this.getCid_uf().toUpperCase());
    }

}
