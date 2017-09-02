package br.com.browseframeworksample.lifecycle.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.faces.context.ExceptionHandler;

import br.com.browseframeworksample.bean.EMailSenderBean;
import br.com.browseframeworksample.bean.LoginBean;
import br.com.browseframeworksample.domain.Parametro;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.domain.enums.NomeParametro;
import br.com.browseframeworksample.facade.ParametroFacade;
import br.com.browseframework.jsfprimefaces.listener.ExceptionHandlerInterceptor;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;
import br.com.browseframework.util.date.DateFormatterUtil;

public class AIExceptionHandlerInterceptor extends ExceptionHandlerInterceptor {

	private String email = null;
	private String subject = null;
	
	public AIExceptionHandlerInterceptor(ExceptionHandler wrapped) {
		super(wrapped);
	}

	/**
	 * Realiza o envio por email.
	 */
	@Override
	public void doNotifyFatalError(Throwable t, Throwable cause) {
		super.doNotifyFatalError(t, cause);
 
		final EMailSenderBean mail = (EMailSenderBean) FacesUtil.resolveExpression("#{eMailSenderBean}");
		final LoginBean login = (LoginBean) FacesUtil.resolveExpression("#{loginBean}");
		
		String to = null;
		if (getEmail() != null){
			to = getEmail();
			String subject = getSubject();
			if (subject == null){
				subject = "Erro";
			}
			
			// Detalhes do usuário
			String message = null;
			if (login != null){
				String msgUserDetails = "";
				Usuario u = (Usuario) login.getLoggedUserDetails();
				if (u != null){
					msgUserDetails += "----------------------------------------------------------\n";
					msgUserDetails += "Data/Hr.: " + DateFormatterUtil.convertDateToString(new Date(), "dd/MM/yyy HH:mm") + "\n";
					msgUserDetails += "Usuário.: " + u.getApelido() + "\n";
					msgUserDetails += "Pessoa..: " + u.getPessoaFisica().getNome() + "\n";
					msgUserDetails += "----------------------------------------------------------\n";
					message = msgUserDetails + "\n";
				}
			}
			
			// Mensagem de erro
			message += "Erro....: " + cause == null ? t.getMessage() : cause.getMessage() + "\n";
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			message += "Detalhe.: " + sw.toString() + "\n";
			
			if (mail != null){
				mail.sendMail(null, to, subject, message);
			}
		}
	}
	
	/**
	 * Recupera um parametro
	 * @param nomeParametro
	 * @param defaultValue
	 * @return
	 */
	protected String getParameterValue(NomeParametro nomeParametro, String defaultValue) {
		final ParametroFacade parametroFacade = (ParametroFacade) FacesUtil.resolveExpression("#{parametroFacade}");
		final Parametro par = parametroFacade.findByNome(nomeParametro.getDescricao());
		String retorno = defaultValue;
		if (par != null && par.getValor() != null){
			String valor = String.valueOf(par.getValor());
			retorno = valor;
		}
		return retorno;
	}

	/**
	 * Recupera o parametro email exception
	 * @return
	 */
	public String getEmail() {
		if (email == null){
			email = getParameterValue(NomeParametro.EMAIL_EXCEPTION, null);
		}
		return email;
	}	

	/**
	 * Recupera o parametro email exception assunto
	 * @return
	 */
	public String getSubject() {
		if (subject == null){
			subject = getParameterValue(NomeParametro.EMAIL_EXCEPTION_ASSUNTO, null);
		}
		return subject;
	}

	// GETTERS && SETTERS
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
}