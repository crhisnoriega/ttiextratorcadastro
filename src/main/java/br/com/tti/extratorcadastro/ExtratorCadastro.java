package br.com.tti.extratorcadastro;

import br.com.tti.nfev200.TNFe;

import br.com.tti.nfev200.TNFe.InfNFe;
import br.com.tti.tools.XMLGenerator;

public class ExtratorCadastro {

	public static void main(String[] args) {
		TNFe t1 = new TNFe();
      t1.setInfNFe(new InfNFe());
      t1.getInfNFe().setId("MyID");
     
      try{
		   XMLGenerator gen = new XMLGenerator("br.com.tti.nfev200");
         String xml1 = gen.toXMLString(t1);
      	System.out.println("xml: " + gen.toXMLString(t1));
         javax.xml.bind.JAXBElement<TNFe> t2 = (javax.xml.bind.JAXBElement<TNFe>) gen.toObject(xml1);
         System.out.println(t2.getValue().getInfNFe().getId());
      }catch (Exception e) {
    		e.printStackTrace();
      }
    }
}
