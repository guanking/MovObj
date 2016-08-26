package mine;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileOperator {
	static final int SIZE = 1024;
	public static final String DATA_DIR = "E:/java/MovObj/workspace/MovObj/lab/status";
	public static final String OUTPUT_DIR = "lab/data";
	public static String readString(FileInputStream in) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		int len;
		byte buf[] = new byte[SIZE];
		try {
			while ((len = in.read(buf, 0, SIZE)) != -1) {
				sb.append(new String(buf, 0, len));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String getData(InputStream in) {
		InputStreamReader reader = new InputStreamReader(in);
		StringBuffer sb = new StringBuffer();
		char buf[] = new char[2014];
		int len;
		try {
			while ((len = reader.read(buf, 0, 10224)) != -1) {
				sb.append(buf, 0, len);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 向文件中写入内容
	 * 
	 * @param content
	 *            内容
	 * @param path
	 *            文件路径
	 */
	public static void writeToFile(String content, String path) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
}
