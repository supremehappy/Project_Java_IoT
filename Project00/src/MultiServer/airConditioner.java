package MultiServer;

public class airConditioner implements Device_IoT{

	boolean state=false;
	String s=null; String cm=null;
	long start = System.currentTimeMillis();
	
	@Override
	public void turnOn(){
		s = "on";
		state=true;
	}
	
	@Override
	public void turnOff(){
		s = "off";
		state=false;
	}
	
	public void airContains(String m){
		if(m.contains("airConditioner")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outAirState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outAirState();
			}
		}
	}
	
	public	String outAirState() {		
		cm = s;		
		return cm;
	}

}
