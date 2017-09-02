package br.com.browseframeworksample.bean.report;

import java.util.List;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import br.com.browseframework.report.jasper.enums.ExportFormat;
import br.com.browseframework.report.jasper.parameter.JasperReportParametersHelper;
import br.com.browseframework.report.jasper.parameter.ReportParameterHelper;

public class ReportBuffer {

	private String reportTitle;
	private String reportRelativePath;
	private String reportResourceBundleRelativePath;
	private JasperReport jasperReport;
	private List<JRParameter> reportParameterList;
	private ExportFormat exportFormat;
	private List<ReportParameterHelper> listReportParameterHelper = null;
	private List<ReportParameterHelper> listPromptableReportParameterHelper = null;

	public ReportBuffer(String reportRelativePath) {
		setReportRelativePath(reportRelativePath);
	}
	
	/**
	 * Returns a singleton list.
	 * @return
	 */
	public List<ReportParameterHelper> getListReportParameterHelper(){
		if (this.listReportParameterHelper == null){
			setListReportParameterHelper(JasperReportParametersHelper.getListOfReportParameterHelperFromJRParameterList(reportParameterList));
		}
		
		return this.listReportParameterHelper;
	}
	
	/**
	 * Returns a singleton list.
	 * @return
	 */
	public List<ReportParameterHelper> getListPromptableReportParameterHelper(){
		if (this.listPromptableReportParameterHelper == null){
			setListPromptableReportParameterHelper(JasperReportParametersHelper.getPromptableParametersList(getListReportParameterHelper()));
		}
		
		return this.listPromptableReportParameterHelper;
	}

	// GETTERS && SETTERS

	public String getReportRelativePath() {
		return reportRelativePath;
	}

	public void setReportRelativePath(String reportRelativePath) {
		this.reportRelativePath = reportRelativePath;
	}

	public String getReportResourceBundleRelativePath() {
		return reportResourceBundleRelativePath;
	}

	public void setReportResourceBundleRelativePath(
			String reportResourceBundleRelativePath) {
		this.reportResourceBundleRelativePath = reportResourceBundleRelativePath;
	}

	public ExportFormat getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(ExportFormat exportFormat) {
		this.exportFormat = exportFormat;
	}

	public List<JRParameter> getReportParameterList() {
		return reportParameterList;
	}

	public void setReportParameterList(List<JRParameter> reportParameterList) {
		this.reportParameterList = reportParameterList;
	}

	public void setListReportParameterHelper(
			List<ReportParameterHelper> listReportParameterHelper) {
		this.listReportParameterHelper = listReportParameterHelper;
	}

	public void setListPromptableReportParameterHelper(
			List<ReportParameterHelper> listPromptableReportParameterHelper) {
		this.listPromptableReportParameterHelper = listPromptableReportParameterHelper;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public JasperReport getJasperReport() {
		return jasperReport;
	}

	public void setJasperReport(JasperReport jasperReport) {
		this.jasperReport = jasperReport;
	}
}
