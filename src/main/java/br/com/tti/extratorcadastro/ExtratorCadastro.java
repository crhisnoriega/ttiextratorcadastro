package br.com.tti.extratorcadastro;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBElement;

import br.com.tti.extratorcadastro.db.ConexaoBancoDados;
import br.com.tti.extratorcadastro.db.ConexaoBancoDados.TIPO_BANCO;
import br.com.tti.extratorcadastro.excel.ExcelDocumentoGenerador;
import br.com.tti.extratorcadastro.xml.XMLGenerator;
import br.com.tti.extratorcadastro.xml.esquemas.cte.TCTe;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe.InfNFe;
import br.com.tti.sefaz.systemconfig.schema.ArquivoConfiguracao.ConfiguracaoSistema;
import br.com.tti.tools.ReadFile;

public class ExtratorCadastro {

	private ConexaoBancoDados conx;
	private Date ultimaData;
	private SimpleDateFormat sdf;

	public ExtratorCadastro(ConfiguracaoSistema conf) {
		this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SSS");

		try {
			this.conx = new ConexaoBancoDados(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.ultimaData = this.sdf.parse(ReadFile.readFile("ultimadata"));
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			try {
				this.ultimaData = this.sdf.parse("01/01/2009 00:00:00");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			this.updateUltimaData();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateUltimaData() {
		try {
			ReadFile.writeFile("ultimadata", this.sdf.format(this.ultimaData)
					.toCharArray(), "UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private String extractNFeXML(String xml) {
		int pos1 = xml.indexOf("<NFe");
		int pos2 = xml.indexOf("</NFe>");

		if (pos1 != -1 && pos2 != -1) {
			String tmp = xml.substring(pos1, pos2 + "</NFe>".length());
			tmp = this.putHeader(tmp);
			return tmp;
		}

		return xml;
	}

	private String putHeader(String xml) {
		int pos1 = xml.indexOf("<infNFe");

		if (pos1 != -1) {
			String substring = xml.substring(pos1, xml.length());
			String header = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">";
			return header + substring;
		}

		return xml;
	}

	private String putHeaderCTe(String xml) {
		int pos1 = xml.indexOf("<infCte");

		if (pos1 != -1) {
			String substring = xml.substring(pos1, xml.length());
			String header = "<CTe xmlns=\"http://www.portalfiscal.inf.br/cte\">";
			return header + substring;
		}

		return xml;
	}

	private String extractCTeXML(String xml) {
		int pos1 = xml.indexOf("<CTe");
		int pos2 = xml.indexOf("</CTe>");

		if (pos1 != -1 && pos2 != -1) {
			String tmp = xml.substring(pos1, pos2 + "</CTe>".length());
			tmp = this.putHeaderCTe(tmp);
			return tmp;
		}

		return xml;
	}

	public void extract() throws Exception {
		ExcelDocumentoGenerador excelgen = new ExcelDocumentoGenerador(
				"log_reg.1.xls", new Hashtable<String, String>());
		excelgen.init();

		XMLGenerator nfeXMLGen = new XMLGenerator(
				"br.com.tti.extratorcadastro.xml.esquemas.nfe");
		XMLGenerator cteXMLGen = new XMLGenerator(
				"br.com.tti.extratorcadastro.xml.esquemas.cte");

		List<String> xmls = this.conx.findXMLs(this.ultimaData);
		for (String xmlString : xmls) {
			if (xmlString == null) {
				continue;
			}
			System.out.println(xmlString.substring(0, 30));
			if (xmlString.contains("<NFe")) {
				xmlString = this.extractNFeXML(xmlString);
				TNFe nfe = ((JAXBElement<TNFe>) nfeXMLGen.toObject(xmlString))
						.getValue();
				excelgen.fill(nfe.getInfNFe().getEmit());
				// excelgen.fill(nfe.getInfNFe().getDest());
				// excelgen.fill(nfe.getInfNFe().getTransp());
			}

			if (xmlString.contains("<CTe")) {
				xmlString = this.extractCTeXML(xmlString);
				TCTe cte = ((JAXBElement<TCTe>) cteXMLGen.toObject(xmlString))
						.getValue();
				excelgen.fill(cte.getInfCte().getEmit());
				excelgen.fill(cte.getInfCte().getDest());
				excelgen.fill(cte.getInfCte().getRem());
				excelgen.fill(cte.getInfCte().getReceb());
				excelgen.fill(cte.getInfCte().getExped());
				excelgen.fill(cte.getInfCte().getIde().getToma4());
			}

		}

		File excelfile = excelgen.getFile();
		System.out.println(excelfile.getAbsolutePath());
		this.ultimaData = Calendar.getInstance().getTime();
		this.updateUltimaData();
	}

	// /////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////
	public static void main(String[] args) {
		ExtratorCadastro ex = new ExtratorCadastro(null);

		TNFe t1 = new TNFe();
		t1.setInfNFe(new InfNFe());
		t1.getInfNFe().setId("MyID");

		try {

			XMLGenerator gen = new XMLGenerator(
					"br.com.tti.extratorcadastro.xml.esquemas.nfe");

			String xml1 = gen.toXMLString(t1);
			System.out.println("xml: " + gen.toXMLString(t1));
			javax.xml.bind.JAXBElement<TNFe> t2 = (javax.xml.bind.JAXBElement<TNFe>) gen
					.toObject(xml1);
			System.out.println(t2.getValue().getInfNFe().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
