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
		String layout = params.getString("layout");

		if (cmds == null || cmds.size() == 0) {
			res.status(400);
			return "Empty commands.";
		} else if (layout == null || !this.robot.getKeyboardLayoutManager().hasLayout(layout)) {
			res.status(400);
			return "Invalid layout.";
		}

		System.out.println("Cmds: " + cmds);

		String[] commands = cmds.toArray(new String[0]);
		this.robot.exec(layout, commands);

		return "Commands executed: " + cmds;
	}

	public String type(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String text = params.getString("text");

		if (text == null || text.isEmpty()) {
			res.status(400);
			return "Empty text.";
		}

		System.out.println("Text: " + text);

		this.robot.type(text);

		return "Text typed: " + text;
	}

	public String press_key(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String key = params.getString("key");
		String layout = params.getString("layout");

		if (key == null || key.isEmpty()) {
			res.status(400);
			return "Empty key.";
		} else if (layout == null || !this.robot.getKeyboardLayoutManager().hasLayout(layout)) {
			res.status(400);
			return "Invalid layout.";
		}

		System.out.println("press_key.Key: " + key);

		this.robot.press_key(layout, key);

		return "Key pressed: " + key;
	}

	public String release_key(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String key = params.getString("key");
		String layout = params.getString("layout");

		if (key == null || key.isEmpty()) {
			res.status(400);
			return "Empty key.";
		} else if (layout == null || !this.robot.getKeyboardLayoutManager().hasLayout(layout)) {
			res.status(400);
			return "Invalid layout.";
		}

		System.out.println("release_key.Key: " + key);

		this.robot.release_key(layout, key);

		return "Key released: " + key;
	}

	public String move_mouse(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String tmp = params.getString("movements");

		if (tmp == null || tmp.isEmpty()) {
			res.status(400);
			return "Empty movements.";
		}

		System.out.println("Movements: " + tmp);

		JSONArray movements = new JSONArray(tmp);
		this.robot.move_mouse(movements);

		return "Movements done: " + tmp;
	}

	public String mouse_down(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String button = params.getString("button");

		if (button == null || button.isEmpty()) {
			res.status(400);
			return "Empty mouse button to press.";
		}

		System.out.println("mouse_down.Button: " + button);

		button = button.toLowerCase();
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
			default: {
				res.status(400);
				return "Invalid mouse button to press.";
			}
		}

		return "Mouse button pressed: " + button;
	}

	public String mouse_up(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		String button = params.getString("button");

		if (button == null || button.isEmpty()) {
			res.status(400);
			return "Empty mouse button to release.";
		}

		System.out.println("mouse_up.Button: " + button);

		button = button.toLowerCase();
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
			default: {
				res.status(400);
				return "Invalid mouse button to release.";
			}
		}

		return "Mouse button released: " + button;
	}

	public String mouse_click(Request req, Response res) {
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8");

		float sensibility = 1.0f;
		try {
			sensibility = Float.parseFloat(params.getString("sensibility"));
		} catch (NumberFormatException ex) {
		}

		String button = params.getString("button");

		if (button == null || button.isEmpty()) {
			res.status(400);
			return "Empty mouse button to click/scroll.";
		}

		System.out.println("mouse_click.Button: " + button + "; Sensibility: " + sensibility);

		button = button.toLowerCase();
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
			default: {
				res.status(400);
				return "Invalid mouse button to click/scroll.";
			}
		}

		return "Mouse button clicked/scrolled: " + button;
	}
}
