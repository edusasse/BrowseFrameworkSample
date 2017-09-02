package br.com.browseframeworksample.domain.enums;

public enum SituacaoCliente {
	A("Ativo"), I("Inativo");

	private String descricao;

	private SituacaoCliente(String descricao) {
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
