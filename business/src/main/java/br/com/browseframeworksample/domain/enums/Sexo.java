package br.com.browseframeworksample.domain.enums;

public enum Sexo {
	M("Masculino"), F("Feminino");

	private String descricao;

	private Sexo(String descricao) {
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
