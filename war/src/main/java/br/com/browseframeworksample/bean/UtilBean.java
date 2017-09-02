package br.com.browseframeworksample.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.browseframeworksample.domain.Parametro;
import br.com.browseframeworksample.domain.Perfil;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.domain.enums.NomeParametro;
import br.com.browseframeworksample.facade.ParametroFacade;
import br.com.browseframeworksample.facade.PerfilFacade;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "utilBean")
@SessionScoped
public class UtilBean {
	
	@ManagedProperty(name="parametroFacade", value ="#{parametroFacade}")
	private ParametroFacade parametroFacade;
	
	@ManagedProperty(name="perfilFacade", value ="#{perfilFacade}")
	private PerfilFacade perfilFacade;

	private String emailException = "";
	private String emailExceptionAssunto = "";
	private String ambiente = null;
	private boolean metaQuantidade = false;
	private boolean metaValor = false;
	private boolean carregaMesAtualListaDeCliente = false;

	/**
	 * Carrega parametros.
	 */
	@PostConstruct
	public void doCarregarParametros(){
		doCarregarEmailException();
		doCarregarEmailExceptionAssunto();
		doCarregarIsCarregaMesAtualListaDeCliente();
		doCarregarIsExibeMetaQuantidade();
		doCarregarIsExibeMetaValor();
		doCarregarAmbiente();
	}
	
	/**
	 * Marca flag perfil gerencia.
	 */
	public void doCarregarIsCarregaMesAtualListaDeCliente(){
		setCarregaMesAtualListaDeCliente(getBooleanParameterValue(NomeParametro.CARREGA_MES_ATUAL_LISTA_DE_CLIENTES, false));
	}
	
	/**
	 * Retorna se o parametro EXIBIR META POR QUANTIDADE esta marcado
	 * @return
	 */
	public void doCarregarIsExibeMetaQuantidade() {
		setMetaValor(getBooleanParameterValue(NomeParametro.EXIBIR_META_POR_QUANTIDADE, false));
	}
	
	public void doCarregarEmailException() {
		setEmailException(getParameterValue(NomeParametro.EMAIL_EXCEPTION, null));
	}
	
	public void doCarregarEmailExceptionAssunto() {
		setEmailExceptionAssunto(getParameterValue(NomeParametro.EMAIL_EXCEPTION_ASSUNTO, null));
	}
	
	/**
	 * Retorna se o parametro EXIBIR META POR VALOR esta marcado
	 * @return
	 */
	public void doCarregarIsExibeMetaValor() {
		setMetaValor(getBooleanParameterValue(NomeParametro.EXIBIR_META_POR_VALOR, false));
	}
	
	/**
	 * Retorna o ambiente
	 */
	public void doCarregarAmbiente() {
		setAmbiente(getParameterValue(NomeParametro.AMBIENTE, null));
	}

	public boolean isAmbienteTeste(){
		boolean retorno = false;
		if (getAmbiente() != null){
			retorno = "TESTE".equalsIgnoreCase(getAmbiente().trim());
		}
		return retorno;
	}
	
	public boolean isAmbienteProducao(){
		boolean retorno = false;
		if (getAmbiente() != null){
			retorno = "PRODUCAO".equalsIgnoreCase(getAmbiente().trim());
		}
		return retorno;
	}
	
	public boolean isAmbienteDesenvolvimento(){
		boolean retorno = false;
		if (getAmbiente() != null){
			retorno = "DESENVOLVIMENTO".equalsIgnoreCase(getAmbiente().trim());
		}
		return retorno;
	}
	
	/**
	 * Retorna o texto do rodapé.
	 * @return
	 */
	public String getTextoRodape(){
		String retorno = null;
		
		final Parametro p = getParametroFacade().findByNome(NomeParametro.RODAPE_PRINCIPAL_WEB.getDescricao());
		if (p != null){
			retorno = p.getValor();
		}
		
		return retorno;
	}

	/**
	 * Retorna de acordo com o parametro de meta informado.
	 * @param nomeParametro
	 * @return
	 */
	protected boolean getBooleanParameterValue(NomeParametro nomeParametro, boolean defaultValue) {
		final Parametro par = getParametroFacade().findByNome(nomeParametro.getDescricao());
		boolean exibe = defaultValue;
		if (par != null && par.getValor() != null){
			String valor = String.valueOf(defaultValue);
			//  Parametro numerico
			Integer vaux = null;
			try {
				vaux = Integer.parseInt(par.getValor());
			} catch (Exception e){ }
			if (vaux != null){
				if (vaux > 0){
					valor = "true";
				} else {
					valor = "false";
				}
			}
			// --------------------
			exibe = Boolean.parseBoolean(valor);
		}
		return exibe;
	}

