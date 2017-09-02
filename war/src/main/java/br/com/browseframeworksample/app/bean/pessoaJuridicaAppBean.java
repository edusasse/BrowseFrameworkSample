package br.com.browseframeworksample.app.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.component.menuitem.MenuItem;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.PessoaJuridica;
import br.com.browseframeworksample.facade.PessoaJuridicaFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "pessoaJuridicaAppBean")
@ViewScoped
public class pessoaJuridicaAppBean extends GenericAppBean<PessoaJuridica> {
	private static final String BOTAO_ID_MI_PESSOA_JURIDICA_TELEFONE = "miPessoaJuridicaTelefone";
	private static final String BOTAO_ID_MI_PESSOA_JURIDICA_ENDERECO = "miPessoaJuridicaEndereco";
	private static final String BOTAO_ID_MI_PESSOA_JURIDICA_CLIENTE = "miPessoaJuridicaCliente";
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "pessoaJuridicaFacade", value = "#{pessoaJuridicaFacade}")
	private PessoaJuridicaFacade pessoaJuridicaFacade;
    
    public pessoaJuridicaAppBean() {
		super("pessoaJuridica", DiretoriosApp.SECURED_APPS,  PessoaJuridica.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getPessoaJuridicaFacade();
	}
    
    @Override
    public List<MenuItem> getMenuItems(AplicacaoType aplicacaoType) {
    	List<MenuItem> retorno = super.getMenuItems(aplicacaoType);
    	if (retorno == null){
    		retorno = new ArrayList<MenuItem>();
    	}
    	
    	if (AplicacaoType.CRUD.equals(aplicacaoType)){
    		// Endereço
    		final MenuItem miEndereco = new MenuItem();
    		retorno.add(miEndereco);
    		miEndereco.setUpdate(":all");
    		miEndereco.setStyle("color: green;");    		
    		miEndereco.setId(BOTAO_ID_MI_PESSOA_JURIDICA_ENDERECO);
    		miEndereco.setValue(FacesUtil.getResourceBundleString("msgs", "app.endereco.menuItemLabel"));
    		miEndereco.setIcon("ui-icon-document");
    		miEndereco.setImmediate(true);
    		miEndereco.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doCarregar(enderecoAppBean,'CRUD',pessoaJuridicaAppBean.dto)}"));

    		// Cliente
    		final MenuItem miCliente = new MenuItem();
    		retorno.add(miCliente);
    		miCliente.setUpdate(":all");
    		miCliente.setStyle("color: green;");    		
    		miCliente.setId(BOTAO_ID_MI_PESSOA_JURIDICA_CLIENTE);
    		miCliente.setValue(FacesUtil.getResourceBundleString("msgs", "app.cliente.menuItemLabel"));
    		miCliente.setIcon("ui-icon-document");
    		miCliente.setImmediate(true);
    		miCliente.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doCarregar(clienteAppBean,'CRUD',pessoaJuridicaAppBean.dto)}"));
    		miCliente.setDisabled(getDto().isNew());
    		 
    		// Telefone
    		final MenuItem miTelefone = new MenuItem();
    		retorno.add(miTelefone);
    		miTelefone.setUpdate(":all");
    		miTelefone.setStyle("color: green;");    		
    		miTelefone.setId(BOTAO_ID_MI_PESSOA_JURIDICA_TELEFONE);
    		miTelefone.setValue(FacesUtil.getResourceBundleString("msgs", "app.telefone.menuItemLabel"));
    		miTelefone.setIcon("ui-icon-document");
    		miTelefone.setImmediate(true);
    		miTelefone.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doCarregar(telefoneAppBean,'CRUD',pessoaJuridicaAppBean.dto)}"));
    		miTelefone.setDisabled(getDto().isNew());
    	}
    	 
    	
    	return retorno;
    }
    
    // GETTERS && SETTERS 
     
	public PessoaJuridicaFacade getPessoaJuridicaFacade() {
		return pessoaJuridicaFacade;
	}

	public void setPessoaJuridicaFacade(
			PessoaJuridicaFacade pessoaJuridicaFacade) {
		this.pessoaJuridicaFacade = pessoaJuridicaFacade;
	}
	
}