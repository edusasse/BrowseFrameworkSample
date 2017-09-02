package br.com.browseframeworksample.app.bean;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DualListModel;
import org.springframework.util.DigestUtils;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.bean.UtilBean;
import br.com.browseframeworksample.domain.Perfil;
import br.com.browseframeworksample.domain.PessoaFisica;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.facade.UsuarioFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "usuarioAppBean")
@ViewScoped
public class UsuarioAppBean extends GenericAppBean<Usuario> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "usuarioFacade", value = "#{usuarioFacade}")
	private UsuarioFacade usuarioFacade;
    //
    @ManagedProperty(value="#{perfilAppBean}")
	private PerfilAppBean perfilAppBean;
    
    // Acesso ao Facade do classificacao
 	@ManagedProperty(value = "#{utilBean}")
 	private UtilBean utilBean;
    
    private String senhaAux;
    
    // Dual List Model para a Perfis
    private DualListModel<Perfil> dualListModelPerfil = null;
    
    public UsuarioAppBean() {
		super("usuario", DiretoriosApp.SECURED_APPS,  Usuario.class);
	}
    
    @Override
    public boolean isAccessGranted() {
    	return getUtilBean().areAnyGranted("ROLE_ADMIN,ROLE_MANAGER,ROLE_USER,ROLE_CLIPPING");
    }
    
    @Override
    public boolean isMenuItemVisivel(AplicacaoBotaoId abId, AplicacaoType aplicacaoType) {
    	boolean retorno = false;
    	final boolean acessoAdminManager = getUtilBean().areAnyGranted("ROLE_ADMIN,ROLE_MANAGER");
//    	final boolean acessoUser = getUtilBean().areAnyGranted("ROLE_USER");
//    	final boolean acessoClipping = getUtilBean().areAnyGranted("ROLE_CLIPPING");
    	
    	if (AplicacaoBotaoId.NOVO.equals(abId) && acessoAdminManager){
    		retorno = true;
    	}
    	
    	if (AplicacaoBotaoId.EXCLUIR.equals(abId) && acessoAdminManager){
    		retorno = true;
    	}
    	
    	if (AplicacaoBotaoId.SALVAR.equals(abId)){
    		retorno = true;
    	}
    	
    	if (AplicacaoBotaoId.FECHAR.equals(abId)){
    		retorno = true;
    	}
    	
    	return retorno;
    }
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getUsuarioFacade();
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#doNovo()
     */
    @RolesAllowed({"ROLE_ADMIN"})
    @Override
    public void doNovo() {
		// Limpa o campo
		doLimparDualListModels();
    	super.doNovo();
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#doCarregar(br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    @Override
    public void doCarregar(AplicacaoType aplicacaoType){
    	super.doCarregar(aplicacaoType);
    	if (AplicacaoType.CRUD.equals(aplicacaoType)){
    		// Somente se for um objeto novo
    		if (getDto() == null || (getDto() != null && getDto().isNew())){
        		// Parametro passado é a pessoa do endereço
        		PessoaFisica pessoaFisica = (PessoaFisica) getParametro();
	    		if (pessoaFisica == null){
	    			throw new GenericBusinessException(FacesUtil.getResourceBundleString("msgs", "app.usuario.passagemParametro"));
	    		}
	    		// Procura se ja existe um endereco cadastrado para essa pessoa
	    		final Usuario usuario = getUsuarioFacade().findByPessoaFisica(pessoaFisica);
	    		if (usuario != null){
	    			setBaseDTOEmEdicao(usuario);
	    		} else {
	    			// Cria um novo 
	    			doNovo();
	    			// Carrega a pessoa no objeto
	    			getDto().setPessoaFisica(pessoaFisica);
	    		}
    		}
    	}
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DualListModel<Perfil> getDualListModelPerfil() {
		if (this.dualListModelPerfil == null){
			setDualListModelPerfil(new DualListModel(getPerfilAppBean().getPerfilFacade().findAll(null).getData(), new ArrayList<Perfil>())); 
		}
		return this.dualListModelPerfil;
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
			final Usuario cli = (Usuario) dto;

			// Remove dos peris todas que já foram adicionadas a Perfis
			getDualListModelPerfil().getSource().removeAll(cli.getPerfis());
			// Adiciona todos retornados da base a Perfis
			getDualListModelPerfil().getTarget().addAll(cli.getPerfis());
		}
	}
    
	protected void doLimparDualListModels() {
		setDualListModelPerfil(null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.GenericAppBean#doSalvar()
	 */
    @Override
    public void doSalvar() {
    	// Se for um usuario novo, e a senha não estiver 
    	if ((getDto() != null && getDto().isNew()) && (getSenhaAux() == null || (getSenhaAux() != null && getSenhaAux().trim().length() == 0))) {
    		FacesUtil.errorMessage(FacesUtil.getResourceBundleString("msgs", "app.usuario.senha.mensagemSenhasNaoInformada"));
    	} else {
    		final Usuario dto = (Usuario) getBaseDTO();
    		// MD5
    		if (getSenhaAux() != null && getSenhaAux().trim().length() > 0) {
    			dto.setSenha(DigestUtils.md5DigestAsHex(getSenhaAux().getBytes()));
    		}
	    	// Caso não tenha equipe seta os valores do target
	    	if (dto.getPerfis() == null){
	    		dto.setPerfis(getDualListModelPerfil().getTarget());
	    	} else {
	    		// Limpa a equipe oriunda do banco e adiciona os valores do target
	    		dto.getPerfis().clear();
	    		dto.getPerfis().addAll(getDualListModelPerfil().getTarget());
	    	}
	
	    	super.doSalvar();
    	}
    }
    
    // GETTERS && SETTERS 
     
	public UsuarioFacade getUsuarioFacade() {
		return usuarioFacade;
	}

	public void setUsuarioFacade(
			UsuarioFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}

	public PerfilAppBean getPerfilAppBean() {
		return perfilAppBean;
	}

	public void setPerfilAppBean(PerfilAppBean perfilAppBean) {
		this.perfilAppBean = perfilAppBean;
	}

	public void setDualListModelPerfil(
			DualListModel<Perfil> dualListModelPerfil) {
		this.dualListModelPerfil = dualListModelPerfil;
	}

	public String getSenhaAux() {
		return senhaAux;
	}

	public void setSenhaAux(String senhaAux) {
		this.senhaAux = senhaAux;
	}

	public UtilBean getUtilBean() {
		return utilBean;
	}

	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}
	
}