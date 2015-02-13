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
import java.util.logging.Level;

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

import br.com.tti.extratorcadastro.XMLInfo;

public class ExcelDocumentoGenerador {

	protected Vector<Object> infos;

	protected Hashtable<String, String> headerdata;

	protected File fileout;

	private SimpleDateFormat sdf;

	private boolean islogs;

	public ExcelDocumentoGenerador(Hashtable<String, String> headerdata,
			Vector<Object> infos) {
		super();
		this.infos = infos;
		this.headerdata = headerdata;

		this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}

	private void fillNFeAbbottRow(int rownum, Row row, XMLInfo info,
			CellStyle cs) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

		Cell cd = row.createCell(0);
		cd.setCellValue(sdf.format(info.getDT_EMISSAO()));

		Cell cd1 = row.createCell(1);
		cd1.setCellValue(sdf.format(info.getDT_ENTRADA()));

		row.createCell(2).setCellValue(info.getDOCNUM());
		row.createCell(3).setCellValue(info.getNUM_NF());
		row.createCell(4).setCellValue(info.getSERIE());

		row.createCell(5).setCellValue(info.getCGC_CPF());
		row.createCell(6).setCellValue(info.getTOTAL_DA_NOTA());

		Cell celldest = row.createCell(7);
		celldest.setCellValue(info.getChave());

		Cell cellnum = row.createCell(8);
		cellnum.setCellValue(info.getCnpjdest());
	}

	public void create() {
		try {
			fileout = new File(System.getProperty("user.home")
					+ "/workbook.xls");
			FileOutputStream out = new FileOutputStream(fileout);

			Workbook wb = new HSSFWorkbook();
			// create a new sheet
			Sheet s = wb.createSheet();

			Row r = null;
			Cell c = null;
			// create 3 cell styles
			CellStyle cs = wb.createCellStyle();
			CellStyle cs2 = wb.createCellStyle();
			CellStyle cs3 = wb.createCellStyle();
			DataFormat df = wb.createDataFormat();

			// create 2 fonts objects
			Font f = wb.createFont();
			Font f2 = wb.createFont();

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
			wb.setSheetName(0, "testes");

			s.setColumnWidth(0, 400 * 10);
			s.setColumnWidth(1, 400 * 10);
			s.setColumnWidth(2, 400 * 14);
			s.setColumnWidth(3, 400 * 14);
			s.setColumnWidth(4, 400 * 30);
			s.setColumnWidth(5, 400 * 14);
			s.setColumnWidth(6, 400 * 14);
			s.setColumnWidth(7, 400 * 34);

			boolean isheader = true;

			for (int rownum = 0; rownum < this.infos.size(); rownum++) {
				Row row = s.createRow((short) (rownum + 1));
				Object info = this.infos.get(rownum);
				if (info instanceof XMLInfo) {
					if (isheader) {
						Row header = s.createRow(0);
						header.createCell(0).setCellValue("\"dt_emissao\"");
						header.createCell(1).setCellValue("\"dt_entrada\"");
						header.createCell(2).setCellValue("\"docnum\"");
						header.createCell(3).setCellValue("\"num_nf\"");
						header.createCell(4).setCellValue("\"serie\"");
						header.createCell(5).setCellValue("\"cgc_cpf\"");
						header.createCell(6).setCellValue("\"tota_da_nota\"");
						header.createCell(7).setCellValue("\"chave\"");
						header.createCell(8).setCellValue("\"destinatario\"");

						isheader = false;
					}

					XMLInfo nfinfo = (XMLInfo) info;
					this.fillNFeAbbottRow(rownum, row, nfinfo, cs2);
				}
			}

			Row row = s.createRow(infos.size() + 1);
			String strFormula = "SUM(B1:B" + (infos.size() + 1) + ")";

			Cell cellserienum = row.createCell(0);
			cellserienum.setCellValue("Somatorio do Valor das Notas");

			Cell cell1 = row.createCell(1);
			cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell1.setCellFormula(strFormula);

			wb.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
