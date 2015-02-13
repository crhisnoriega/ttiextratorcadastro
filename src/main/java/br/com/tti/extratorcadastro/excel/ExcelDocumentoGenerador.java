package br.com.tti.extratorcadastro.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelDocumentoGenerador {

	protected Hashtable<String, String> headerdata;

	protected File fileout;

	private SimpleDateFormat sdf;

	private HSSFWorkbook workbook;
	private FileOutputStream out;
	private HSSFSheet sheet;
	private int counter;

	private String tmpFile;

	public ExcelDocumentoGenerador(String tmpFile,
			Hashtable<String, String> headerdata) {
		super();
		this.tmpFile = tmpFile;
		this.headerdata = headerdata;
		this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}

	public void fillEmitenteNFe(
			br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe.InfNFe.Emit emitente,
			CellStyle cs) {
		Row row = sheet.createRow(this.counter++);
		row.createCell(0).setCellValue(
				emitente.getCNPJ() != null ? emitente.getCNPJ() : emitente
						.getCPF());

	}

	public void createHeader() {

	}

	public File getFile() throws Exception {

		/*
		 * // String strFormula = "SUM(B1:B" + (infos.size() + 1) + ")";
		 * 
		 * Cell cellserienum = row.createCell(0);
		 * cellserienum.setCellValue("Somatorio do Valor das Notas");
		 * 
		 * Cell cell1 = row.createCell(1);
		 * cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		 * cell1.setCellFormula(strFormula);
		 */

		this.workbook.write(this.out);
		this.out.close();

		return this.fileout;
	}

	public void init() throws Exception {

		this.fileout = new File(this.tmpFile);
		this.out = new FileOutputStream(fileout);
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet();

		Row r = null;
		Cell c = null;
		// create 3 cell styles
		CellStyle cs = workbook.createCellStyle();
		CellStyle cs2 = workbook.createCellStyle();
		CellStyle cs3 = workbook.createCellStyle();
		DataFormat df = workbook.createDataFormat();

		// create 2 fonts objects
		Font f = workbook.createFont();
		Font f2 = workbook.createFont();

		// set font 1 to 12 point type
		f.setFontHeightInPoints((short) 12);
		// make it blue
		f.setColor((short) 0xc);
		// make it bold
		// arial is the default font
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// set font 2 to 10 point type
		f2.setFontHeightInPoints((short) 10);
		// make it red
		f2.setColor((short) Font.COLOR_RED);
		// make it bold
		f2.setBoldweight(Font.BOLDWEIGHT_BOLD);

		f2.setStrikeout(true);

		// set cell stlye
		cs.setFont(f);
		// set the cell format
		cs.setDataFormat(df.getFormat("#,##0.0"));

		// set a thin border
		// cs2.setBorderBottom(cs2.BORDER_THIN);
		// / fill w fg fill color
		// cs2.setFillPattern((short) CellStyle.SOLID_FOREGROUND);
		// set the cell format to text see DataFormat for a full list
		cs2.setDataFormat(HSSFDataFormat.getBuiltinFormat("MM/dd/yyyy"));

		// set the font
		// cs2.setFont(f2);

		// set the sheet name in Unicode
		this.workbook.setSheetName(0, "emitentes");

		this.sheet.setColumnWidth(0, 400 * 10);
		this.sheet.setColumnWidth(1, 400 * 10);
		this.sheet.setColumnWidth(2, 400 * 14);
		this.sheet.setColumnWidth(3, 400 * 14);
		this.sheet.setColumnWidth(4, 400 * 30);
		this.sheet.setColumnWidth(5, 400 * 14);
		this.sheet.setColumnWidth(6, 400 * 14);
		this.sheet.setColumnWidth(7, 400 * 34);

		Row header = this.sheet.createRow(this.counter++);
		header.createCell(0).setCellValue("\"cnpj\"");
		header.createCell(1).setCellValue("\"\"");
		header.createCell(2).setCellValue("\"docnum\"");
		header.createCell(3).setCellValue("\"num_nf\"");
		header.createCell(4).setCellValue("\"serie\"");
		header.createCell(5).setCellValue("\"cgc_cpf\"");
		header.createCell(6).setCellValue("\"tota_da_nota\"");
		header.createCell(7).setCellValue("\"chave\"");
		header.createCell(8).setCellValue("\"destinatario\"");

	}

	public File generatePDF() {
		return this.fileout;
	}

	public static void main(String[] args) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					"C:\\teste.xls"));
			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator rows = sheet.rowIterator();
			boolean first = true;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				if (first) {
					row.createCell(16).setCellValue("Validação CT-e");
					first = false;
					continue;
				}

				Iterator cells = row.cellIterator();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();
					System.out.println(cell.toString());

				}

				row.createCell(16).setCellValue("VALIDADA");
			}

			workbook.write(new FileOutputStream(new File("C:\\teste2.xls")));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
