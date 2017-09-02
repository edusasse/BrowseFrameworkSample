package br.com.browseframeworksample.bean.report;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.jfree.util.Log;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.report.jasper.JasperReportBuilderServlet;

@ManagedBean(name="applicationReportBufferBean")
@ApplicationScoped
public class ApplicationReportBufferBean {
	
	@ManagedProperty(name="jasperReportBuilderServlet", value ="#{jasperReportBuilderServlet}")
	private JasperReportBuilderServlet jasperReportBuilderServlet;
	
	// Utiliza o caminho para o relatorio como chave
	private Map<String, ReportBuffer> mapBuffer = new HashMap<String, ReportBuffer>();

	/**
	 * Carrega o relatório, se necessário, e retorna seu objeto de buffer. 
	 * @param relativePath
	 */
	public ReportBuffer loadReport(String relativePath){
		if (!getMapBuffer().containsKey(relativePath)){
			// Certifica-se que o relatório existe.
			try {
				if (getJasperReportBuilderServlet().getJasperReport(relativePath) == null){
					throw new GenericBusinessException("Não foi possível encontrar o relatório no caminho [" + relativePath + "]");
				}
			} catch (Exception e1) {
				throw new GenericBusinessException("Erro ao carregar o relatório no caminho [" + relativePath + "]");
			}
			// Cria o objeto de buffer
			final ReportBuffer rb = new ReportBuffer(relativePath);
			// .. e adiciona ao mapa
			getMapBuffer().put(relativePath, rb);
			
			// Titulo do Relatorio
			try {
				rb.setReportTitle(getJasperReportBuilderServlet().getJasperReportTitle(relativePath));
			} catch (Exception e1) {
				Log.error("Não foi possível obter o titulo do relatório");
				rb.setReportTitle("**Titulo Não Definido**");
			}
			
			// Jasper Report file
			try {
				rb.setJasperReport(getJasperReportBuilderServlet().getJasperReport(relativePath));
			} catch (Exception e1) {
				throw new GenericBusinessException("Não foi possível carregar o arquivo [" + relativePath + "]");
			}

			// Carrega os parâmetros
			try {
				rb.setReportParameterList(getJasperReportBuilderServlet().getJasperReportParamters(rb.getReportRelativePath()));
			} catch (Exception e) {
				throw new GenericBusinessException("Não foi possível carregar os parâmetros a partir do relatório [" + relativePath + "]");
			}
		}
		
		return getMapBuffer().get(relativePath);
	}
	
	// GETTERS && SETTERS
	
	public Map<String, ReportBuffer> getMapBuffer() {
		return mapBuffer;
	}

	public void setMapBuffer(Map<String, ReportBuffer> mapBuffer) {
		this.mapBuffer = mapBuffer;
	}

	public JasperReportBuilderServlet getJasperReportBuilderServlet() {
		return jasperReportBuilderServlet;
	}

	public void setJasperReportBuilderServlet(
			JasperReportBuilderServlet jasperReportBuilderServlet) {
		this.jasperReportBuilderServlet = jasperReportBuilderServlet;
	}
	
}
