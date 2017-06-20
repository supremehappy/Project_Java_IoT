package MultiClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class view02 extends JFrame{

	//--------------- 스윙
	Container c;
	JLabel imageLabel = new JLabel();
	JLabel serverM = new JLabel("대기중");
	ImageIcon icon;
	private final JButton connbtn = new JButton("연결");
	JComboBox device;
	
	//--------------- 소켓 통신
	Socket s;
	InputStream is;
	OutputStream os;
	byte[] b=null;
	String m=null, sm="확인";
	
	//--------------- DB
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	String driver= "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "hr", pw="1234";
	
	String[] devicelist = {"null","tv","light","airConditioner","boiler","fridge","humidifier","inductionRange","microwaveRange","rangeHood","riceCooker"};
	
	//--------------- 생성자
	view02(){

		this.setTitle("view Ex");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		
		device = new JComboBox(devicelist);
		panel.add(device);
		
		//--------------- 연결 버튼 이벤트 처리
		panel.add(connbtn);
		connbtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					connection();
					outputMessage();
					InputMessage();
				}catch(Exception a){
					a.printStackTrace();
				}
			}
		});
		
		panel.add(serverM);
		
		c.add(panel, BorderLayout.NORTH);
		c.add(imageLabel, BorderLayout.CENTER);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.setSize(580, 550);
		this.setVisible(true);
		
	}
	
	//--------------------- 연결 메소드
	private void connection() throws IOException{
		s= new Socket();
		System.out.println("소켓 생성");
		System.out.println("연결 요청");
		s.connect(new InetSocketAddress("localhost",6001));
		System.out.println("연결 성공");
		
		is=s.getInputStream();
		os=s.getOutputStream();
	}
	
	//------------- 서버에서 받은 메시지
	private void InputMessage(){
		try{
			
			b= new byte[1024];
			int readCount = is.read(b);
			
			if(readCount<0){
				System.out.println("not data!");
			}else{
				m=new String(b,0,readCount,"UTF-8");
				imageoutput();
			}
			
			System.out.println("m : "+m);
			System.out.println("받기 성공");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//------------- 서버에게 보내는 메시지
	private void outputMessage(){
		
		try{
			
			int Index = device.getSelectedIndex();
			String deviceName = device.getItemAt(Index).toString();
			String outputMessage = "";

			if (deviceName.equals("null")) {
			
				String error="error";
				serverM.setText(error);
				
			} else {
				outputMessage = "view> "+deviceName;
				sm = outputMessage;
			}
			
			b=new byte[100];
			b=sm.getBytes("UTF-8");
			os.write(b);
			os.flush();
			
			System.out.println("sm : "+sm);
			System.out.println("보내기 성공");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//------------- 이미지 출력 메소드
	public void imageoutput(){
		String[] split = m.split(" ");
		
		if(split[2].contains("tv")){			
			if(split[4].contains("on")){
				icon = new ImageIcon("image/04_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/04_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("light")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/07_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/07_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}
			
		}else if(split[2].contains("airConditioner")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/03_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/03_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("boiler")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/08_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/08_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("fridge")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/01_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/01_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("humidifier")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/09_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/09_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("inductionRange")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/06_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/06_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("microwaveRange")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/00_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/00_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("rangeHood")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/02_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/02_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else if(split[2].contains("riceCooker")){
			if(split[4].contains("on")){
				icon = new ImageIcon("image/05_0.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);				
			}else if(split[4].contains("off")){
				icon = new ImageIcon("image/05_1.png");
				imageLabel.setIcon(icon);
				serverM.setText(m);
			}

		}else{
			serverM.setText("error");
			return;
		}
	}
	
	public static void main(String[] args){
		view02 v = new 	view02();
	}
}
