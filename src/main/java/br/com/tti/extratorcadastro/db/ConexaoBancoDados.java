package br.com.tti.extratorcadastro.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConexaoBancoDados {

	private String url;
	private String driver;
	private String usuario;
	private String senha;
	private Connection conx;

	public ConexaoBancoDados(String url, String driver, String usuario,
			String senha) throws Exception {
		super();
		this.url = url;
		this.driver = driver;
		this.usuario = usuario;
		this.senha = senha;

		Class.forName(this.driver);
		this.conx = DriverManager.getConnection(this.url, this.usuario,
				this.senha);
	}

	public List<String> findXMLs(Date fromData) throws Exception {
		List<String> xmls = new ArrayList<String>();

		String sql = "SELECT * FROM XMLINFO";
		Statement stat = this.conx.createStatement();
		ResultSet result = stat.executeQuery(sql);

		while (result.next()) {
			String xmlstring = result.getString("XMLSTRING");
			xmls.add(xmlstring);
		}

		return xmls;
	}

}
