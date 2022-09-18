
public class AhkFileHandler {
	private String path;
	private Process p = null;

	public AhkFileHandler(String path) {
		this.path = path;
	}

	public void start() {
		this.stop();

		try {
			ProcessBuilder pb = new ProcessBuilder(this.path);
			this.p = pb.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		if (this.p != null) {
			this.p.destroyForcibly();
			this.p = null;
		}
	}
}
