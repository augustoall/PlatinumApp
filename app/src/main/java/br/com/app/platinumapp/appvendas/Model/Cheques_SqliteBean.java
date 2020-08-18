package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;

public class Cheques_SqliteBean {

    public static String CHEQUE_ID = "chq_codigo";
    public static String CHEQUE_CLI_CODIGO = "chq_cli_codigo";
    public static String CHEQUE_NUMERO = "chq_numerocheque";
    public static String CHEQUE_TELEFONE1 = "chq_telefone1";
    public static String CHEQUE_TELEFONE2 = "chq_telefone2";
    public static String CHEQUE_CPF_DONO = "chq_cpf_dono";
    public static String CHEQUE_DONO = "chq_nomedono";
    public static String CHEQUE_NOME_BANCO = "chq_nomebanco";
    public static String CHEQUE_VECIMENTO = "chq_vencimento";
    public static String CHEQUE_VALOR = "chq_valorcheque";
    public static String CHEQUE_TERCEIRO = "chq_terceiro";
    public static String CHEQUE_CHAVEVENDA = "vendac_chave";
    public static String CHEQUE_ENVIADO = "chq_enviado";
    public static String CHEQUE_DATA_CADASTRO = "chq_dataCadastro";

    private Integer chq_codigo;
    private Integer chq_cli_codigo;
    private String chq_numerocheque;
    private String chq_telefone1;
    private String chq_telefone2;
    private String chq_cpf_dono;
    private String chq_nomedono;
    private String chq_nomebanco;
    private String chq_vencimento;
    private BigDecimal chq_valorcheque;
    private String chq_terceiro;
    private String vendac_chave;
    private String chq_enviado;
    private String chq_dataCadastro;

    public Cheques_SqliteBean() {
    }

    public Cheques_SqliteBean(Integer chq_codigo, String chq_numerocheque, String vendac_chave) {
        this.chq_codigo = chq_codigo;
        this.chq_numerocheque = chq_numerocheque;
        this.vendac_chave = vendac_chave;
    }

    public Integer getChq_codigo() {
        return chq_codigo;
    }

    public void setChq_codigo(Integer chq_codigo) {
        this.chq_codigo = chq_codigo;
    }

    public Integer getChq_cli_codigo() {
        return chq_cli_codigo;
    }

    public void setChq_cli_codigo(Integer chq_cli_codigo) {
        this.chq_cli_codigo = chq_cli_codigo;
    }

    public String getChq_numerocheque() {
        return chq_numerocheque;
    }

    public void setChq_numerocheque(String chq_numerocheque) {
        this.chq_numerocheque = chq_numerocheque;
    }

    public String getChq_telefone1() {
        return chq_telefone1;
    }

    public void setChq_telefone1(String chq_telefone1) {
        this.chq_telefone1 = chq_telefone1;
    }

    public String getChq_telefone2() {
        return chq_telefone2;
    }

    public void setChq_telefone2(String chq_telefone2) {
        this.chq_telefone2 = chq_telefone2;
    }

    public String getChq_cpf_dono() {
        return chq_cpf_dono;
    }

    public void setChq_cpf_dono(String chq_cpf_dono) {
        this.chq_cpf_dono = chq_cpf_dono;
    }

    public String getChq_nomedono() {
        return chq_nomedono;
    }

    public void setChq_nomedono(String chq_nomedono) {
        this.chq_nomedono = chq_nomedono;
    }

    public String getChq_nomebanco() {
        return chq_nomebanco;
    }

    public void setChq_nomebanco(String chq_nomebanco) {
        this.chq_nomebanco = chq_nomebanco;
    }

    public String getChq_vencimento() {
        return chq_vencimento;
    }

    public void setChq_vencimento(String chq_vencimento) {
        this.chq_vencimento = chq_vencimento;
    }

    public BigDecimal getChq_valorcheque() {
        return chq_valorcheque;
    }

    public void setChq_valorcheque(BigDecimal chq_valorcheque) {
        this.chq_valorcheque = chq_valorcheque;
    }

    public String getChq_terceiro() {
        return chq_terceiro;
    }

    public void setChq_terceiro(String chq_terceiro) {
        this.chq_terceiro = chq_terceiro;
    }

    public String getVendac_chave() {
        return vendac_chave;
    }

    public void setVendac_chave(String vendac_chave) {
        this.vendac_chave = vendac_chave;
    }

    public String getChq_enviado() {
        return chq_enviado;
    }

    public void setChq_enviado(String chq_enviado) {
        this.chq_enviado = chq_enviado;
    }

    public String getChq_dataCadastro() {
        return chq_dataCadastro;
    }

    public void setChq_dataCadastro(String chq_dataCadastro) {
        this.chq_dataCadastro = chq_dataCadastro;
    }
}
