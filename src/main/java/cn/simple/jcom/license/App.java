package cn.simple.jcom.license;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * 串口接收工具
 */
public class App {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		LicenseFrame mainFrame = new LicenseFrame();
		mainFrame.setTitle("通用序列号生成器");
		mainFrame.setSize(600, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
}
