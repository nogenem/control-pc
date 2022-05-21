import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.util.List;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
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
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		List<String> cmds = params.get("commands[]");

		System.out.println("Cmds: " + cmds);
		if (cmds != null && cmds.size() > 0) {
			String[] commands = cmds.toArray(new String[0]);
			this.robot.exec(commands);

			return "Commands executed: " + cmds;
		}

		return "Empty command.";
	}

	public String type(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String text = params.getString("text");

		System.out.println("Text: " + text);
		if (!text.equals("")) {
			this.robot.type(text);

			return "Text typed: " + text;
		}

		return "Empty text.";
	}

	public String move_mouse(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String tmp = params.getString("movements");

		System.out.println("Movements: " + tmp);
		if (!tmp.equals("")) {
			JSONArray movements = new JSONArray(tmp);
			this.robot.move_mouse(movements);

			return "Movements done: " + tmp;
		}

		return "Empty text.";
	}

	public String mouse_down(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String button = params.getString("button").toLowerCase();

		System.out.println("mouse_down.Button: " + button);
		if (!button.equals("")) {
			switch (button) {
				case "left":
					this.robot.mouse_down(InputEvent.BUTTON1_DOWN_MASK);
					break;
				case "middle":
					this.robot.mouse_down(InputEvent.BUTTON2_DOWN_MASK);
					break;
				case "right":
					this.robot.mouse_down(InputEvent.BUTTON3_DOWN_MASK);
					break;
				default:
					break;
			}

			return "Mouse pressed: " + button;
		}

		return "Empty text.";
	}

	public String mouse_up(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String button = params.getString("button").toLowerCase();

		System.out.println("mouse_up.Button: " + button);
		if (!button.equals("")) {
			switch (button) {
				case "left":
					this.robot.mouse_up(InputEvent.BUTTON1_DOWN_MASK);
					break;
				case "middle":
					this.robot.mouse_up(InputEvent.BUTTON2_DOWN_MASK);
					break;
				case "right":
					this.robot.mouse_up(InputEvent.BUTTON3_DOWN_MASK);
					break;
				default:
					break;
			}

			return "Mouse pressed: " + button;
		}

		return "Empty text.";
	}

	public String mouse_click(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String button = params.getString("button").toLowerCase();
		int sensibility = 1;

		try {
			sensibility = Integer.parseInt(params.getString("sensibility"));
		} catch (NumberFormatException ex) {
		}

		System.out.println("mouse_click.Button: " + button + ". Sensibility: " + sensibility);
		if (!button.equals("")) {
			switch (button) {
				case "left":
					this.robot.mouse_click(InputEvent.BUTTON1_DOWN_MASK);
					break;
				case "middle":
					this.robot.mouse_click(InputEvent.BUTTON2_DOWN_MASK);
					break;
				case "right":
					this.robot.mouse_click(InputEvent.BUTTON3_DOWN_MASK);
					break;
				case "scroll_up":
					this.robot.scroll_up(sensibility);
					break;
				case "scroll_down":
					this.robot.scroll_down(sensibility);
					break;
				default:
					break;
			}

			return "Mouse pressed: " + button;
		}

		return "Empty text.";
	}
}
