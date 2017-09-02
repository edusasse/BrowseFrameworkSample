package br.com.browseframeworksample.domain.enums;

public enum OrigemAcao {
	N("Nova Ação"),P("Planejada"),C("Clipagem");

	private String descricao;

	private OrigemAcao(String descricao) {
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
