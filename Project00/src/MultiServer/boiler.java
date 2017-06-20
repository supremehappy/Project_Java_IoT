package MultiServer;

public class boiler implements Device_IoT{

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
	
	public void boilerContains(String m){
		if(m.contains("boiler")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outBoilerState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outBoilerState();
			}
		}
	}
	
	public	String outBoilerState() {		
		cm = s;		
		return cm;
	}
	
}
