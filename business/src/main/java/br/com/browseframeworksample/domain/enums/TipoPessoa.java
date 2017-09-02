package br.com.browseframeworksample.domain.enums;

public enum TipoPessoa {

	F("Física"), J("Jurídica");

	private String descricao;

	private TipoPessoa(String descricao) {
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
