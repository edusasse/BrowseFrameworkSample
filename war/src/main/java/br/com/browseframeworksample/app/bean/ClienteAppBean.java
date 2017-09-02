package br.com.browseframeworksample.app.bean;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DualListModel;

import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;
import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.Cliente;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.domain.PessoaFisica;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.facade.ClienteFacade;

@ManagedBean(name = "clienteAppBean")
@ViewScoped
public class ClienteAppBean extends GenericAppBean<Cliente> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "clienteFacade", value = "#{clienteFacade}")
	private ClienteFacade clienteFacade;
    // Pessoa
    @ManagedProperty(value="#{pessoaBean}")
    private PessoaBean pessoaBean;
    // Pessoa Fisica
    @ManagedProperty(value="#{pessoaFisicaAppBean}")
	private PessoaFisicaAppBean pessoaFisicaBean;
    // Usuário
    @ManagedProperty(value="#{usuarioAppBean}")
	private UsuarioAppBean usuarioAppBean;
    
    // Dual List Model para a Equipe
    private DualListModel<PessoaFisica> dualListModelPessoaFisica = null;
    // Dual List Model para a Usuario
    private DualListModel<Usuario> dualListModelUsuario = null;
    // Dual List Model para a Usuario 
    private DualListModel<Usuario> dualListModelUsuarioClipping = null;
    
    public ClienteAppBean() {
		super("cliente", DiretoriosApp.SECURED_APPS,  Cliente.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#doNovo()
     */
    @Override
    public void doNovo() {
		// Limpa o campo
		doLimparDualListModels();
    	super.doNovo();
    }

    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#setBaseDTOEmEdicao(br.com.browseframework.base.data.dto.BaseDTO)
     */
	@Override
	public void setBaseDTOEmEdicao(@SuppressWarnings("rawtypes") BaseDTO dto) {
		super.setBaseDTOEmEdicao(dto);
		doLimparDualListModels();
		
		if (dto != null && !dto.isNew()){
			final Cliente cli = (Cliente) dto;

			// Remove das pessoas todas que já foram adicionadas a Equipe
			getDualListModelPessoaFisica().getSource().removeAll(cli.getEquipe());
			// Adiciona todos retornados da base a Equipe
			getDualListModelPessoaFisica().getTarget().addAll(cli.getEquipe());
						
			// Remove das pessoas todas que já foram adicionadas a Equipe
			getDualListModelUsuario().getSource().removeAll(cli.getUsuarios());
			// Adiciona todos retornados da base a Equipe
			getDualListModelUsuario().getTarget().addAll(cli.getUsuarios());
			
			// Remove das pessoas todas que já foram adicionadas a Equipe
			getDualListModelUsuarioClipping().getSource().removeAll(cli.getUsuariosClipping());
			// Adiciona todos retornados da base a Equipe
			getDualListModelUsuarioClipping().getTarget().addAll(cli.getUsuariosClipping());
		}
	}
    
	protected void doLimparDualListModels() {
		setDualListModelPessoaFisica(null);
		setDualListModelUsuario(null);
		setDualListModelUsuarioClipping(null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.GenericAppBean#doSalvar()
	 */
    @Override
    public void doSalvar() {
    	final Cliente dto = (Cliente) getBaseDTO();
    	// Caso não tenha equipe seta os valores do target
    	if (dto.getEquipe() == null){
    		dto.setEquipe(getDualListModelPessoaFisica().getTarget());
    	} else {
    		// Limpa a equipe oriunda do banco e adiciona os valores do target
    		dto.getEquipe().clear();
    		dto.getEquipe().addAll(getDualListModelPessoaFisica().getTarget());
    	}

    	// Caso não tenha usuarios seta os valores do target
    	if (dto.getUsuarios() == null){
    		dto.setUsuarios(getDualListModelUsuario().getTarget());
    	} else {
    		// Limpa a equipe oriunda do banco e adiciona os valores do target
    		dto.getUsuarios().clear();
    		dto.getUsuarios().addAll(getDualListModelUsuario().getTarget());
    	}
    	
    	// Caso não tenha usuarios seta os valores do target
    	if (dto.getUsuariosClipping() == null){
    		dto.setUsuariosClipping(getDualListModelUsuarioClipping().getTarget());
    	} else {
    		// Limpa a equipe oriunda do banco e adiciona os valores do target
    		dto.getUsuariosClipping().clear();
    		dto.getUsuariosClipping().addAll(getDualListModelUsuarioClipping().getTarget());
    	}
    	
    	super.doSalvar();
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#doCarregar(br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    public void doCarregar(AplicacaoType aplicacaoType){
    	super.doCarregar(aplicacaoType);
    	if (AplicacaoType.CRUD.equals(aplicacaoType)){
    		// Parametro passado é a pessoa do endereço
    		final Pessoa pessoa = (Pessoa) getParametro();
    		if (pessoa == null){
    			throw new GenericBusinessException(FacesUtil.getResourceBundleString("msgs", "app.endereco.passagemParametro"));
    		}
    		// Procura se ja existe um endereco cadastrado para essa pessoa
    		final Cliente cliente = getClienteFacade().findByPessoa(pessoa);
    		if (cliente != null){
    			setBaseDTOEmEdicao(cliente);
    		} else {
    			// Cria um novo
    			doNovo();
    			// Carrega a pessoa no objeto
    			getDto().setPessoa(pessoa);
    		}
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getClienteFacade();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DualListModel<PessoaFisica> getDualListModelPessoaFisica() {
		if (this.dualListModelPessoaFisica == null){
			setDualListModelPessoaFisica(new DualListModel(getPessoaFisicaBean().getPessoaFisicaFacade().findAll(null).getData(), new ArrayList<PessoaFisica>())); 
		}
		return this.dualListModelPessoaFisica;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DualListModel<Usuario> getDualListModelUsuario() {
		if (this.dualListModelUsuario == null){
			setDualListModelUsuario(new DualListModel(getUsuarioAppBean().getUsuarioFacade().findAll(null).getData(), new ArrayList<Usuario>())); 
		}
		return this.dualListModelUsuario;
	}
	
	@Override
	public String getNomeCampoDescricao() {
		return "pessoa.nome";
	}
	
    // GETTERS && SETTERS 
     
	public ClienteFacade getClienteFacade() {
		return clienteFacade;
	}

	public void setClienteFacade(
			ClienteFacade clienteFacade) {
		this.clienteFacade = clienteFacade;
	}
	
	public void setDualListModelPessoaFisica(DualListModel<PessoaFisica> dualListModelPessoaFisica) {
		this.dualListModelPessoaFisica = dualListModelPessoaFisica;
	}

	public PessoaBean getPessoaBean() {
		return pessoaBean;
	}

	public void setPessoaBean(PessoaBean pessoaBean) {
		this.pessoaBean = pessoaBean;
	}
	
	public PessoaFisicaAppBean getPessoaFisicaBean() {
		return pessoaFisicaBean;
	}

	public void setPessoaFisicaBean(PessoaFisicaAppBean pessoaFisicaBean) {
		this.pessoaFisicaBean = pessoaFisicaBean;
	}

	public UsuarioAppBean getUsuarioAppBean() {
		return usuarioAppBean;
	}

	public void setUsuarioAppBean(UsuarioAppBean usuarioAppBean) {
		this.usuarioAppBean = usuarioAppBean;
	}

	public void setDualListModelUsuario(
			DualListModel<Usuario> dualListModelUsuario) {
		this.dualListModelUsuario = dualListModelUsuario;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DualListModel<Usuario> getDualListModelUsuarioClipping() {
		if (this.dualListModelUsuarioClipping == null){
			setDualListModelUsuarioClipping(new DualListModel(getUsuarioAppBean().getUsuarioFacade().findUsuariosClipping(), new ArrayList<Usuario>())); 
		}
		return dualListModelUsuarioClipping;
	}

	public void setDualListModelUsuarioClipping(
			DualListModel<Usuario> dualListModelUsuarioClipping) {
		this.dualListModelUsuarioClipping = dualListModelUsuarioClipping;
	}
}