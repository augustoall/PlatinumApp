package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;

public class Configuracoes_SqliteBean {

    private Integer USU_CODIGO;
    private String NOME_VENDEDOR;
    private String IMPORTAR_TODOS_CLIENTES;
    private String IP_SERVER;
    private BigDecimal DESCONTO_VENDEDOR;
    private String VENDER_SEM_ESTOQUE;
    private String PEDIR_SENHA_NA_VENDA;
    private String PERMITIR_VENDER_ACIMA_DO_LIMITE;
    private BigDecimal JUROS_VENDA_PRAZO;


    public Configuracoes_SqliteBean() {
    }

    public Integer getUSU_CODIGO() {
        return USU_CODIGO;
    }

    public String getNOME_VENDEDOR() {
        return NOME_VENDEDOR;
    }

    public void setNOME_VENDEDOR(String NOME_VENDEDOR) {
        this.NOME_VENDEDOR = NOME_VENDEDOR;
    }

    public void setUSU_CODIGO(Integer USU_CODIGO) {
        this.USU_CODIGO = USU_CODIGO;
    }

    public String getIMPORTAR_TODOS_CLIENTES() {
        return IMPORTAR_TODOS_CLIENTES;
    }

    public void setIMPORTAR_TODOS_CLIENTES(String IMPORTAR_TODOS_CLIENTES) {
        this.IMPORTAR_TODOS_CLIENTES = IMPORTAR_TODOS_CLIENTES;
    }

    public String getIP_SERVER() {
        return IP_SERVER;
    }

    public void setIP_SERVER(String IP_SERVER) {
        this.IP_SERVER = IP_SERVER;
    }

    public BigDecimal getDESCONTO_VENDEDOR() {
        return DESCONTO_VENDEDOR;
    }

    public void setDESCONTO_VENDEDOR(BigDecimal DESCONTO_VENDEDOR) {
        this.DESCONTO_VENDEDOR = DESCONTO_VENDEDOR;
    }

    public String getVENDER_SEM_ESTOQUE() {
        return VENDER_SEM_ESTOQUE;
    }

    public void setVENDER_SEM_ESTOQUE(String VENDER_SEM_ESTOQUE) {
        this.VENDER_SEM_ESTOQUE = VENDER_SEM_ESTOQUE;
    }

    public String getPEDIR_SENHA_NA_VENDA() {
        return PEDIR_SENHA_NA_VENDA;
    }

    public void setPEDIR_SENHA_NA_VENDA(String PEDIR_SENHA_NA_VENDA) {
        this.PEDIR_SENHA_NA_VENDA = PEDIR_SENHA_NA_VENDA;
    }

    public String getPERMITIR_VENDER_ACIMA_DO_LIMITE() {
        return PERMITIR_VENDER_ACIMA_DO_LIMITE;
    }

    public void setPERMITIR_VENDER_ACIMA_DO_LIMITE(String PERMITIR_VENDER_ACIMA_DO_LIMITE) {
        this.PERMITIR_VENDER_ACIMA_DO_LIMITE = PERMITIR_VENDER_ACIMA_DO_LIMITE;
    }

    public BigDecimal getJUROS_VENDA_PRAZO() {
        return JUROS_VENDA_PRAZO;
    }

    public void setJUROS_VENDA_PRAZO(BigDecimal JUROS_VENDA_PRAZO) {
        this.JUROS_VENDA_PRAZO = JUROS_VENDA_PRAZO;
    }
}
