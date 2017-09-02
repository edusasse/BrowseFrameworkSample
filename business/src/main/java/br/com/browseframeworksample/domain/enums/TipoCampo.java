package br.com.browseframeworksample.domain.enums;

public enum TipoCampo {
	TEX("Texto"), 
	DAT("Data"), 
	DTH("Data e Hora"), 
	INT("Inteiro"), 
	DEC("Decimal"), 
	IMG("Imagem");

	/**
	 * Armazena a descrição
	 */
	private String descricao;

	private TipoCampo(String descricao) {
		setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
