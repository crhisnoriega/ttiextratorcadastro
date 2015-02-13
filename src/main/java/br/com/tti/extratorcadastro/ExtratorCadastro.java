package br.com.tti.extratorcadastro;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.JAXBElement;

import br.com.tti.extratorcadastro.db.ConexaoBancoDados;
import br.com.tti.extratorcadastro.excel.ExcelDocumentoGenerador;
import br.com.tti.extratorcadastro.xml.XMLGenerator;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe.InfNFe;
import br.com.tti.tools.ReadFile;

public class ExtratorCadastro {

	private ConexaoBancoDados conx;
	private Date ultimaData;
	private SimpleDateFormat sdf;

	public ExtratorCadastro() {
		this.sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:SS");

		try {
			this.conx = new ConexaoBancoDados(
					"jdbc:jtds:sqlserver://mysqlserver.cqrgys6g7chq.sa-east-1.rds.amazonaws.com",
					"net.sourceforge.jtds.jdbc.Driver", "ttidb", "ttidb415");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.ultimaData = this.sdf.parse(ReadFile.readFile("ultimadata"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				ReadFile.writeFile("ultimadata",
						this.sdf.format(Calendar.getInstance().getTime())
								.toCharArray(), "UTF-8");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			this.ultimaData = Calendar.getInstance().getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void extract() throws Exception {
		ExcelDocumentoGenerador excelgen = new ExcelDocumentoGenerador(
				"log_reg.1", new Hashtable<String, String>());
		excelgen.init();

		XMLGenerator xmlgen = new XMLGenerator("");
		List<String> xmls = this.conx.findXMLs(this.ultimaData);
		for (String string : xmls) {
			TNFe nfe = ((JAXBElement<TNFe>) xmlgen.toObject(string)).getValue();
			excelgen.fillEmitenteNFe(nfe.getInfNFe().getEmit(), null);
		}

		File excelfile = excelgen.getFile();
	}

	// /////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////
	public static void main(String[] args) {
		ExtratorCadastro ex = new ExtratorCadastro();

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
