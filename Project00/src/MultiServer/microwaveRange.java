package MultiServer;

public class microwaveRange implements Device_IoT{

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
	
	public void microwaveRangeContains(String m){
		if(m.contains("microwaveRange")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outMicrowaveRangeState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outMicrowaveRangeState();
			}
		}
	}
	
	public	String outMicrowaveRangeState() {		
		cm = s;		
		return cm;
	}
	
}
