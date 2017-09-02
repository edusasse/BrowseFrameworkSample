package br.com.browseframeworksample.domain.enums;

public enum NomeParametro {
	PERFIL_ADMINISTRADOR("PERFIL - ADMINISTRADOR"),
	PERFIL_GERENCIAL("PERFIL - GERENCIAL"),
	PERFIL_CLIPPING("PERFIL - CLIPPING"),
	PERFIL_USUARIO("PERFIL - USUARIO"),
	EXIBIR_META_POR_QUANTIDADE("EXIBIR META POR QUANTIDADE"),
	EXIBIR_META_POR_VALOR("EXIBIR META POR VALOR"),
	EMAIL_EXCEPTION("EMAIL EXCEPTION"),
	EMAIL_EXCEPTION_ASSUNTO("EMAIL EXCEPTION ASSUNTO"),
	CARREGA_MES_ATUAL_LISTA_DE_CLIENTES("CARREGA MES ATUAL LISTA DE CLIENTES"),
	RODAPE_PRINCIPAL_WEB("RODAPE PRINCIPAL WEB"),
	AMBIENTE("AMBIENTE"),
	TAMANHO_LIMITE_MB_RELATORIO_CLIPPING("TAMANHO LIMITE(MB) RELATORIO CLIPPING");

	private String descricao;

	private NomeParametro(String descricao) {
		setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
}
