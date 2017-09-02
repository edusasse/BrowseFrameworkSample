package br.com.browseframeworksample.app.enums;

public enum AplicacaoBotaoId {
	NOVO("miNovo"),
	SALVAR("miSalvar"),
	EXCLUIR("miExcluir"),
	FECHAR("miFechar");
	
	private String id;
	
	private AplicacaoBotaoId(String id) {
		setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
