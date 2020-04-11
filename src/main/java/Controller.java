import java.awt.AWTException;
import org.json.JSONArray;
import spark.Request;
import spark.Response;

public class Controller {
	private BetterRobot robot;
	
	public Controller() throws AWTException {
		this.robot = new BetterRobot();
	}
	
	// http://sparkjava.com/
	public String exec(Request req, Response res) {
		String cmd = req.queryParamOrDefault("command", "[]");
		if(!cmd.equals("[]")) {
			JSONArray tmpArr = new JSONArray(cmd);
			String[] commands = this.toStringArray(tmpArr);
			this.robot.exec(commands);
			
			return "Command executed: " +cmd;
		}
		
		return "Empty command.";
	}
	
	private String[] toStringArray(JSONArray array) {
	    if(array == null)
	        return null;

	    String[] arr = new String[array.length()];
	    for(int i = 0; i < arr.length; i++) {
	        arr[i] = array.optString(i);
	    }
	    return arr;
	}
}
