package MultiServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

public class testServer02 {
	
	//--------------- 소켓 통신
	ServerSocket s =null;
	Socket so= null;
	InputStream is;
	BufferedWriter out;
	InetSocketAddress isa;
	byte[] b;
	String m = null, cm = null;
	Vector v =null;
	
	//--------------- DB
	Connection conn=null;
	ResultSet rs=null;
	Statement stmt = null;
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "hr", pw="1234";
	
	//--------------- 장치 클래스
	TV tv = new TV();
	light light = new light();
	airConditioner air = new airConditioner();
	boiler boiler = new boiler();
	fridge fri = new fridge();
	humidifier hum = new humidifier();
	inductionRange induc = new inductionRange();
	microwaveRange micro = new microwaveRange();
	rangeHood hood = new rangeHood();
	riceCooker rice = new riceCooker();
	
	ArrayList<String> a = new ArrayList<String>();
	
	testServer02() {
		v = new Vector(5);
		try {
			DBconnection();

			s = new ServerSocket();
			s.bind(new InetSocketAddress("localhost", 6001));

			while (true) {
				so = s.accept();				
				isa=(InetSocketAddress) so.getRemoteSocketAddress();
				System.out.println("연결됨");
				
				try {
					//System.out.println("a");
					for (int i = 0; i < v.size(); i++) {
						Socket so = (Socket) v.get(i);
						//System.out.println("b");
						if(so==null){
							v.remove(i);
							v.trimToSize();
						}
					}
				} catch (Exception a) {
					a.printStackTrace();
				}
				//System.out.println("추가");
				v.add(so);
				
				Message m = new Message(v,so);
				//System.out.println("스레드 시작");
				m.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				s.close();
			}catch(Exception a){
				a.printStackTrace();
			}
		}

	}
	
	private void DBconnection() throws Exception{
	
		Class.forName(driver);
		
		conn=DriverManager.getConnection(url,id,pw);
		stmt = conn.createStatement();
		
		System.out.println("DB연결됨");
	}
	
	class Message extends Thread{
		private Vector v;
		private Socket so;
		
		public Message(Vector v, Socket so){
			this.v=v;
			this.so=so;
		}
		
		public void run(){
			try{
				while(true){
					is=so.getInputStream();
					
					b = new byte[1024];
					int readCount = is.read(b);
					m = new String(b,0,readCount,"EUC-KR");
					
					System.out.println("m : "+m);
					System.out.println("받기 성공");
					
					String Cname = "["+Thread.currentThread().getName()+"] : ";
					String ss=null;
					
					for(int i =0; i<v.size();i++){
						Socket so = (Socket)v.get(i);
						if(so.isClosed()){
							v.remove(i);
							v.trimToSize();
						}else{
							out = new BufferedWriter(new OutputStreamWriter(so.getOutputStream()));
							
							if(m.contains("view")){
								viewStateSelect();
								ss = Cname+cm;
								out.write(ss);
								out.newLine();
								out.flush();
							}else if((m.contains("list"))||(m.contains("state"))){
								buttonContains();
								for(int j = 0; j<a.size();j++){
									
									cm=a.get(j);
									System.out.println("a.get : "+a.get(j));
									ss = Cname+cm;
									out.write(ss);
									out.newLine();
									out.flush();
								}
								a.clear();
							}else{
								deviceContains(m);
								ss = Cname+cm;
								out.write(ss);
								out.newLine();
								out.flush();
							}
							
							
							System.out.println("ss : "+ss);
							System.out.println("보내기 성공");
							
							if(m==null){
								break;
							}
						}
					}
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					so.close();
				}catch(IOException a){
					a.printStackTrace();
				}
			}
		}
	}

