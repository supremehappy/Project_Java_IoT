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

	//--------------- ����
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
	
	//--------------- ������
	public login01(){
		
		this.setTitle("login Ex");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c = getContentPane();
		c.setLayout(null);
		
		JLabel IdLabel = new JLabel("ID");
		IdLabel.setFont(new Font("HY����L", Font.PLAIN, 27));
		IdLabel.setBounds(12, 10, 100, 30);
		getContentPane().add(IdLabel);
		
		JLabel PwLabel = new JLabel("PW");
		PwLabel.setFont(new Font("HY����L", Font.PLAIN, 27));
		PwLabel.setBounds(12, 52, 100, 30);
		getContentPane().add(PwLabel);
		
		//--------------- ok ��ư �̺�Ʈ ó��(db�� �ִ� ����� ���Ͽ� �ߺ��Ǹ� �α���)
		JButton Okbtn = new JButton("Ok");
		Okbtn.setBounds(54, 92, 80, 30);
		getContentPane().add(Okbtn);
		Okbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s1=null, s2=null;
				//-------------------------- ���ǰ� �񱳷� �ش��ϴ� ���� �޽��� ȣ��
				if(IdField.getText().isEmpty()==true){
					JOptionPane.showMessageDialog(null, "plz, input id","�Է�",JOptionPane.ERROR_MESSAGE);
					IdField.requestFocus();
				}else if(PwField.getText().isEmpty()==true){
					JOptionPane.showMessageDialog(null, "plz, input pw","�Է�",JOptionPane.ERROR_MESSAGE);
					PwField.requestFocus();
				}else{
					try{
						rs=stmt.executeQuery("select * from exmember");
					System.out.println("�Էµ�");
					while(rs.next()){
						String Did=rs.getString(1);
						String Dpw=rs.getString(2);
						
						System.out.println("�Էµ�1");
						System.out.println(Did + ",  " + Dpw );
						System.out.println("�Էµ�2");
						s1 = Did;
						s2 = Dpw;
					}
					
					if(!((s1.equals(IdField.getText()))&&(s2.equals(PwField.getText())))){
						JOptionPane.showMessageDialog(null, "id �Ǵ� pw �� �ٸ��ϴ�.","�Է�",JOptionPane.ERROR_MESSAGE);
						IdField.requestFocus();
					}else{						
						dispose(); // ���� ���� â �ݱ�
						getContentPane().add(new client07()); 		//client05 ������ ȣ��(client �� �ҷ�����)
					}
					}catch(Exception arg0){
						arg0.printStackTrace();
					}
				}
			}
		});		
		
		//--------------- join ��ư �̺�Ʈ ó��(ȸ������)
		JButton Joinbtn = new JButton("Join");
		Joinbtn.setBounds(146, 92, 80, 30);
		getContentPane().add(Joinbtn);
		Joinbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().add(new join01());		//join00 ������ ȣ��(ȸ������ �� �ҷ�����)
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
		
		
		//------------------- DB ����
		try{
			DBconnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	//------------ DB ���� �޼ҵ�
	private void DBconnection() throws Exception{
		Class.forName(driver);
		
		conn=DriverManager.getConnection(url,id,pw);
		stmt = conn.createStatement();
		
		System.out.println("DB ���� ����");
	}
	
	public static void main(String[] args){
		login01 l = new login01();
	}
}
