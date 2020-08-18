package br.com.app.platinumapp.appvendas.Model;

import java.math.BigDecimal;


public class Cliente_SqliteBean {

	public static final String CODIGO_DO_CLIENTE = "cli_codigo";
	public static final String _ID_TO_SIMPLECURSORADAPTER = "_id";
	public static final String NOME_DO_CLIENTE = "cli_nome";
	public static final String NOME_FANTASIA = "cli_fantasia";
	public static final String ENDERECO_CLIENTE = "cli_endereco";
	public static final String BAIRRO_CLIENTE = "cli_bairro";
	public static final String CEP_CIDADE = "cli_cep";
	public static final String CODIGO_CIDADE = "cid_codigo";
	public static final String CONTATO_CLIENTE1 = "cli_contato1";
	public static final String CONTATO_CLIENTE2 = "cli_contato2";
	public static final String CONTATO_CLIENTE3 = "cli_contato3";
	public static final String DATA_NASCIMETO = "cli_nascimento";
	public static final String CPF_CNPJ_CLIENTE = "cli_cpfcnpj";
	public static final String RG_INSCRICAO_ESTADUAL = "cli_rginscricaoest";
	public static final String LIMITE_CLIENTE = "cli_limite";
	public static final String EMAIL_CLIENTE = "cli_email";
	public static final String OBSERVACAO = "cli_observacao";
	public static final String CODIGO_VENDEDOR_USUARIO = "usu_codigo";
	public static final String CLIENTE_ENVIADO = "cli_enviado";
	public static final String SENHA_DO_CLIENTE = "cli_senha";
	public static final String CHAVE_CLIENTE = "cli_chave";


	private Integer cli_codigo;
	private String cli_nome;
	private String cli_fantasia;
	private String cli_endereco;
	private String cli_bairro;
	private String cli_cep;
	private Integer cid_codigo;
	private String cli_contato1;
	private String cli_contato2;
	private String cli_contato3;
	private String cli_nascimento;
	private String cli_cpfcnpj;
	private String cli_rginscricaoest;
	private BigDecimal cli_limite;
	private String cli_email;
	private String cli_observacao;
	private Integer usu_codigo;
	private String cli_enviado;
	private String cli_senha;
	private String cli_chave;

	public Cliente_SqliteBean() {
	}

	public Integer getCli_codigo() {
		return cli_codigo;
	}

	public void setCli_codigo(Integer cli_codigo) {
		this.cli_codigo = cli_codigo;
	}

	public String getCli_nome() {
		return cli_nome;
	}

	public void setCli_nome(String cli_nome) {
		this.cli_nome = cli_nome;
	}

	public String getCli_fantasia() {
		return cli_fantasia;
	}

	public void setCli_fantasia(String cli_fantasia) {
		this.cli_fantasia = cli_fantasia;
	}

	public String getCli_endereco() {
		return cli_endereco;
	}

	public void setCli_endereco(String cli_endereco) {
		this.cli_endereco = cli_endereco;
	}

	public String getCli_bairro() {
		return cli_bairro;
	}

	public void setCli_bairro(String cli_bairro) {
		this.cli_bairro = cli_bairro;
	}

	public String getCli_cep() {
		return cli_cep;
	}

	public void setCli_cep(String cli_cep) {
		this.cli_cep = cli_cep;
	}

	public Integer getCid_codigo() {
		return cid_codigo;
	}

	public void setCid_codigo(Integer cid_codigo) {
		this.cid_codigo = cid_codigo;
	}

	public String getCli_contato1() {
		return cli_contato1;
	}

	public void setCli_contato1(String cli_contato1) {
		this.cli_contato1 = cli_contato1;
	}

	public String getCli_contato2() {
		return cli_contato2;
	}

	public void setCli_contato2(String cli_contato2) {
		this.cli_contato2 = cli_contato2;
	}

	public String getCli_contato3() {
		return cli_contato3;
	}

	public void setCli_contato3(String cli_contato3) {
		this.cli_contato3 = cli_contato3;
	}


	public String getCli_nascimento() {
		return cli_nascimento;
	}

	public void setCli_nascimento(String cli_nascimento) {
		this.cli_nascimento = cli_nascimento;
	}

	public String getCli_cpfcnpj() {
		return cli_cpfcnpj;
	}

	public void setCli_cpfcnpj(String cli_cpfcnpj) {
		this.cli_cpfcnpj = cli_cpfcnpj;
	}

	public String getCli_rginscricaoest() {
		return cli_rginscricaoest;
	}

	public void setCli_rginscricaoest(String cli_rginscricaoest) {
		this.cli_rginscricaoest = cli_rginscricaoest;
	}

	public BigDecimal getCli_limite() {
		return cli_limite;
	}

	public void setCli_limite(BigDecimal cli_limite) {
		this.cli_limite = cli_limite;
	}

	public String getCli_email() {
		return cli_email;
	}

	public void setCli_email(String cli_email) {
		this.cli_email = cli_email;
	}

	public String getCli_observacao() {
		return cli_observacao;
	}

	public void setCli_observacao(String cli_observacao) {
		this.cli_observacao = cli_observacao;
	}

	public Integer getUsu_codigo() {
		return usu_codigo;
	}

	public void setUsu_codigo(Integer usu_codigo) {
		this.usu_codigo = usu_codigo;
	}

	public String getCli_enviado() {
		return cli_enviado;
	}

	public void setCli_enviado(String cli_enviado) {
		this.cli_enviado = cli_enviado;
	}

	public String getCli_senha() {
		return cli_senha;
	}

	public void setCli_senha(String cli_senha) {
		this.cli_senha = cli_senha;
	}

	public String getCli_chave() {
		return cli_chave;
	}

	public void setCli_chave(String cli_chave) {
		this.cli_chave = cli_chave;
	}

}
