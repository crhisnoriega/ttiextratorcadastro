package br.com.tti.tools;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class ReadFile {

	public static String readFile(String path) throws Exception {
		FileInputStream f = new FileInputStream(path);

		DataInputStream dis = new DataInputStream(f);
		String line = dis.readLine();
		String all = new String();

		while (line != null) {
			all += line;
			line = dis.readLine();
		}

		f.close();
		return all;
	}

	public static String readFile(String path, String encoding)
			throws Exception {
		File fileDirs = new File(path);

		InputStreamReader is = new InputStreamReader(new FileInputStream(
				fileDirs), encoding);

		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		String str = s.hasNext() ? s.next() : "";
		is.close();
		return str;
	}

	public static String convert(byte[] bytess, String source_encoding,
			String target_encoding) throws Exception {

		Charset charsetInput = Charset.forName(source_encoding);
		CharsetDecoder decoder = charsetInput.newDecoder();
		CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(bytess));

		Charset charsetOutput = Charset.forName(target_encoding);
		CharsetEncoder encoder = charsetOutput.newEncoder();
		ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(cbuf));

		String xmlutf = new String(bbuf.array(), 0, bbuf.limit(), charsetOutput);

		return xmlutf;
	}

	public static void writeFile(String filename, char[] cbuf, String encoding)
			throws Exception {
		FileOutputStream fos = new FileOutputStream(filename);
		OutputStreamWriter out = new OutputStreamWriter(fos, encoding);
		out.write(cbuf);
		out.close();

	}
}
