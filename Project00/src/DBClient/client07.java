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
	
	//--------------- ����
	Container c;
	private JTextField input;
	DefaultListModel model;
	JComboBox device;
	JList list;
	
	//---------------------------- ��ӹڽ��� ��� ��ġ�׸�
	String[] devicelist = {"null","tv","light","airConditioner","boiler","fridge","humidifier","inductionRange","microwaveRange","rangeHood","riceCooker"}; 
	
	//--------------- ���� ���
	BufferedReader in = null;
	BufferedWriter out = null;
	Socket s=null;
	InputStream is;
	OutputStream os;
	byte[] b=null;
	String m=null, sm=null;
	ArrayList<String> a = new ArrayList<String>();
	
	//--------------- �Է� ��ü
	Scanner in1 = new Scanner(System.in);
	
	//--------------- ������
	InputMessage iM;
	
	//------------------- ������
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
		
		//---------------------------- ��ġ ����Ʈ
		device = new JComboBox(devicelist);
		device.setBounds(12, 449, 115, 52);
		getContentPane().add(device);
		
		//---------------------------- ��ġ ���� Ȯ�� ��ư
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
		
		//---------------------------- �Է� �ؽ�Ʈ�ʵ�
		input = new JTextField();
		input.setBounds(216, 450, 256, 52);
		getContentPane().add(input);
		input.setColumns(10);
		input.addActionListener(this);
		
		//---------------------------- ����Ʈ Ȯ�� ��ư
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
	
	//------------------- ���� �Է½� �޽��� �۽�
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
	
	//--------------------- ����
	private void connection() throws IOException{
		s= new Socket();
		System.out.println("���� ����");
		System.out.println("���� ��û");
		s.connect(new InetSocketAddress("localhost",5002));
		System.out.println("���� ����");
		
		is=s.getInputStream();
		out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	}
	
	//------------ �������� ���� �޽��� by ������
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
					System.out.println("�ޱ� ����");
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	//------------- �������� ������ �޽���
	private void outputMessage(){
		try{
			String ss = "client> "+sm;
			out.write(ss);
			out.flush();
			
			System.out.println("������ ����");
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
