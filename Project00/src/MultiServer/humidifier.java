package MultiServer;

public class humidifier implements Device_IoT{

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
	
	public void humidifierContains(String m){
		if(m.contains("humidifier")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outHumidifierState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outHumidifierState();
			}
		}
	}
	
	public	String outHumidifierState() {		
		cm = s;		
		return cm;
	}
	
}
