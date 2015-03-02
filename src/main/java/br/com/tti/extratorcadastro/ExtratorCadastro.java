package br.com.tti.extratorcadastro;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBElement;

import br.com.tti.extratorcadastro.db.ConexaoBD;
import br.com.tti.extratorcadastro.db.ConexaoBD.TIPO_BANCO;
import br.com.tti.extratorcadastro.email.EmailSender;
import br.com.tti.extratorcadastro.excel.ExcelDocumentoGenerador;
import br.com.tti.extratorcadastro.xml.XMLGenerator;
import br.com.tti.extratorcadastro.xml.esquemas.cte.TCTe;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe.InfNFe;
import br.com.tti.sefaz.systemconfig.schema.ArquivoConfiguracao.ConfiguracaoSistema;
import br.com.tti.tools.ReadFile;

public class ExtratorCadastro {

	private ConexaoBD conx;
	private EmailSender emailSender;
	private Date ultimaData;
	private SimpleDateFormat sdf;
	private String email;
	private List<String> cnpjsv;
	private Long maxl;

	public ExtratorCadastro(ConfiguracaoSistema conf, List<String> cnpjsv,
			String email, Long maxl) {
		this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SSS");
		this.email = email;
		this.cnpjsv = cnpjsv;
		this.maxl = maxl;

		try {
			this.conx = new ConexaoBD(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.emailSender = new EmailSender();
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

	private int fileNumber = 1;

	@SuppressWarnings("unchecked")
	public void extract() throws Exception {

		ExcelDocumentoGenerador excelgen = new ExcelDocumentoGenerador(
				new File(System.getProperty("user.dir")
						+ File.separator
						+ File.createTempFile(
								"log_tti_" + (fileNumber++) + "_", ".log")
								.getName()), new Hashtable<String, String>());
		excelgen.init();

		XMLGenerator nfeXMLGen = new XMLGenerator(
				"br.com.tti.extratorcadastro.xml.esquemas.nfe");
		XMLGenerator cteXMLGen = new XMLGenerator(
				"br.com.tti.extratorcadastro.xml.esquemas.cte");

		ResultSet result = this.conx.findXMLFromRS(this.ultimaData);
		// for (String xmlString : xmls) {

		long counter = 0L;

		while (result.next()) {

			boolean wasAdded = false;

			String xmlString = result.getString("XMLSTRING");

			if (xmlString == null || xmlString.isEmpty()) {
				continue;
			}

			if (xmlString.contains("<NFe")) {
				xmlString = this.extractNFeXML(xmlString);
				TNFe nfe = ((JAXBElement<TNFe>) nfeXMLGen.toObject(xmlString))
						.getValue();

				wasAdded = excelgen
						.fill(nfe.getInfNFe().getEmit(), this.cnpjsv);
				wasAdded = excelgen
						.fill(nfe.getInfNFe().getDest(), this.cnpjsv);
				wasAdded = excelgen.fill(nfe.getInfNFe().getTransp()
						.getTransporta(), this.cnpjsv);
			}

			if (xmlString.contains("<CTe")) {
				xmlString = this.extractCTeXML(xmlString);
				TCTe cte = ((JAXBElement<TCTe>) cteXMLGen.toObject(xmlString))
						.getValue();

				wasAdded = excelgen
						.fill(cte.getInfCte().getEmit(), this.cnpjsv);
				wasAdded = excelgen
						.fill(cte.getInfCte().getDest(), this.cnpjsv);
				wasAdded = excelgen.fill(cte.getInfCte().getRem(), this.cnpjsv);
				wasAdded = excelgen.fill(cte.getInfCte().getReceb(),
						this.cnpjsv);
				wasAdded = true;
				wasAdded = excelgen.fill(cte.getInfCte().getExped(),
						this.cnpjsv);

				wasAdded = excelgen.fill(cte.getInfCte().getIde().getToma4(),
						this.cnpjsv);

			}

			try {

				if (wasAdded) {
					counter++;
				}

				if (counter >= this.maxl) {
					counter = 0;
					System.gc();

					// enviando email
					if (excelgen != null) {
						File excelfile = excelgen.getFile();

						System.out.println("Arquivo:"
								+ excelfile.getAbsolutePath());
						if (excelfile.exists() && excelfile.length() != 0) {
							System.out.println("Arquivo OK");
						}

						this.emailSender.myMailRaw(
								cleanEmails(this.email),
								"cadastro empresas: "
										+ this.sdf.format(this.ultimaData),
								"arquivo: " + excelfile.getName(),
								new String[] { excelfile.getAbsolutePath() });

						while (!excelfile.delete()) {
							Thread.sleep(500);
						}

						System.out.println("");
					}

					// criando nova planilha
					{
						excelgen = new ExcelDocumentoGenerador(new File(
								System.getProperty("user.dir")
										+ File.separator
										+ File.createTempFile(
												"log_tti_" + (fileNumber++)
														+ "_", ".log")
												.getName()),
								new Hashtable<String, String>());
						excelgen.init();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		{
			if (counter != 0) {
				File excelfile = excelgen.getFile();

				System.out.println("Arquivo:" + excelfile.getAbsolutePath());
				if (excelfile.exists() && excelfile.length() != 0) {
					System.out.println("Arquivo OK");
				}

				this.emailSender.myMailRaw(
						cleanEmails(this.email),
						"cadastro empresas: "
								+ this.sdf.format(this.ultimaData), "arquivo: "
								+ excelfile.getName(),
						new String[] { excelfile.getAbsolutePath() });

				this.ultimaData = Calendar.getInstance().getTime();
				this.updateUltimaData();

				while (!excelfile.delete()) {
					Thread.sleep(500);
				}

				System.out.println("");
			} else {
				System.out.println("Nada novo");

				this.ultimaData = Calendar.getInstance().getTime();
				this.updateUltimaData();

				File excelfile = excelgen.getFile();
				while (!excelfile.delete()) {
					Thread.sleep(500);
				}
			}
		}
	}

	private static String[] cleanEmails(String all) {
		String[] mm = all.split("\\,");
		Vector<String> mmv = new Vector<String>();

		for (String mail : mm) {
			mail = mail.trim();
			if (mail != null && !mail.trim().isEmpty() && !mail.equals("null")
					&& !mail.equals("@")) {
				if (!mmv.contains(mail)) {
					mmv.add(mail);
				}
			}
		}

		String[] mms = new String[mmv.size()];
		mmv.toArray(mms);
		return mms;
	}

	// /////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////
	public static void main(String[] args) {
		ExtratorCadastro ex = new ExtratorCadastro(null, null,
				"crhisnoriega@gmail.com", 200L);

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
