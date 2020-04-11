import java.awt.AWTException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import spark.Spark;

public class Main {
	
	private static String IP_ADRESS = Main.getIpAdress();
	private static int PORT = 7777;
	
	public static void main(String[] args) throws AWTException {
		Controller controller = new Controller();
		
//		if(!Main.IP_ADRESS.equals(""))
//			ipAddress(Main.IP_ADRESS);
		Spark.port(Main.PORT);
		
		Spark.staticFiles.location("/public");
		
		Spark.exception(Exception.class, (e, req, res) -> {
			String msg = e.getMessage();
			
			System.err.println("Spark Uncaught Exception: ");
			if(msg.contains("Connection reset"))
				System.err.println(msg);
			else
				e.printStackTrace();
			
			res.status(500);
			res.body("Ocorreu um erro no servidor¹.");
		}); // print all exceptions
		
		Spark.get("/exec", (req, res) -> controller.exec(req, res));
		
		System.out.println("Server started at http://localhost:"+Main.PORT+" and http://"+Main.IP_ADRESS+":"+Main.PORT);
	}
	
	private static String getIpAdress() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			return inetAddress.getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
	}

}
