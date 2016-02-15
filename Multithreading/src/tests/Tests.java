package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import logger.Logger;
import multithreading.MultiThreadedServer;

import org.junit.Test;

public class Tests {
	
	private static final Path path = Paths.get("/home/aneta/Desktop/", "test.log");
	private Logger logger;
	
	@Test
	public void test() throws IOException {
		logger = new Logger(path);
		Thread thread1 = new Thread() {
			public void run() {
				try {
					logger.log("Aneta", "This is Aneta's testaaaaaaaaaa");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread thread2 = new Thread() {
			public void run() {
				try {
					logger.log("Ivan", "This is Ivan's testwwwwwwwwwwwwwwwwwwwwwww");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		thread1.start();
		thread2.start();
	}
	
	@Test
	public void testConnectionToServer() {
		MultiThreadedServer.setServerActive(false);
		assertEquals(false, MultiThreadedServer.isServerActive());
	}
	
	@Test
	public void testIfConnectionToServerIsClosed() {
		MultiThreadedServer.setServerActive(true);
		MultiThreadedServer.closeConnectionToServer();
		assertEquals(false, MultiThreadedServer.isServerActive());
	}

}
