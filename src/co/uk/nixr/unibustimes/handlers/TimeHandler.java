package co.uk.nixr.unibustimes.handlers;

public class TimeHandler {
	public static String time = "Nothing!";
	
	public TimeHandler()
	{
		
	}
	

	public void setTime(String loc){
		time = loc;
	}
	
	public String getTime(){
		return time;
	}

}
