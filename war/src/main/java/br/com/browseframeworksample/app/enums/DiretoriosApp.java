package br.com.browseframeworksample.app.enums;

public enum DiretoriosApp {
	SECURED("/secured/"),
	SECURED_APPS("/secured/apps/");
	
	private String caminho;
	
	DiretoriosApp(String caminho){
		setCaminho(caminho);
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
}
