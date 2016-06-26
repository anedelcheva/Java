package logger;

import java.io.IOException;

public interface LoggerInterface extends AutoCloseable {
	public void log(String username, String msg)
		throws IOException;
}