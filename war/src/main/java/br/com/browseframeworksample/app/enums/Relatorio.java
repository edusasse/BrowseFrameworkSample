package br.com.browseframeworksample.app.enums;

public enum Relatorio {
	REL001_RELATORIO_DE_CLIENTES("/report/REL001.jasper");
	
	private String caminho;
	
	Relatorio(String caminho){
		setCaminho(caminho);
	}
	
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
	public String getCaminho() {
		return caminho;
	}
}
