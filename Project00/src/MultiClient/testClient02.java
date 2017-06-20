package MultiClient;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class testClient02 extends JFrame implements ActionListener {
	// --------------- ����
	Container c;
	private JTextField input;
	DefaultListModel model;
	JComboBox device;
	JList list;

	// ---------------------------- ��ӹڽ��� ��� ��ġ�׸�
	String[] devicelist = { "null", "tv", "light", "airConditioner", "boiler", "fridge", "humidifier", "inductionRange",
			"microwaveRange", "rangeHood", "riceCooker" };

	// --------------- ���� ���
	Socket s = null;
	InputStream is;
	OutputStream os;
	byte[] b;
	String m = null, sm = null;
	BufferedWriter out;
	ArrayList<String> a = new ArrayList<String>();

	// --------------- �Է� ��ü
	Scanner in1 = new Scanner(System.in);

	// --------------- ������
	inputMessage im;

	// ------------------- ������
	public testClient02() {

		try {
			connection();
			im = new inputMessage(s);
			im.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setTitle("Client Ex");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		c = getContentPane();
		c.setLayout(null);

		list = new JList(new DefaultListModel());
		model = (DefaultListModel) list.getModel();
		JScrollPane scrollPane_1 = new JScrollPane(list);
		scrollPane_1.setSize(540, 429);
		scrollPane_1.setLocation(12, 10);
		getContentPane().add(scrollPane_1, "Center");
		list.setBounds(12, 10, 540, 429);

		// ---------------------------- ��ġ ����Ʈ
		device = new JComboBox(devicelist);
		device.setBounds(12, 449, 115, 52);
		getContentPane().add(device);

		// ---------------------------- ��ġ ���� Ȯ�� ��ư
		JButton stateBtn = new JButton("state");
		stateBtn.setBounds(139, 449, 65, 52);
		getContentPane().add(stateBtn);
		stateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.clear();
				sm = "state";
				outputMessage();

				for (int i = 0; i < a.size(); i++) {
					model.addElement(a.get(i));
				}
				a.clear();
			}
		});

		// ---------------------------- �Է� �ؽ�Ʈ�ʵ�
		input = new JTextField();
		input.setBounds(216, 450, 256, 52);
		getContentPane().add(input);
		input.setColumns(10);
		input.addActionListener(this);

		// ---------------------------- ����Ʈ Ȯ�� ��ư
		JButton listBtn = new JButton("listUp");
		listBtn.setBounds(484, 449, 68, 52);
		getContentPane().add(listBtn);
		listBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.clear();
				sm = "list";
				// om.start();
				outputMessage();
				for (int i = 0; i < a.size(); i++) {
					model.addElement(a.get(i));
				}
				a.clear();
			}
		});

		this.setSize(580, 550);
		this.setVisible(true);
	}

	// --------------------- ���� ���� �� �߻��ϴ� �̺�Ʈ	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == input) {
			model.clear();
			int Index = device.getSelectedIndex();
			String deviceName = device.getItemAt(Index).toString();
			String outputMessage = "";

			if (deviceName.equals("null")) {
				model.addElement("error"); // �ƹ��͵� �Է� ���� ���� ĥ ��� error ���� ���
			} else {
				outputMessage = deviceName + " " + input.getText();
				sm = outputMessage;
				
				outputMessage();
				
				model.addElement(m); // ���� ���� ������ ȭ�鿡 ���
				
				input.setText(null);
			}
		}

	}

	// --------------------- ����
	private void connection() throws IOException {
		s = new Socket();
		System.out.println("���� ����");
		System.out.println("���� ��û");
		s.connect(new InetSocketAddress("localhost", 6001));
		System.out.println("���� ����");
	}

	// ------------ �������� ���� �޽��� by ������
	class inputMessage extends Thread {
		private Socket s;

		public inputMessage(Socket s) {
			this.s = s;
		}

		public void run() {
			try {
				is = s.getInputStream();

				while (true) {
					b = new byte[1024];
					int readCount = is.read(b);
					m = new String(b, 0, readCount, "EUC-KR");

					System.out.println("m : " + m);
					System.out.println("�ޱ� ����");
					
					if (readCount == 0) {
						System.out.println("not data!");
						break;
					}

					a.add(m); // ���� ���� �����͸� arraylist �� ����

					System.out.println("m : " + m);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	// ------------ �������� ������ ������ �޼ҵ�
	private void outputMessage() {
		try {
			out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			String ss = "client> " + sm;
			out.write(ss);
			out.flush();

			System.out.println("������ ����");
			model.addElement(ss); // �������� �����͸� ȭ�鿡 ���

			input.setText(null); // �ؽ�Ʈ�ʵ� ���

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		testClient02 c0 = new testClient02();
	}
}
