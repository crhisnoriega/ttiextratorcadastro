//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.03 at 02:45:07 PM BRT 
//

package br.com.tti.sefaz.systemconfig.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for cnpjinfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="cnpjinfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cnpj" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nomeFantasia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cnpjinfo", propOrder = { "cnpj", "nomeFantasia", "tipoemp" })
public class Cnpjinfo {

	public static enum TIPOEMP {
		DEST, EMIT, REM, TRANSP, EXPED, RECEB, TOMA, OUTRO, TOMA_NFSE, PREST
	}

	@XmlElement(required = true)
	protected String cnpj;

	@XmlElement(required = true)
	protected String nomeFantasia;

	@XmlElement(required = true)
	protected TIPOEMP tipoemp;

	public TIPOEMP getTipoemp() {
		return tipoemp;
	}

	public void setTipoemp(TIPOEMP tipoemp) {
		this.tipoemp = tipoemp;
	}

	/**
	 * Gets the value of the cnpj property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * Sets the value of the cnpj property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCnpj(String value) {
		this.cnpj = value;
	}

	/**
	 * Gets the value of the nomeFantasia property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	/**
	 * Sets the value of the nomeFantasia property.
	 * 
	 * @param value
	 *            allowed object is {@link String } -
	 */
	public void setNomeFantasia(String value) {
		this.nomeFantasia = value;
	}

}
