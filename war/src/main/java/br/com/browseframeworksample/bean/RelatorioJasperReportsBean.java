package br.com.browseframeworksample.bean;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperPrint;
import br.com.browseframeworksample.bean.report.ApplicationReportBufferBean;
import br.com.browseframeworksample.bean.report.ReportBuffer;
import br.com.browseframeworksample.domain.Parametro;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.domain.enums.NomeParametro;
import br.com.browseframeworksample.facade.ParametroFacade;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;
import br.com.browseframework.report.jasper.JasperReportBuilderServlet;
import br.com.browseframework.report.jasper.parameter.JasperReportParametersHelper;
import br.com.browseframework.report.jasper.parameter.ReportParameterHelper;
import br.com.browseframework.util.date.DateFormatterUtil;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean(name = "relatorioJasperReportsBean")
@SessionScoped
@URLMapping(id = "relatorio", viewId = "/secured_relatorio.jsf", pattern = "/secured/relatorio")
public class RelatorioJasperReportsBean {
	
	protected final String HAS_PROMPTABLE_PARAMTERS = "HAS_PROMPTABLE_PARAMTERS";
	
	@ManagedProperty(name="applicationReportBufferBean", value ="#{applicationReportBufferBean}")
	private ApplicationReportBufferBean applicationReportBufferBean;
	
	@ManagedProperty(name="jasperReportBuilderServlet", value ="#{jasperReportBuilderServlet}")
	private JasperReportBuilderServlet jasperReportBuilderServlet;
	
	@ManagedProperty(name="aiDataSource", value ="#{aiDataSource}")
	private DataSource aiDataSource;
	
	@ManagedProperty(value = "#{eMailSenderBean}")
	private EMailSenderBean eMailSenderBean;
	
	private String email = null;
	private String subject = null;

	// -----------------------
	// Relatorio em execuçao
	// -----------------------
	private ReportBuffer currentReport;
	private List<ReportParameterHelper> currentListParameters;
	// -----------------------
	
	/**
	 * Carrega o relatório.
	 * @param relativePath
	 */
	public void doCarregarRelatorio(String relativePath){
		final ReportBuffer rb = applicationReportBufferBean.loadReport(relativePath);
		if (rb == null){
			throw new GenericBusinessException("Não foi possível obter um relatório a partir do caminho [" + relativePath + "]");
		}
        // Adiciona o relatorio como em execucao
		setCurrentReport(rb);

		// Cria uma nova lista
		setCurrentListParameters(new ArrayList<ReportParameterHelper>());
		
		// Verifica se o relatorio possui parametros a sugerir para o usuario 
		if (rb.getListPromptableReportParameterHelper() != null && !rb.getListPromptableReportParameterHelper().isEmpty()){
			FacesUtil.addRequestContextCallbackParam(HAS_PROMPTABLE_PARAMTERS, true);
			// Copia a lista de parametros default
			for (ReportParameterHelper rph: rb.getListPromptableReportParameterHelper()){
				try {
					getCurrentListParameters().add((ReportParameterHelper) rph.clone());
				} catch (CloneNotSupportedException e) {
					throw new GenericBusinessException("Não foi possível clonar o parâmetro [" + rph.getName() + "]. Erro [" + e.getMessage() + "]");
				}
			}
		} else {
			FacesUtil.addRequestContextCallbackParam(HAS_PROMPTABLE_PARAMTERS, false);
			
		}
	}
	
	/**
	 * Gera um relatorio PDF descrevendo o Erro capturado.
	 * @param e
	 * @param out
	 * @throws DocumentException
	 */
	protected void buildPdfErrorToReturn(Exception e, final OutputStream out) throws DocumentException{
		final Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, out);
		document.open();
		
		// Logo de erro
		try {
			final Image img = Image.getInstance(getClass().getResource("/images/error.png"));
			img.setAlignment(Element.ALIGN_CENTER);
			document.add(img);			
		} catch (Exception e1) { }
		
		// Titulo
		final Font fonteTitulo = new Font(Font.COURIER, 20, Font.BOLD);
		final Paragraph p1 = new Paragraph("Erro ao processar o relatório: " + getCurrentReport().getReportTitle(), fonteTitulo);
		p1.setAlignment(Element.ALIGN_CENTER);
		p1.setSpacingAfter(20);
		document.add(p1);

		// Parametros
		final PdfPTable table = new PdfPTable(2);
		final PdfPCell header = new PdfPCell(new Paragraph("Parâmetros utilizados:"));
		header.setColspan(2);
		table.addCell(header);
		for (ReportParameterHelper rp : getCurrentListParameters()){
			table.addCell(rp.getName());
			table.addCell(String.valueOf(rp.getValue()));
		}
		document.add(table);
		
		// Stack
		final Font fonteTituloStack = new Font(Font.COURIER, 12, Font.BOLD, Color.RED);
		final Paragraph pStack1 = new Paragraph("Mensagem de erro: " + e.getMessage(), fonteTituloStack);
		pStack1.setAlignment(Element.ALIGN_LEFT);
		document.add(pStack1);
		
