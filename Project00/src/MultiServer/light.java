package MultiServer;

public class light implements Device_IoT{

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
	
	public void lightContains(String m){
		if(m.contains("light")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outLightState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outLightState();
			}
		}
	}
	
	public	String outLightState() {		
		cm = s;		
		return cm;
	}
	
}
