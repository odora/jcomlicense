package cn.simple.jcom.license;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 通用工具类
 * 
 * @author xu.jun.fan@gmail.com
 * 
 */
public class Objects {
	/**
	 * 弹出错误框
	 * 
	 * @param root
	 * @param text
	 * @param title
	 */
	public static void errorBox(Component root, String text, Object... params) {
		String msg = params == null || params.length == 0 ? text : String.format(text, params);
		JOptionPane.showMessageDialog(root, msg, "错误信息", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 弹出信息框
	 * 
	 * @param root
	 * @param text
	 * @param title
	 */
	public static void infoBox(Component root, String text, Object... params) {
		String msg = params == null || params.length == 0 ? text : String.format(text, params);
		JOptionPane.showMessageDialog(root, msg, "提示信息", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 选择文件夹
	 */
	public static String choosePath() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		if (file != null) {
			System.out.println(file.getAbsolutePath());
			return file.getAbsolutePath();
		}
		return null;
	}

	/**
	 * 读取文本文件内容
	 * 
	 * @param file
	 * @return
	 */
	public static String readFile(File file) {
		FileInputStream fin = null;
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			Charset utf8 = Charset.forName("UTF-8");
			fin = new FileInputStream(file);
			int length = (int) file.length();
			byte[] bytes = new byte[length];
			int read = fin.read(bytes);
			if (read == length) {
				return new String(bytes, utf8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fin.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static Object[] listLicType() {
		return new Object[] { "试用版", "正式版" };
	}

	public static void writeFile(File file, String text) {
		FileOutputStream fos = null;
		try {
			byte[] bytes = text.getBytes("UTF-8");
			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e1) {
				}
			}
		}
	}
}
