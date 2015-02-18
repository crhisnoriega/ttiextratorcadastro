package br.com.tti.sefaz.systemconfig;

import java.io.File;

import javax.xml.bind.JAXBElement;

import br.com.tti.extratorcadastro.xml.XMLGenerator;
import br.com.tti.sefaz.systemconfig.schema.ArquivoConfiguracao;

public class XMLConfigSystem {

	private String xmlFile;
	private ArquivoConfiguracao fileConfig;

	@SuppressWarnings("unchecked")
	public XMLConfigSystem(String xmlFile) throws Exception {
		this.xmlFile = xmlFile;

		XMLGenerator xmls = new XMLGenerator(
				"br.com.tti.sefaz.systemconfig.schema");

		File f = new File(this.xmlFile);

		JAXBElement<ArquivoConfiguracao> c = (JAXBElement<ArquivoConfiguracao>) xmls
				.toObjectFromFile(f);
		this.fileConfig = c.getValue();

	}

	public ArquivoConfiguracao getFileConfig() {
		return fileConfig;
	}

}
