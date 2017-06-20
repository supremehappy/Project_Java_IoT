package MultiServer;

public class fridge implements Device_IoT{

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
	
	public void fridgeContains(String m){
		if(m.contains("fridge")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outFridgeState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outFridgeState();
			}
		}
	}
	
	public	String outFridgeState() {		
		cm = s;		
		return cm;
	}
	
}
