package MultiServer;

public class TV implements Device_IoT {

	boolean state=false;
	String s=null; String cm=null;
	
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
	
	public void tvContains(String m){
		if(m.contains("tv")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outTvState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outTvState();
			}
		}
	}
	
	public	String outTvState() {		
		cm = s;		
		return cm;
	}
}