package br.com.browseframeworksample.domain.enums;

public enum SituacaoAcao {
	A("Aberta"),P("Programada"),R("Encerrada"),C("Cancelada");

	private String descricao;

	private SituacaoAcao(String descricao) {
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
