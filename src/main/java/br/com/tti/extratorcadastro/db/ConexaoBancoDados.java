package br.com.tti.extratorcadastro.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.tti.extratorcadastro.db.ConexaoBancoDados.TIPO_BANCO;
import br.com.tti.sefaz.systemconfig.schema.ArquivoConfiguracao.ConfiguracaoSistema;

public class ConexaoBancoDados {

	public static enum TIPO_BANCO {
		TTIREC, TTINFE
	};

	private String url;
	private String driver;
	private String usuario;
	private String senha;
	private Connection conx;
	private ConfiguracaoSistema conf;
	private TIPO_BANCO type;

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

	public ConexaoBancoDados(ConfiguracaoSistema conf) throws Exception {
		super();
		this.conf = conf;
		this.url = this.conf.getConf().getUrlBancoDados();
		this.driver = this.conf.getConf().getDriver();
		this.usuario = this.conf.getConf().getUsuario();
		this.senha = this.conf.getConf().getPassword();

		Class.forName(this.driver);
		this.conx = DriverManager.getConnection(this.url, this.usuario,
				this.senha);
	}

	public TIPO_BANCO getType() throws Exception {

		try {
			Statement stat = this.conx.createStatement();
			stat.executeQuery("SELECT COUNT(*) FROM XMLINFO");
			this.type = TIPO_BANCO.TTIREC;
			stat.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}

		try {
			Statement stat = this.conx.createStatement();
			stat.executeQuery("SELECT COUNT(*) FROM ESTADONFE");
			this.type = TIPO_BANCO.TTINFE;
			stat.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}

		return this.type;
	}

	public List<String> findXMLs(Date fromData) throws Exception {
		List<String> xmls = new ArrayList<String>();

		String sql = "";

		if (TIPO_BANCO.TTIREC.equals(this.getType())) {
			sql = "SELECT ID,XMLSTRING FROM XMLINFO WHERE DATARECEBIDA >=?";
		}

		if (TIPO_BANCO.TTINFE.equals(this.getType())) {
			sql = "SELECT ID,XMLSTRING FROM ESTADONFE WHERE DATAGERADA >=?";
		}

		PreparedStatement stat = this.conx.prepareStatement(sql);
		stat.setDate(1, new java.sql.Date(fromData.getTime()));
		ResultSet result = stat.executeQuery();

		while (result.next()) {
			String xmlstring = result.getString("XMLSTRING");
			xmls.add(xmlstring);
		}

		return xmls;
	}

}
