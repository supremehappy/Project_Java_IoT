package MultiServer;

public class riceCooker implements Device_IoT{

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
	
	public void riceCookerContains(String m){
		if(m.contains("riceCooker")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outRiceCookerState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outRiceCookerState();
			}
		}
	}
	
	public	String outRiceCookerState() {		
		cm = s;		
		return cm;
	}
	
}
