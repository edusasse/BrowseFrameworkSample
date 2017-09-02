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
import br.com.browseframeworksample.bean.UtilBean;
import br.com.browseframeworksample.domain.PessoaFisica;
import br.com.browseframeworksample.facade.PessoaFisicaFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "pessoaFisicaAppBean")
@ViewScoped
public class PessoaFisicaAppBean extends GenericAppBean<PessoaFisica> {
	private static final String BOTAO_ID_MI_PESSOA_FISICA_USUARIO = "miPessoaFisicaUsuario";
    private static final String BOTAO_ID_MI_PESSOA_FISICA_TELEFONE = "miPessoaFisicaTelefone";
	private static final String BOTAO_ID_MI_PESSOA_FISICA_ENDERECO = "miPessoaFisicaEndereco";
	private static final String BOTAO_ID_MI_PESSOA_FISICA_CLIENTE = "miPessoaFisicaCliente";
	// Acesso ao Facade do classificacao
    @ManagedProperty(name = "pessoaFisicaFacade", value = "#{pessoaFisicaFacade}")
	private PessoaFisicaFacade pessoaFisicaFacade;
    // Acesso ao Facade do classificacao
 	@ManagedProperty(value = "#{utilBean}")
 	private UtilBean utilBean;
 	
    public PessoaFisicaAppBean() {
		super("pessoaFisica", DiretoriosApp.SECURED_APPS,  PessoaFisica.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getMenuItems(br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    @Override
    public List<MenuItem> getMenuItems(AplicacaoType aplicacaoType) {
    	List<MenuItem> retorno = super.getMenuItems(aplicacaoType);
    	if (retorno == null){
    		retorno = new ArrayList<MenuItem>();
    	}
    	
    	if (AplicacaoType.CRUD.equals(aplicacaoType)){
    		// Usuario
    		final MenuItem miUsuario = new MenuItem();
    		retorno.add(miUsuario);
    		miUsuario.setUpdate(":all");
    		miUsuario.setStyle("color: green;");    		
    		miUsuario.setId(BOTAO_ID_MI_PESSOA_FISICA_USUARIO);
    		miUsuario.setValue(FacesUtil.getResourceBundleString("msgs", "app.usuario.menuItemLabel"));
    		miUsuario.setIcon("ui-icon-document");
    		miUsuario.setImmediate(true);
    		miUsuario.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doCarregar(usuarioAppBean,'CRUD',pessoaFisicaAppBean.dto)}"));
    		miUsuario.setDisabled(getDto().isNew());
    		miUsuario.setRendered(getUtilBean().areAnyGranted("ROLE_ADMIN"));
    		// Cliente
    		final MenuItem miCliente = new MenuItem();
    		retorno.add(miCliente);
    		miCliente.setUpdate(":all");
    		miCliente.setStyle("color: green;");    		
    		miCliente.setId(BOTAO_ID_MI_PESSOA_FISICA_CLIENTE);
    		miCliente.setValue(FacesUtil.getResourceBundleString("msgs", "app.cliente.menuItemLabel"));
    		miCliente.setIcon("ui-icon-document");
    		miCliente.setImmediate(true);
    		miCliente.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doCarregar(clienteAppBean,'CRUD',pessoaFisicaAppBean.dto)}"));
    		miCliente.setDisabled(getDto().isNew());
    		miCliente.setRendered(getUtilBean().areAnyGranted("ROLE_ADMIN, ROLE_MANAGER"));
    		// Endereço
    		final MenuItem miEndereco = new MenuItem();
    		retorno.add(miEndereco);
    		miEndereco.setUpdate(":all");
    		miEndereco.setStyle("color: green;");    		
    		miEndereco.setId(BOTAO_ID_MI_PESSOA_FISICA_ENDERECO);
    		miEndereco.setValue(FacesUtil.getResourceBundleString("msgs", "app.endereco.menuItemLabel"));
    		miEndereco.setIcon("ui-icon-document");
    		miEndereco.setImmediate(true);
    		miEndereco.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doCarregar(enderecoAppBean,'CRUD',pessoaFisicaAppBean.dto)}"));
    		miEndereco.setDisabled(getDto().isNew());
    		// Telefone
    		final MenuItem miTelefone = new MenuItem();
    		retorno.add(miTelefone);
    		miTelefone.setUpdate(":all");
    		miTelefone.setStyle("color: green;");    		
    		miTelefone.setId(BOTAO_ID_MI_PESSOA_FISICA_TELEFONE);
    		miTelefone.setValue(FacesUtil.getResourceBundleString("msgs", "app.telefone.menuItemLabel"));
    		miTelefone.setIcon("ui-icon-document");
    		miTelefone.setImmediate(true);
    		miTelefone.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doCarregar(telefoneAppBean,'CRUD',pessoaFisicaAppBean.dto)}"));
    		miTelefone.setDisabled(getDto().isNew());
    	}
    	
    	return retorno;
    }
    
    /**
     * Não realiza o fechamento da aplicação para dar possibilidade ao usuário cadastrar o endereço.
     */
    @Override
    public boolean isFecharAoSalvar() {
    	return false; // pois a tela permite acessar outros cadastros ligados a pessoa.
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getPessoaFisicaFacade();
	}
    
    // GETTERS && SETTERS 
     
	public PessoaFisicaFacade getPessoaFisicaFacade() {
		return pessoaFisicaFacade;
	}

	public void setPessoaFisicaFacade(
			PessoaFisicaFacade pessoaFisicaFacade) {
		this.pessoaFisicaFacade = pessoaFisicaFacade;
	}

	public UtilBean getUtilBean() {
		return utilBean;
	}

	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}
	
}