	/**
	 * Retorna de acordo com o parametro de meta informado.
	 * @param nomeParametro
	 * @return
	 */
	protected String getParameterValue(NomeParametro nomeParametro, String defaultValue) {
		final Parametro par = getParametroFacade().findByNome(nomeParametro.getDescricao());
		String retorno = defaultValue;
		if (par != null && par.getValor() != null){
			String valor = String.valueOf(par.getValor());
			retorno = valor;
		}
		return retorno;
	}
	
	/**
	 * Verifica se o usuario logado possui o perfil para o parametro informado - relativo apenas aos parametros de perfil -.
	 * @param nomeParametro
	 * @return
	 */
	protected boolean isUsuaruiComPerfilByParametro(NomeParametro nomeParametro){
		boolean retorno = false;
		final Parametro par = getParametroFacade().findByNome(nomeParametro.getDescricao());
		if (par == null){
			throw new GenericBusinessException("Parâmetro perfil [" + nomeParametro.getDescricao() + "] não encontrado!");
		} else {
			final Perfil perfil = getPerfilFacade().findById(Long.parseLong(par.getValor()));
			if (perfil == null){
				throw new GenericBusinessException("Parâmetro perfil [" + nomeParametro.getDescricao() + "] com o valor [" + par.getValor() + "] não representa um Perfil existente!");
			} else {
				// Verifica se o usuario possui o parametro
				final List<Perfil> listPerfis = ((Usuario) getLoginBean().getLoggedUserDetails()).getPerfis();
				if (listPerfis != null){
					for (Perfil p: listPerfis){
						if (p.equals(perfil)){
							retorno = true;
							break;
						}
					}
				}
			}
		}
		
		return retorno;
	}


	public boolean isExibeMetaQuantidade() {
		return metaQuantidade;
	}
	
	public boolean isExibeMetaValor() {
		return metaValor;
	}

	protected LoginBean getLoginBean() {
		return (LoginBean) FacesUtil.resolveExpression("#{loginBean}");
	}
	
	/**
	 * Formata a data conforme dd/MM/yyyy.
	 * @param date
	 * @param formato
	 * @return
	 */
	public String convertDateToString(Date date){
		return convertDateToString(date, "dd/MM/yyyy");
	}
	
	/**
	 * Formata a data conforme desejado.
	 * @param date
	 * @param formato
	 * @return
	 */
	public String convertDateToString(Date date, String formato){
		String retorno = "";
		if (formato == null){
			throw new GenericBusinessException("Formato não informado para converter data!");
		}
		if (date != null){
			retorno = new SimpleDateFormat(formato).format(date);
		}
		return retorno;
	}
	
	/**
	 * Verifica se a regra, ou as regras separadas por "," (virgula), estão aplicadas.
	 * @param role
	 * @return
	 */
	public boolean areAnyGranted(String role) {
		boolean retorno = false;
		if (role != null){
			String[] roles = role.split(",");
			for (int i = 0; i < roles.length; i++){
				roles[i] = roles[i].trim();
			}
			retorno =  areAnyGranted(roles);
		}
		
		return retorno;
	}
	
	/**
	 * Verifica se as regras são aplicadas ao usuário logado.
	 * @param roles
	 * @return
	 */
	private boolean areAnyGranted(String[] roles) {
	    boolean result = false;
	    for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
	        String userRole = authority.getAuthority();
	        for (String role : roles) {
	            if (role.equals(userRole)) {
	                result = true;
	                break;
	            }
	        }

	        if (result) {
	            break;
	        }
	    }

	    return result;
	}
	
	// GETTERS && SETTERS
	
	public ParametroFacade getParametroFacade() {
		return parametroFacade;
	}
	
	public void setParametroFacade(ParametroFacade parametroFacade) {
		this.parametroFacade = parametroFacade;
	}

	public PerfilFacade getPerfilFacade() {
		return perfilFacade;
	}

	public void setPerfilFacade(PerfilFacade perfilFacade) {
		this.perfilFacade = perfilFacade;
	}

	protected void setMetaValor(boolean metaValor) {
		this.metaValor = metaValor;
	}

	protected void setMetaQuantidade(boolean metaQuantidade) {
		this.metaQuantidade = metaQuantidade;
	}

	public String getEmailException() {
		return emailException;
	}

	public void setEmailException(String emailException) {
		this.emailException = emailException;
	}

	public String getEmailExceptionAssunto() {
		return emailExceptionAssunto;
	}

	public void setEmailExceptionAssunto(String emailExceptionAssunto) {
		this.emailExceptionAssunto = emailExceptionAssunto;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public boolean isCarregaMesAtualListaDeCliente() {
		return carregaMesAtualListaDeCliente;
	}

	public void setCarregaMesAtualListaDeCliente(
			boolean carregaMesAtualListaDeCliente) {
		this.carregaMesAtualListaDeCliente = carregaMesAtualListaDeCliente;
	}
}