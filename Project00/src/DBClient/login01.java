package DBClient;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class login01 extends JFrame{

	//--------------- 스윙
	Container c;
	private JTextField IdField;
	private JTextField PwField;
	
	//--------------- DB
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	String driver ="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "hr", pw = "1234";
	
	//--------------- 생성자
	public login01(){
		
		this.setTitle("login Ex");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c = getContentPane();
		c.setLayout(null);
		
		JLabel IdLabel = new JLabel("ID");
		IdLabel.setFont(new Font("HY센스L", Font.PLAIN, 27));
		IdLabel.setBounds(12, 10, 100, 30);
		getContentPane().add(IdLabel);
		
		JLabel PwLabel = new JLabel("PW");
		PwLabel.setFont(new Font("HY센스L", Font.PLAIN, 27));
		PwLabel.setBounds(12, 52, 100, 30);
		getContentPane().add(PwLabel);
		
		//--------------- ok 버튼 이벤트 처리(db에 있는 내용과 비교하여 중복되면 로그인)
		JButton Okbtn = new JButton("Ok");
		Okbtn.setBounds(54, 92, 80, 30);
		getContentPane().add(Okbtn);
		Okbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s1=null, s2=null;
				//-------------------------- 조건값 비교로 해당하는 오류 메시지 호출
				if(IdField.getText().isEmpty()==true){
					JOptionPane.showMessageDialog(null, "plz, input id","입력",JOptionPane.ERROR_MESSAGE);
					IdField.requestFocus();
				}else if(PwField.getText().isEmpty()==true){
					JOptionPane.showMessageDialog(null, "plz, input pw","입력",JOptionPane.ERROR_MESSAGE);
					PwField.requestFocus();
				}else{
					try{
						rs=stmt.executeQuery("select * from exmember");
					System.out.println("입력됨");
					while(rs.next()){
						String Did=rs.getString(1);
						String Dpw=rs.getString(2);
						
						System.out.println("입력됨1");
						System.out.println(Did + ",  " + Dpw );
						System.out.println("입력됨2");
						s1 = Did;
						s2 = Dpw;
					}
					
					if(!((s1.equals(IdField.getText()))&&(s2.equals(PwField.getText())))){
						JOptionPane.showMessageDialog(null, "id 또는 pw 가 다릅니다.","입력",JOptionPane.ERROR_MESSAGE);
						IdField.requestFocus();
					}else{						
						dispose(); // 현재 열린 창 닫기
						getContentPane().add(new client07()); 		//client05 생성차 호출(client 폼 불러오기)
					}
					}catch(Exception arg0){
						arg0.printStackTrace();
					}
				}
			}
		});		
		
		//--------------- join 버튼 이벤트 처리(회원가입)
		JButton Joinbtn = new JButton("Join");
		Joinbtn.setBounds(146, 92, 80, 30);
		getContentPane().add(Joinbtn);
		Joinbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().add(new join01());		//join00 생성자 호출(회원가입 폼 불러오기)
			}
		});
		
		IdField = new JTextField();
		IdField.setBounds(85, 10, 140, 30);
		getContentPane().add(IdField);
		IdField.setColumns(10);
		
		PwField = new JTextField();
		PwField.setColumns(10);
		PwField.setBounds(85, 51, 140, 30);
		getContentPane().add(PwField);
		
		this.setSize(250, 165);
		this.setVisible(true);
		
		
		//------------------- DB 연결
		try{
			DBconnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	//------------ DB 연결 메소드
	private void DBconnection() throws Exception{
		Class.forName(driver);
		
		conn=DriverManager.getConnection(url,id,pw);
		stmt = conn.createStatement();
		
		System.out.println("DB 연결 성공");
	}
	
	public static void main(String[] args){
		login01 l = new login01();
	}
}
