package br.com.browseframeworksample.domain.enums;

public enum Periodicidade {
	SEMANAL("Semanal"),
	QUINZENAL("Quinzenal"),
	MENSAL("Mensal"),
	ANUAL("Anual");
	

	private String descricao;

	private Periodicidade(String descricao) {
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
