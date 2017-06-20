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

public class join01 extends JFrame{
	
	//--------------- 스윙
	Container c;
	private JTextField IdField;
	private JTextField PwField;
	private JTextField RePwField;
	
	//--------------- DB
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	String driver ="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "hr", pw = "1234";
	
	//--------------- 생성자
	join01(){
		
		this.setTitle("join Ex");
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
		
		JLabel RePwLabel = new JLabel("Re PW");
		RePwLabel.setFont(new Font("HY센스L", Font.PLAIN, 27));
		RePwLabel.setBounds(12, 92, 100, 30);
		getContentPane().add(RePwLabel);
		
		IdField = new JTextField();
		IdField.setBounds(120, 10, 120, 30);
		getContentPane().add(IdField);
		IdField.setColumns(10);
		
		PwField = new JTextField();
		PwField.setColumns(10);
		PwField.setBounds(120, 52, 212, 30);
		getContentPane().add(PwField);
		
		RePwField = new JTextField();
		RePwField.setColumns(10);
		RePwField.setBounds(120, 92, 212, 30);
		getContentPane().add(RePwField);
		
		//--------------- 확인 버튼 이벤트 처리(아이디 중복 확인)
		JButton Sbtn = new JButton("확인");
		Sbtn.setBounds(252, 10, 80, 32);
		getContentPane().add(Sbtn);
		Sbtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String s1 = null;
				
				try{
					rs=stmt.executeQuery("commit");
					rs=stmt.executeQuery("select * from exmember where id='"+IdField.getText()+"'");
					System.out.println("조회됨");
					
					while(rs.next()){
						String Did = rs.getString(1);
						String Dpw = rs.getString(2);
						
						System.out.println(Did+ ", "+Dpw);
				
						s1 = Did;
					}
				}catch(Exception a){
					a.printStackTrace();
				}
				
				//---------- 중복값이 없을때 nullPointException 발생을 막기 위해 s1 변수에 x 를 넣음
				if(s1==null){
					s1="x";
				}
				
				//--------------- 조건값에 따라 오류 메시지 출력
				System.out.println(s1);
				if((!s1.equals("x"))&&(s1.equals(IdField.getText()))){
					JOptionPane.showMessageDialog(null, "중복된 ID 입니다.","입력",JOptionPane.ERROR_MESSAGE);
					IdField.requestFocus();
				}else if((s1.equals("x"))&&!(s1.equals(IdField.getText()))){
					JOptionPane.showMessageDialog(null, "중복되지 않는 ID 입니다.","입력",JOptionPane.OK_OPTION);
					IdField.requestFocus();
				}
			}
			
		});
		
		//--------------- ok 버튼 이벤트 처리(id, pw 등록)
		JButton Okbtn = new JButton("Ok");
		Okbtn.setBounds(161, 132, 80, 30);
		getContentPane().add(Okbtn);
		Okbtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					rs=stmt.executeQuery("insert into exmember values('"+IdField.getText()+"','"+PwField.getText()+"')");
					System.out.println("입력됨");
					
					rs=stmt.executeQuery("select * from exmember");
					
					while(rs.next()){
						String Did=rs.getString(1);
						String Dpw=rs.getString(2);
					
						System.out.println("입력됨1");
						System.out.println(Did + ",  " + Dpw );
						System.out.println("입력됨2");
					}
					rs=stmt.executeQuery("commit");
					JOptionPane.showMessageDialog(null, "등록되었습니다.","입력",JOptionPane.OK_OPTION);
					IdField.requestFocus();
				}catch(Exception arg0){
					arg0.printStackTrace();
				}
				dispose();
			}
			
		});
		
		//--------------- Cancle 버튼 이벤트 처리(이전 화면으로 돌아감) 
		JButton Canclebtn = new JButton("Cancle");
		Canclebtn.setBounds(252, 132, 80, 30);
		getContentPane().add(Canclebtn);
		Canclebtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();// 현재 창 닫기
			}
			
		});
		
		this.setSize(360, 210);
		this.setVisible(true);
		
		//--------------- DB 연결
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
		stmt=conn.createStatement();
		
		System.out.println("DB 연결");
	}
	
	public static void main(String[] args){
		join01 j = new join01();
	}
}
