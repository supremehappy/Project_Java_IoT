package MultiServer;

public class rangeHood implements Device_IoT{

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
	
	public void rangeHoodContains(String m){
		if(m.contains("rangeHood")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outRangeHoodState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outRangeHoodState();
			}
		}
	}
	
	public	String outRangeHoodState() {		
		cm = s;		
		return cm;
	}
	
}