		document.close();
		try {
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Processa o relatório e prepara o retorno do Pdf stream.
	 * @throws IOException
	 */
	@URLAction
	public void doExecutarRelatorioERetornarPdfStream() {
		// HttpServletResponse
	    final FacesContext context = FacesContext.getCurrentInstance();
	    final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		// Processa o relatório
		// ---------------------
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = doProcessarRelatorioCorrente();
		} catch (Exception e){
			try {
				if (e != null){
					doNotifyFatalErrorOnReport(e, e.getCause());
					// Retorna um pdf apresentando o erro capturado
					buildPdfErrorToReturn(e, response.getOutputStream());
				}
			} catch (DocumentException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if (jasperPrint != null){
			// Prepara o retorno do Pdf
			// -------------------------
		    // Converte o jasper print para Pdf
		    try {
				getJasperReportBuilderServlet().doWritePdf(jasperPrint, response);
			} catch (Exception e) {
				try {
					if (e != null){
						doNotifyFatalErrorOnReport(e, e.getCause());
						// Retorna um pdf apresentando o erro capturado
						buildPdfErrorToReturn(e, response.getOutputStream());
					}
				} catch (DocumentException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				jasperPrint = null;
			}
		} else {
			// Marca a resposta como completa
			context.responseComplete();  
		}
	}
	
	/**
	 * Processa a execução do relatório corrente.
	 * @return
	 */
	protected JasperPrint doProcessarRelatorioCorrente() {
		JasperPrint retorno = null;
		
		Connection conn = null;
		try {
			// Obtém a connection do Data Source
			conn = getAiDataSource().getConnection();
			// Locale
			final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			final Map<String, Object> mapPar = JasperReportParametersHelper.convertListToParameterMap(getCurrentListParameters());
			if (!mapPar.containsKey("REPORT_LOCALE")){
				if (locale != null){
					mapPar.put("REPORT_LOCALE", locale);
				}
			}
			// Builds
			retorno = getJasperReportBuilderServlet().buildReport(
					getCurrentReport().getJasperReport(), 
					getCurrentReport().getReportResourceBundleRelativePath(), 
					mapPar, 
					locale.getLanguage()+"_"+locale.getCountry(), 
					conn);  

		} catch (SQLException e) {
			throw new GenericBusinessException("Erro de SQL ao processar relatório:\n" + "Erro:" + e.getMessage());
		} catch (Exception e) {
			throw new GenericBusinessException("Erro inesperado ao processar relatório:\n" + "Erro:" + e.getMessage());
		} finally {
			if (conn != null){
				try {
					conn.close(); // finaliza a conexão.
				} catch (SQLException e) {
					throw new GenericBusinessException("Não foi possível fechar a conexão após executar relatório!");
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Realiza o envio por email.
	 */
	public void doNotifyFatalErrorOnReport(Throwable t, Throwable cause) {
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
					if (getCurrentReport() != null){
						msgUserDetails += "----------------------------------------------------------\n";
						msgUserDetails += "- RELATORIO: " + getCurrentReport().getReportRelativePath() + "\n";
						msgUserDetails += "----------------------------------------------------------\n";
						msgUserDetails += "- PARÂMETROS: \n";
						for (ReportParameterHelper rp : getCurrentListParameters()){
							msgUserDetails += rp.getName() + " => " + ( rp.getValue() != null ? String.valueOf(rp.getValue()) : "" )+ "\n";
						}
						msgUserDetails += "\n";
					}
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
				mail.sendMail(null, to, subject + ":Relatório", message);
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
	
	public void setAiDataSource(DataSource aiDataSource) {
		this.aiDataSource = aiDataSource;
	}
	
	public DataSource getAiDataSource() {
		return aiDataSource;
	}

	public ApplicationReportBufferBean getApplicationReportBufferBean() {
		return applicationReportBufferBean;
	}

	public void setApplicationReportBufferBean(
			ApplicationReportBufferBean applicationReportBufferBean) {
		this.applicationReportBufferBean = applicationReportBufferBean;
	}

	public ReportBuffer getCurrentReport() {
		return currentReport;
	}

	public void setCurrentReport(ReportBuffer currentReport) {
		this.currentReport = currentReport;
	}

	public JasperReportBuilderServlet getJasperReportBuilderServlet() {
		return jasperReportBuilderServlet;
	}

	public void setJasperReportBuilderServlet(
			JasperReportBuilderServlet jasperReportBuilderServlet) {
		this.jasperReportBuilderServlet = jasperReportBuilderServlet;
	}

	public List<ReportParameterHelper> getCurrentListParameters() {
		return currentListParameters;
	}

	public void setCurrentListParameters(
			List<ReportParameterHelper> currentListParameters) {
		this.currentListParameters = currentListParameters;
	}

	public EMailSenderBean geteMailSenderBean() {
		return eMailSenderBean;
	}

	public void seteMailSenderBean(EMailSenderBean eMailSenderBean) {
		this.eMailSenderBean = eMailSenderBean;
	}
}