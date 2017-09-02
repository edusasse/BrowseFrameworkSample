package br.com.browseframeworksample.domain.enums;

public enum SituacaoAgenda {
	P("Programada"),R("Realizada");

	private String descricao;

	private SituacaoAgenda(String descricao) {
		setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