	// ------------- 디바이스 구별메소드
	public void deviceContains(String m) {
		if (m.contains("tv")) {
			tv.tvContains(m);
			DBUpdate();
			cm = tv.outTvState();
		} else if (m.contains("light")) {
			light.lightContains(m);
			DBUpdate();
			cm = light.outLightState();
		} else if (m.contains("airConditioner")) {
			air.airContains(m);
			DBUpdate();
			cm = air.outAirState();
		} else if (m.contains("boiler")) {
			boiler.boilerContains(m);
			DBUpdate();
			cm = boiler.outBoilerState();
		} else if (m.contains("fridge")) {
			fri.fridgeContains(m);
			DBUpdate();
			cm = fri.outFridgeState();
		} else if (m.contains("humidifier")) {
			hum.humidifierContains(m);
			DBUpdate();
			cm = hum.outHumidifierState();
		} else if (m.contains("inductionRange")) {
			induc.inductionRangeContains(m);
			DBUpdate();
			cm = induc.outInductionRangeState();
		} else if (m.contains("microwaveRange")) {
			micro.microwaveRangeContains(m);
			DBUpdate();
			cm = micro.outMicrowaveRangeState();
		} else if (m.contains("rangeHood")) {
			hood.rangeHoodContains(m);
			DBUpdate();
			cm = hood.outRangeHoodState();
		} else if (m.contains("riceCooker")) {
			rice.riceCookerContains(m);
			DBUpdate();
			cm = rice.outRiceCookerState();
		} else {
			return;
		}
	}

	// ------------- 버튼 구별메소드
	public void buttonContains() {
		if (m.contains("state")) {
			DBStateSelect();
		} else if (m.contains("list")) {
			DBSelect();
		} else {
			return;
		}
	}

	// ------------- db 커밋 메소드
	public void DBCommit() {
		try {
			rs = stmt.executeQuery("commit");

			System.out.println("커밋 성공");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------- list 버튼 액션 메소드
	public void DBSelect() {
		try {
			int i = 0;
			String as = null;

			DBCommit();
			rs = stmt.executeQuery("select * from db01");

			System.out.println("조회됨");

			while (rs.next()) {
				String Did = rs.getString(1);
				String Dname = rs.getString(2);
				String Dstate = rs.getString(3);

				System.out.println(Did + ", " + Dname + ",  " + Dstate + "\n");
				cm = Did + ", " + Dname + ",  " + Dstate + "\n";

				if (i < Integer.parseInt(Did)) {
					as = Did + ", " + Dname + ",  " + Dstate + "\n";
					a.add(as);
					i++;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------- state 버튼 액션 메소드
	public void DBStateSelect() {
		try {
			int i = 0;
			String as = null;

			DBCommit();
			rs = stmt.executeQuery("select * from db01");

			System.out.println("조회됨");

			while (rs.next()) {
				String Did = rs.getString(1);
				String Dname = rs.getString(2);
				String Dstate = rs.getString(3);

				System.out.println(Dname + ", " + Dstate);
				cm = Dname + ",  " + Dstate + "\n";

				if (i < Integer.parseInt(Did)) {
					as = Dname + ",  " + Dstate + "\n";
					a.add(as);
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------- view 클래스에서 연결 버튼 액션 메소드
	public void viewStateSelect() {
		try {
			DBCommit();
			String split[] = m.split(" ");
			System.out.println(split[1]);
			DBCommit();
			rs = stmt.executeQuery("select * from db01 where name ='" + split[1] + "'");

			System.out.println("조회됨");

			while (rs.next()) {
				String Did = rs.getString(1);
				String Dname = rs.getString(2);
				String Dstate = rs.getString(3);

				System.out.println(Dname + ", " + Dstate);
				cm = Dname + ",  " + Dstate + "\n";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ------------- DB 에 있는 장치 상태 변경 메소드
	public void DBUpdate() {

		String split[] = m.split(" ");
		// System.out.println("split : "+split[0]+", "+split[1]+", "+split[2]);

		try {
			DBCommit();

			rs = stmt.executeQuery("update db01 set state ='" + split[2] + "' where name='" + split[1] + "'");
			System.out.println("수정됨");

			rs = stmt.executeQuery("select * from db01 where name='" + split[1] + "'");

			while (rs.next()) {
				String Did = rs.getString(1);
				String Dname = rs.getString(2);
				String Dstate = rs.getString(3);

				cm = Dname + ", " + Dstate;
				// System.out.println(Did+", "+Dname+", "+Dstate);
				// System.out.println("cm : "+ cm);
				System.out.println("조회됨");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		testServer02 ss = new testServer02();
	}

}
