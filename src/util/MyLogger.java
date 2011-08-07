package util;

public class MyLogger {

	public static final int	OFF			= 0;
	public static final int	INFO		= 1;
	private int				logLevel	= INFO;

	public void info(final Object obj) {
		if (getLogLevel() >= INFO) {
			System.out.println(obj.toString());
		}
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public int getLogLevel() {
		return logLevel;
	}

}
