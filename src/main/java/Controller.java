import java.awt.AWTException;
import java.util.List;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import spark.Request;
import spark.Response;

public class Controller {
	private BetterRobot robot;
	
	public Controller() throws AWTException {
		this.robot = new BetterRobot();
	}
	
	// http://sparkjava.com/
	public String exec(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");
		
		List<String> cmds = params.get("commands[]");
		
		System.out.println("Cmds: "+ cmds);
		if(cmds != null && cmds.size() > 0) {
			String[] commands = cmds.toArray(new String[0]);
			this.robot.exec(commands);
			
			return "Commands executed: " +cmds;
		}
		
		return "Empty command.";
	}
	
	public String type(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");
		
		String text = params.getString("text");
		
		System.out.println("Text: "+ text);
		if(!text.equals("")) {
			this.robot.type(text);
			
			return "Text typed: " +text;
		}
		
		return "Empty text.";
	}
}
