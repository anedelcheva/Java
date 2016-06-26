package logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	private static final Path logFile = Paths.get("/home/aneta/Desktop/", "current.log");
	
	public static void main(String[] args) throws IOException {
		Logger logger = createLogFile();
		//logger.log("Aneta", "I love you");
		//Main.reset();
	}
	
	private static void reset() throws IOException {
		Files.deleteIfExists(logFile);
	}

	public static Logger createLogFile() throws IOException {
		return new Logger(logFile);
		
	}
}
