package br.com.browseframeworksample.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.mail.MailSender;

import br.com.browseframework.util.mail.AbstractEMailSender;

@ManagedBean(name = "eMailSenderBean")
@SessionScoped
public class EMailSenderBean extends AbstractEMailSender {

    @ManagedProperty(name = "mailSender", value = "#{mailSender}")
	private MailSender mailSender;
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframework.util.mail.AbstractEMailSender#getMailSender()
     */
	@Override
	protected org.springframework.mail.MailSender getMailSender() {
		return this.mailSender;
	}

	// GETTERS && SETTERS
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
}
