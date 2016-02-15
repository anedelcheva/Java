package logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger implements LoggerInterface {
	
	private static final SimpleDateFormat tsFormat = 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private Path logFile;
	private PrintWriter logFileWriter;
	
	public Logger(Path logFile) throws IOException {
		this.logFile = logFile;
		this.open();
	}
	
	private void open() throws IOException {
		FileOutputStream logFileOS = new FileOutputStream(this.logFile.toString(), true);
		this.logFileWriter = new PrintWriter(logFileOS);
	}
	
	@Override
	public void close() throws Exception {
		this.logFileWriter.close();
	}

	@Override
	public void log(String username, String msg) throws IOException {
		Calendar now = Calendar.getInstance();
		String currentTime = Logger.tsFormat.format(now.getTime());
		this.logFileWriter.println(currentTime + " [" + username + "]: " + msg);
		logFileWriter.flush();
	}
}
