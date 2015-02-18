package br.com.tti.extratorcadastro.main;

import br.com.tti.extratorcadastro.ExtratorCadastro;
import br.com.tti.sefaz.systemconfig.XMLConfigSystem;
import br.com.tti.sefaz.systemconfig.schema.ArquivoConfiguracao.ConfiguracaoSistema;

public class Launch {

	public static void main(String[] args) {
		try {
			XMLConfigSystem confsys = new XMLConfigSystem(args[0]);
			ConfiguracaoSistema conf = confsys.getFileConfig()
					.getConfiguracaoSistema();
			ExtratorCadastro cad = new ExtratorCadastro(conf);
			cad.extract();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
