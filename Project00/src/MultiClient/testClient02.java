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
	// --------------- 스윙
	Container c;
	private JTextField input;
	DefaultListModel model;
	JComboBox device;
	JList list;

	// ---------------------------- 드롭박스에 담긴 장치항목
	String[] devicelist = { "null", "tv", "light", "airConditioner", "boiler", "fridge", "humidifier", "inductionRange",
			"microwaveRange", "rangeHood", "riceCooker" };

	// --------------- 소켓 통신
	Socket s = null;
	InputStream is;
	OutputStream os;
	byte[] b;
	String m = null, sm = null;
	BufferedWriter out;
	ArrayList<String> a = new ArrayList<String>();

	// --------------- 입력 객체
	Scanner in1 = new Scanner(System.in);

	// --------------- 스레드
	inputMessage im;

	// ------------------- 생성자
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

		// ---------------------------- 장치 리스트
		device = new JComboBox(devicelist);
		device.setBounds(12, 449, 115, 52);
		getContentPane().add(device);

		// ---------------------------- 장치 상태 확인 버튼
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

		// ---------------------------- 입력 텍스트필드
		input = new JTextField();
		input.setBounds(216, 450, 256, 52);
		getContentPane().add(input);
		input.setColumns(10);
		input.addActionListener(this);

		// ---------------------------- 리스트 확인 버튼
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

	// --------------------- 엔터 누를 시 발생하는 이벤트	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == input) {
			model.clear();
			int Index = device.getSelectedIndex();
			String deviceName = device.getItemAt(Index).toString();
			String outputMessage = "";

			if (deviceName.equals("null")) {
				model.addElement("error"); // 아무것도 입력 없이 엔터 칠 경우 error 문구 출력
			} else {
				outputMessage = deviceName + " " + input.getText();
				sm = outputMessage;
				
				outputMessage();
				
				model.addElement(m); // 수신 받은 데이터 화면에 출력
				
				input.setText(null);
			}
		}

	}

	// --------------------- 연결
	private void connection() throws IOException {
		s = new Socket();
		System.out.println("소켓 생성");
		System.out.println("연결 요청");
		s.connect(new InetSocketAddress("localhost", 6001));
		System.out.println("연결 성공");
	}

	// ------------ 서버에서 받은 메시지 by 스레드
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
					System.out.println("받기 성공");
					
					if (readCount == 0) {
						System.out.println("not data!");
						break;
					}

					a.add(m); // 수신 받은 데이터를 arraylist 에 저장

					System.out.println("m : " + m);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	// ------------ 서버에게 데이터 보내는 메소드
	private void outputMessage() {
		try {
			out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			String ss = "client> " + sm;
			out.write(ss);
			out.flush();

			System.out.println("보내기 성공");
			model.addElement(ss); // 보내려는 데이터를 화면에 출력

			input.setText(null); // 텍스트필드 비움

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		testClient02 c0 = new testClient02();
	}
}
