package br.com.tti.extratorcadastro.main;

import java.util.ArrayList;
import java.util.List;

import br.com.tti.extratorcadastro.ExtratorCadastro;
import br.com.tti.sefaz.systemconfig.XMLConfigSystem;
import br.com.tti.sefaz.systemconfig.schema.ArquivoConfiguracao.ConfiguracaoSistema;
import br.com.tti.sefaz.systemconfig.schema.Cnpjinfo;
import br.com.tti.sefaz.systemconfig.schema.Cnpjs;

public class Launch {

	public static void main(String[] args) {
		try {
			XMLConfigSystem confsys = new XMLConfigSystem(args[0]);
			ConfiguracaoSistema conf = confsys.getFileConfig()
					.getConfiguracaoSistema();

			List<String> cnpjsv = new ArrayList<String>();
			List<Cnpjs> cnpjs = confsys.getFileConfig().getCnpjsCadastrados()
					.getCadastro();
			for (Cnpjs cnpj : cnpjs) {
				List<Cnpjinfo> infos = cnpj.getCnpjInfo();
				for (Cnpjinfo info : infos) {
					cnpjsv.add(info.getCnpj());
				}
			}

			Long maxl = 65000L;

			try {
				maxl = Long.parseLong(args[2]);
			} catch (Exception e) {
				// TODO: handle exception
			}

			ExtratorCadastro cad = new ExtratorCadastro(conf, cnpjsv, args[1],
					maxl);
			cad.extract();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
