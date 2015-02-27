package br.com.tti.extratorcadastro.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
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

import br.com.tti.extratorcadastro.ExtratorCadastro;
import br.com.tti.extratorcadastro.excel.ExcelDocumentoGenerador.ObjectMethodPair;
import br.com.tti.extratorcadastro.xml.esquemas.cte.TEndeEmi;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TEnderEmi;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TEndereco;
import br.com.tti.extratorcadastro.xml.esquemas.nfe.TNFe.InfNFe.Emit;

public class ExcelDocumentoGenerador {

	protected Hashtable<String, String> headerdata;

	protected File fileout;

	// private SimpleDateFormat sdf;

	private HSSFWorkbook workbook;
	private FileOutputStream out;
	private HSSFSheet sheet;
	private int counter;

	private String tmpFile;

	private String[] keys = new String[] { "CNPJ", "CPF", "XNome", "XFant",
			"IE", "CEP", "XLgr", "Nro", "XBairro", "XMun", "CMun", "XCpl",
			"Fone", "CPais", "XPais", "email" };

	public ExcelDocumentoGenerador(String tmpFile,
			Hashtable<String, String> headerdata) {
		super();
		this.tmpFile = tmpFile;
		this.headerdata = headerdata;
		// this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}

	// private boolean hasHeader = false;

	public void fill(Object info) {

		Hashtable<String, ObjectMethodPair> vv = extractAttributes(info);

		/*
		 * if (!this.hasHeader) { this.createHeader(); this.hasHeader = true; }
		 */

		if (!vv.isEmpty()) {
			this.createRow(vv, info.getClass());
		}

	}

	private void createHeader() {
		Row header = this.sheet.createRow(this.counter++);

		int counter = 0;
		for (String key : this.keys) {
			header.createCell(counter++).setCellValue("\"" + key + "\"");
		}

	}

	private void createRow(Hashtable<String, ObjectMethodPair> vv,
			Class<? extends Object> class1) {
		Row header = this.sheet.createRow(this.counter++);

		int counter = 0;

		for (String key : this.keys) {
			ObjectMethodPair pair = vv.get(key);

			if (pair == null) {
				header.createCell(counter++).setCellValue("");
				continue;
			}

			Object value = "NADA";
			try {
				value = pair.getMethod().invoke(pair.getObj(), new Object[] {});
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (value == null) {
				value = "";
			}
			header.createCell(counter++).setCellValue(value + "");
		}

		header.createCell(counter++).setCellValue(class1.getSimpleName());

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

		this.sheet.setColumnWidth(0, 400 * 14);
		this.sheet.setColumnWidth(1, 400 * 7);
		this.sheet.setColumnWidth(2, 400 * 14);
		this.sheet.setColumnWidth(3, 400 * 14);
		this.sheet.setColumnWidth(4, 400 * 14);
		this.sheet.setColumnWidth(5, 400 * 14);
		this.sheet.setColumnWidth(6, 400 * 14);
		this.sheet.setColumnWidth(7, 400 * 14);
		this.sheet.setColumnWidth(8, 400 * 14);
		this.sheet.setColumnWidth(9, 400 * 14);
		this.sheet.setColumnWidth(10, 400 * 14);
		this.sheet.setColumnWidth(11, 400 * 14);
		this.sheet.setColumnWidth(12, 400 * 14);

		this.createHeader();
	}

	public File generatePDF() {
		return this.fileout;
	}

	public static class ObjectMethodPair {
		private Object obj;
		private Method method;

		public Object getObj() {
			return obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

	}

	public static Hashtable<String, ObjectMethodPair> extractAttributes(
			Object obj) {
		Hashtable<String, ObjectMethodPair> att = new Hashtable<String, ObjectMethodPair>();
		if (obj == null) {
			return att;
		}

		Method[] methods = obj.getClass().getDeclaredMethods();

		for (Method method : methods) {
			if (method.getName().startsWith("get")
					&& method.getParameterTypes().length == 0) {
				if (method.getReturnType().equals(String.class)
						|| method.getReturnType().equals(Integer.class)) {
					ObjectMethodPair methodPair = new ObjectMethodPair();
					methodPair.setMethod(method);
					methodPair.setObj(obj);
					att.put(method.getName().replace("get", ""), methodPair);
				} else {

					try {
						method.setAccessible(true);
						Object obj1 = method.invoke(obj, new Object[] {});

						if (obj1 != null) {
							Hashtable<String, ObjectMethodPair> mm = extractAttributes(obj1);
							att.putAll(mm);

						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}

		return att;
	}

	public static void main(String[] args) {

		Emit ee = new Emit();
		ee.setEnderEmit(new TEnderEmi());
		/*
		 * Vector<ObjectMethodPair> mm = ExcelDocumentoGenerador
		 * .extractAttributes(ee); for (ObjectMethodPair methodpair : mm) {
		 * System.out.println(methodpair.getMethod().getName() + " -> " +
		 * methodpair.getObj()); } System.out.println();
		 */
	}

	public static void main2(String[] args) {
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
