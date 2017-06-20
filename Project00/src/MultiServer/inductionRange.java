package MultiServer;

public class inductionRange implements Device_IoT{

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
	
	public void inductionRangeContains(String m){
		if(m.contains("inductionRange")){
			if(m.contains("on")){
				state=true;
				turnOn();
				outInductionRangeState();				
			}else if(m.contains("off")){
				state=false;
				turnOff();
				outInductionRangeState();
			}
		}
	}
	
	public	String outInductionRangeState() {		
		cm = s;		
		return cm;
	}
	
}
