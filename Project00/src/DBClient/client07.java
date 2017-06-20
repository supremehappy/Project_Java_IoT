package DBClient;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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

public class client07 extends JFrame implements ActionListener{
	
	//--------------- 스윙
	Container c;
	private JTextField input;
	DefaultListModel model;
	JComboBox device;
	JList list;
	
	//---------------------------- 드롭박스에 담긴 장치항목
	String[] devicelist = {"null","tv","light","airConditioner","boiler","fridge","humidifier","inductionRange","microwaveRange","rangeHood","riceCooker"}; 
	
	//--------------- 소켓 통신
	BufferedReader in = null;
	BufferedWriter out = null;
	Socket s=null;
	InputStream is;
	OutputStream os;
	byte[] b=null;
	String m=null, sm=null;
	ArrayList<String> a = new ArrayList<String>();
	
	//--------------- 입력 객체
	Scanner in1 = new Scanner(System.in);
	
	//--------------- 스레드
	InputMessage iM;
	
	//------------------- 생성자
	public client07(){

		iM= new InputMessage();
		Thread t = new Thread(iM);
		
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
		
		//---------------------------- 장치 리스트
		device = new JComboBox(devicelist);
		device.setBounds(12, 449, 115, 52);
		getContentPane().add(device);
		
		//---------------------------- 장치 상태 확인 버튼
		JButton stateBtn = new JButton("state");
		stateBtn.setBounds(139, 449, 65, 52);
		getContentPane().add(stateBtn);
		stateBtn.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.clear();
				sm = "state";
				outputMessage();
				for(int i = 0; i<a.size();i++){
					model.addElement(a.get(i));
				}
				a.clear();
			}			
		});
		
		//---------------------------- 입력 텍스트필드
		input = new JTextField();
		input.setBounds(216, 450, 256, 52);
		getContentPane().add(input);
		input.setColumns(10);
		input.addActionListener(this);
		
		//---------------------------- 리스트 확인 버튼
		JButton listBtn = new JButton("listUp");
		listBtn.setBounds(484, 449, 68, 52);
		getContentPane().add(listBtn);
		listBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				model.clear();
				sm="list";
				outputMessage();
				for(int i = 0; i<a.size();i++){
					model.addElement(a.get(i));
				}
				a.clear();
			}			
		});
		
		this.setSize(580, 550);
		this.setVisible(true);
		
		try{
			connection();			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		t.start();		
	}
	
	//------------------- 엔터 입력시 메시지 송신
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == input) {
			model.clear();
			int Index = device.getSelectedIndex();
			String deviceName = device.getItemAt(Index).toString();
			String outputMessage = "";

			if (deviceName.equals("null")) {
				model.addElement("error");
			} else {
				outputMessage = deviceName + " " + input.getText();
				sm = outputMessage;
				outputMessage();
				//if(m.contains("on")||m.contains("off")){
					model.addElement(m);
				//}
				input.setText(null);
			}	
		}
		
	}
	
	//--------------------- 연결
	private void connection() throws IOException{
		s= new Socket();
		System.out.println("소켓 생성");
		System.out.println("연결 요청");
		s.connect(new InetSocketAddress("localhost",5002));
		System.out.println("연결 성공");
		
		is=s.getInputStream();
		out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	}
	
	//------------ 서버에서 받은 메시지 by 스레드
	class InputMessage implements Runnable{

		@Override
		public void run() {
			while(true){
				try{
					
					b= new byte[1024];
					int readCount = is.read(b);
					m=new String(b,0,readCount,"UTF-8");
					
					if(readCount<0){
						System.out.println("not data!");
						break;
					}
					
					a.add(m);
					
					System.out.println("m : "+m);
					System.out.println("받기 성공");
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	//------------- 서버에게 보내는 메시지
	private void outputMessage(){
		try{
			String ss = "client> "+sm;
			out.write(ss);
			out.flush();
			
			System.out.println("보내기 성공");
			model.addElement(ss);
			
			input.setText(null);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		
		client07 cl = new client07();
	}	
}
