package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] geraRelatorioPdf(List listaDados, String nomeRelatorio, ServletContext servletContext)
			throws Exception {

		/* Cria a lista de dados que vem do nosso SQL da consulta feita */
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados);

		String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";

		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashMap(), jrbcds);

		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}

	public byte[] geraRelatorioPdf(List listaDados, String nomeRelatorio, HashMap<String, Object> params,
			ServletContext servletContext) throws Exception {

		/* Cria a lista de dados que vem do nosso SQL da consulta feita */
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados);

		String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";

		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, params, jrbcds);

		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
	
	public byte[] geraRelatorioExcel(List listaDados, String nomeRelatorio, HashMap<String, Object> params,
			ServletContext servletContext) throws Exception {

		/* Cria a lista de dados que vem do nosso SQL da consulta feita */
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados);

		String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";

		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, params, jrbcds);
		
		 // Usando JRXlsxExporter
        JRXlsxExporter exporter = new JRXlsxExporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Configura o exportador
        exporter.setExporterInput(new SimpleExporterInput(impressoraJasper));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        
        // Exporta o relatório
        exporter.exportReport();
        
        return baos.toByteArray();
		
	}

}
