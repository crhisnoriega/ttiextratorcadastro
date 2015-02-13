package br.com.tti.extratorcadastro;

import java.util.Date;

public class XMLInfo {

	private Date DT_EMISSAO;
	private Date DT_ENTRADA;
	private String DOCNUM;
	private String NUM_NF;
	private String SERIE;
	private String CGC_CPF;
	private float TOTAL_DA_NOTA;
	private String cnpjdest;
	private String chave;

	public Date getDT_EMISSAO() {
		return DT_EMISSAO;
	}

	public void setDT_EMISSAO(Date dTEMISSAO) {
		DT_EMISSAO = dTEMISSAO;
	}

	public Date getDT_ENTRADA() {
		return DT_ENTRADA;
	}

	public void setDT_ENTRADA(Date dTENTRADA) {
		DT_ENTRADA = dTENTRADA;
	}

	public String getDOCNUM() {
		return DOCNUM;
	}

	public void setDOCNUM(String dOCNUM) {
		DOCNUM = dOCNUM;
	}

	public String getNUM_NF() {
		return NUM_NF;
	}

	public void setNUM_NF(String nUMNF) {
		NUM_NF = nUMNF;
	}

	public String getSERIE() {
		return SERIE;
	}

	public void setSERIE(String sERIE) {
		SERIE = sERIE;
	}

	public String getCGC_CPF() {
		return CGC_CPF;
	}

	public void setCGC_CPF(String cGCCPF) {
		CGC_CPF = cGCCPF;
	}

	public float getTOTAL_DA_NOTA() {
		return TOTAL_DA_NOTA;
	}

	public void setTOTAL_DA_NOTA(float tOTALDANOTA) {
		TOTAL_DA_NOTA = tOTALDANOTA;
	}

	public void setChave(String string) {
		chave = string;

	}

	public void setCnpjdest(String string) {
		cnpjdest = string;
	}

	public String getCnpjdest() {
		return cnpjdest;
	}

	public String getChave() {
		return chave;
	}

}
