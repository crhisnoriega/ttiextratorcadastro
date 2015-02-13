package br.com.tti.extratorcadastro.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLGenerator {

	public static Hashtable<String, XMLGenerator> tableGen = new Hashtable<String, XMLGenerator>();

	public static XMLGenerator createXMLGenerator(String packageName) {
		XMLGenerator xmlGen = tableGen.get(packageName);
		if (xmlGen == null) {
			xmlGen = new XMLGenerator(packageName);
			tableGen.put(packageName, xmlGen);
		}
		return xmlGen;
	}

	private JAXBContext jc;
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;

	public XMLGenerator(String packageName) {
		try {
			jc = JAXBContext.newInstance(packageName);
			unmarshaller = jc.createUnmarshaller();
			marshaller = jc.createMarshaller();

		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
	}

	synchronized private void toXMLFile(Object obj, File file) throws Exception {
		FileOutputStream os = new FileOutputStream(file);
		this.marshaller.marshal(obj, os);
		os.close();
	}

	synchronized public String toXMLString(Object obj) throws Exception {
		StringWriter w = new StringWriter();
		this.marshaller.marshal(obj, w);
		this.marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		return w.toString();
	}

	synchronized public Object toObjectFromFile(File file) throws Exception {
		this.unmarshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		return unmarshaller.unmarshal(file);
	}

	synchronized public Object toObject(String str) throws Exception {
		StringReader r = new StringReader(str);
		this.unmarshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		return unmarshaller.unmarshal(r);
	}

	public Marshaller getMarshaller() {
		return marshaller;
	}

	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}

}
