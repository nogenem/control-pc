import java.awt.AWTException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import io.github.cdimascio.dotenv.Dotenv;
import spark.Spark;

public class Main {

	private static String AHK_FILE = "SoundControl.exe";
	private static String IP_ADDRESS = Main.getIpAdress();

	private static Dotenv dotenv = null;
	private static int serverPort = 7777;

	private static AhkFileHandler ahkFileHandler = null;
	private static Controller controller = null;

	public static void main(String[] args) throws AWTException {
		Main.loadEnv();

		Main.controller = new Controller();
		Main.ahkFileHandler = new AhkFileHandler(Main.AHK_FILE);

		Main.ahkFileHandler.start();
		Main.setupSpark();
		Main.setupRoutes();
		Main.registerShutdownThread();

		System.out.println(String.format(
				"\n>> Server started at http://localhost:%d and http://%s:%d\n",
				Main.serverPort, Main.IP_ADDRESS, Main.serverPort));
	}

	private static void loadEnv() {
		Main.dotenv = Dotenv.configure().load();

		String envPort = Main.dotenv.get("PORT");
		if (envPort != null && !envPort.isEmpty()) {
			Main.serverPort = Integer.parseInt(envPort);
		}
	}

	private static void setupSpark() {
		Spark.port(Main.serverPort);

		if (!Main.isInsideJar()) {
			String projectDir = System.getProperty("user.dir");
			String staticDir = "/src/main/resources/public";
			Spark.staticFiles.externalLocation(projectDir + staticDir);
		} else {
			if (Main.isPublicFolderInsideTheRootFolder()) {
				Spark.staticFiles.location("/public");
			} else {
				Spark.staticFiles.location("/resources/public");
			}
		}

		Spark.exception(Exception.class, (e, req, res) -> {
			String msg = e.getMessage();

			System.err.println("Spark Uncaught Exception: ");
			if (msg != null && msg.contains("Connection reset"))
				System.err.println(msg);
			else
				e.printStackTrace();

			res.status(500);
			res.body("An error occurred on the server.");
		}); // print all exceptions
	}

	private static void setupRoutes() {
		Spark.post("/exec", (req, res) -> controller.exec(req, res));
		Spark.post("/type", (req, res) -> controller.type(req, res));
		Spark.post("/press_key", (req, res) -> controller.press_key(req, res));
		Spark.post("/release_key", (req, res) -> controller.release_key(req, res));
		Spark.post("/move_mouse", (req, res) -> controller.move_mouse(req, res));
		Spark.post("/mouse_down", (req, res) -> controller.mouse_down(req, res));
		Spark.post("/mouse_up", (req, res) -> controller.mouse_up(req, res));
		Spark.post("/mouse_click", (req, res) -> controller.mouse_click(req, res));
	}

	private static void registerShutdownThread() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("In shutdown hook");
				if (Main.ahkFileHandler != null)
					Main.ahkFileHandler.stop();
			}
		}, "Shutdown-thread"));
	}

	private static boolean isInsideJar() {
		URL path = Main.class.getResource("Main.class");
		return path.toString().startsWith("jar:");
	}

	private static boolean isPublicFolderInsideTheRootFolder() {
		URL resource = Main.class.getResource("/public/index.html");
		return resource != null;
	}

	private static String getIpAdress() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			return inetAddress.getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return "";
		}
	}

}
