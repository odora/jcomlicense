package cn.simple.jcom.license;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
/*
 * Created by JFormDesigner on Sun Aug 04 23:16:21 CST 2019
 */


/**
 * @author CodeCracker
 */
@SuppressWarnings({ "serial" })
public class LicenseFrame extends JFrame {
	public LicenseFrame() {
		initComponents();
		initActions();

		// ----------------------------------------
		// 窗体自己处理关闭事件处理串口的关闭
		// ----------------------------------------
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	}

	/**
	 * 生成证书前检查下输入项
	 * 
	 * @return
	 */
	private boolean checkInput() {
		// 检查保存时间
		String val = jCtlSubject.getText();
		if (val.isEmpty()) {
			Objects.errorBox(this, "请输入【证书名称】");
			return false;
		}

		val = jCtlStart.getText();
		if (val.isEmpty()) {
			Objects.errorBox(this, "请输入【有效开始日】");
			return false;
		} else if (!val.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			Objects.errorBox(this, "【有效开始日】必须为日期yyyy-MM-dd");
			return false;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				sdf.parse(val);
			} catch (Exception e) {
				e.printStackTrace();
				Objects.errorBox(this, "【有效开始日】必须为日期yyyy-MM-dd");
				return false;
			}
		}

		val = jCtlEnd.getText();
		if (val.isEmpty()) {
			Objects.errorBox(this, "请输入【有效结束日】");
			return false;
		} else if (!val.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			Objects.errorBox(this, "【有效结束日】必须为日期yyyy-MM-dd");
			return false;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				sdf.parse(val);
			} catch (Exception e) {
				e.printStackTrace();
				Objects.errorBox(this, "【有效结束日】必须为日期yyyy-MM-dd");
				return false;
			}
		}

		val = jCtlPath.getText();
		if (val.isEmpty()) {
			Objects.errorBox(this, "请选择【保存路径】");
			return false;
		}

		return true;
	}

	private void initConfig() {
		ComboBoxModel model = jCtlType.getModel();
		model.setSelectedItem(model.getElementAt(0));

		// 开始结束日期默认是从今天起3天
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		jCtlStart.setText(sdf.format(cal.getTime()));
		cal.add(Calendar.DAY_OF_MONTH, 3);
		jCtlEnd.setText(sdf.format(cal.getTime()));

		// 硬件信息初始为本机硬件信息
		String value = null;
		try {
			value = HWUtils.getCPUSerial();
			if (value != null) {
				jCtlCpuSerial.setText(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			java.util.List<String> addrs = HWUtils.getMacAddress();
			if (addrs != null && !addrs.isEmpty()) {
				jCtlMacAddr.setText(addrs.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			value = HWUtils.getMainBoardSerial();
			if (value != null) {
				jCtlMainBoard.setText(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成证书
	 */
	private void saveLicense() {
		if (!checkInput()) {
			return;
		}

		License obj = new License();
		obj.setSubject(jCtlSubject.getText());
		obj.setConsumerType(jCtlType.getSelectedItem().toString());
		obj.setIssuedTime(jCtlStart.getText());
		obj.setExpiryTime(jCtlEnd.getText());

		String val = jCtlCpuSerial.getText();
		if (val != null && !val.trim().isEmpty()) {
			obj.setCpuSerial(val.trim());
		}

		val = jCtlMacAddr.getText();
		if (val != null && !val.trim().isEmpty()) {
			obj.setMacAddress(val.trim());
		}

		val = jCtlMainBoard.getText();
		if (val != null && !val.trim().isEmpty()) {
			obj.setMainBoardSerial(val.trim());
		}

		// 构造json串
		String res = new Gson().toJson(obj);
		System.out.println(res);

		// 加密JSON串
		String code = AESUtils.encrypt(res);
		obj.setEncryptCode(code);
		obj.setCpuSerial(null);
		obj.setMacAddress(null);
		obj.setMainBoardSerial(null);

		// 生成证书文件
		String path = jCtlPath.getText();
		res = new Gson().toJson(obj);
		File file = new File(path, "jcomassist-license.dat");
		Objects.writeFile(file, res);
	}

	private void initActions() {
		// 界面控件的初始化数据
		initConfig();
		jCtlPath.setEditable(false);

		// 事件处理
		jBtnPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = Objects.choosePath();
				if (path != null) {
					jCtlPath.setText(path);
				}
			}
		});

		jBtnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initConfig();
			}
		});

		jBtnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveLicense();
			}
		});

		jBtnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		panel1 = new JPanel();
		label1 = new JLabel();
		jCtlSubject = new JTextField();
		label2 = new JLabel();
		jCtlStart = new JTextField();
		label3 = new JLabel();
		jCtlEnd = new JTextField();
		label4 = new JLabel();
		jCtlType = new JComboBox(Objects.listLicType());
		separator1 = compFactory.createSeparator("硬件信息");
		label5 = new JLabel();
		jCtlCpuSerial = new JTextField();
		label6 = new JLabel();
		jCtlMacAddr = new JTextField();
		label7 = new JLabel();
		jCtlMainBoard = new JTextField();
		separator2 = compFactory.createSeparator("证书保存路径");
		jBtnPath = new JButton();
		jCtlPath = new JTextField();
		separator3 = compFactory.createSeparator("");
		panel2 = new JPanel();
		jBtnReset = new JButton();
		jBtnSave = new JButton();
		jBtnClose = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				"$lcgap, default, $lcgap, default:grow, $lcgap",
				"11*($lgap, default), $lgap"));

			//---- label1 ----
			label1.setText("证书名称");
			panel1.add(label1, CC.xy(2, 2));
			panel1.add(jCtlSubject, CC.xy(4, 2));

			//---- label2 ----
			label2.setText("有效开始日");
			panel1.add(label2, CC.xy(2, 4));
			panel1.add(jCtlStart, CC.xy(4, 4));

			//---- label3 ----
			label3.setText("有效结束日");
			panel1.add(label3, CC.xy(2, 6));
			panel1.add(jCtlEnd, CC.xy(4, 6));

			//---- label4 ----
			label4.setText("证书类型");
			panel1.add(label4, CC.xy(2, 8));
			panel1.add(jCtlType, CC.xy(4, 8));
			panel1.add(separator1, CC.xywh(2, 10, 3, 1));

			//---- label5 ----
			label5.setText("CPU序列号");
			panel1.add(label5, CC.xy(2, 12));
			panel1.add(jCtlCpuSerial, CC.xy(4, 12));

			//---- label6 ----
			label6.setText("MAC地址");
			panel1.add(label6, CC.xy(2, 14));
			panel1.add(jCtlMacAddr, CC.xy(4, 14));

			//---- label7 ----
			label7.setText("主板序列号");
			panel1.add(label7, CC.xy(2, 16));
			panel1.add(jCtlMainBoard, CC.xy(4, 16));
			panel1.add(separator2, CC.xywh(2, 18, 3, 1));

			//---- jBtnPath ----
			jBtnPath.setText("选择路径");
			panel1.add(jBtnPath, CC.xy(2, 20));
			panel1.add(jCtlPath, CC.xy(4, 20));
			panel1.add(separator3, CC.xywh(2, 22, 3, 1));
		}
		contentPane.add(panel1, BorderLayout.CENTER);

		//======== panel2 ========
		{
			panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

			//---- jBtnReset ----
			jBtnReset.setText("重置");
			panel2.add(jBtnReset);

			//---- jBtnSave ----
			jBtnSave.setText("生成");
			panel2.add(jBtnSave);

			//---- jBtnClose ----
			jBtnClose.setText("关闭");
			panel2.add(jBtnClose);
		}
		contentPane.add(panel2, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JLabel label1;
	private JTextField jCtlSubject;
	private JLabel label2;
	private JTextField jCtlStart;
	private JLabel label3;
	private JTextField jCtlEnd;
	private JLabel label4;
	private JComboBox jCtlType;
	private JComponent separator1;
	private JLabel label5;
	private JTextField jCtlCpuSerial;
	private JLabel label6;
	private JTextField jCtlMacAddr;
	private JLabel label7;
	private JTextField jCtlMainBoard;
	private JComponent separator2;
	private JButton jBtnPath;
	private JTextField jCtlPath;
	private JComponent separator3;
	private JPanel panel2;
	private JButton jBtnReset;
	private JButton jBtnSave;
	private JButton jBtnClose;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
