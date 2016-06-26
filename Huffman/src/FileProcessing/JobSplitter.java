package FileProcessing;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

public class JobSplitter extends Thread {
	
	private static final int ASCII_SYMBOLS_NUMBER = 256;
	private long[] frequency_table = new long[ASCII_SYMBOLS_NUMBER];
	private static String filecontent;
	private static FileInputStream in;
	private String[] splitFileContent;
	private int portionIndex;
	static boolean quiet = false;
	
	public static String readFile(String pathname) throws IOException {
		File file = new File(pathname);
		in = new FileInputStream(file);
		byte[] buffer = new byte[(int)file.length()];
		in.read(buffer);
		filecontent = new String(buffer);
		return filecontent;
	}
	
	public static String[] splitToNumberOfThreads(String filecontent, int threads) {
		String[] partsOfFile = new String[threads];
		int sizeOfPart = (int) Math.ceil(filecontent.length() * 1.0 / threads);
		for (int i = 0; i < threads; i++) {
			partsOfFile[i] = filecontent.substring(i * sizeOfPart, Math.min((i + 1) * sizeOfPart, filecontent.length()));
		}
		return partsOfFile;
	}
	
	public JobSplitter(String[] splitFileContent, int portionIndex) {
		this.splitFileContent = splitFileContent;
		this.portionIndex = portionIndex;
	}
	
	public long[] computeFrequencyTable(String[] filecontent, int index) {
		char ascii_symbol;
		int ascii_code;
		String partOfFile = filecontent[index];
		for (int i = 0; i < partOfFile.length(); i++) {
			ascii_symbol = partOfFile.charAt(i);
			ascii_code = (int) ascii_symbol;
			if (ascii_code < 256) {
				frequency_table[ascii_code]++;
			}
		}
		return frequency_table;
	}
	
	public static void mergeFrequencyTables(long[] whole_frequency_table, long[] frequency_table) {
		for (int i = 0; i < ASCII_SYMBOLS_NUMBER; i++) {
			whole_frequency_table[i] += frequency_table[i];
		}
	}
	
	public static long[] computeInParallel(String filecontent, int threads) {
		
		String[] split_file_content = splitToNumberOfThreads(filecontent, threads);
		JobSplitter[] jobs = new JobSplitter[threads];
		for (int i = 0; i < threads; i++) {
			jobs[i] = new JobSplitter(split_file_content, i);
			jobs[i].start();
		}
		try {
			for(JobSplitter job : jobs) {
				job.join();
			}
		}
		catch(InterruptedException e) {}
		
		long[] whole_frequency_table = new long[ASCII_SYMBOLS_NUMBER];
		for(JobSplitter job : jobs) {
			mergeFrequencyTables(whole_frequency_table, job.getFrequencyTable());
		}
		return whole_frequency_table;
	}
	
	public long[] getFrequencyTable() {
		return frequency_table;
	}

	public void run() {
		long start = 0;
		if(!quiet)
		{
			System.out.println(Thread.currentThread().getName() + " started execution");
			start = System.currentTimeMillis();
			
			frequency_table = computeFrequencyTable(splitFileContent, portionIndex);
			
			System.out.println(Thread.currentThread().getName() + " stopped execution");
			System.out.println(Thread.currentThread().getName() + " execution time was (millis): " + (System.currentTimeMillis() - start));
		}
		else {
			frequency_table = computeFrequencyTable(splitFileContent, portionIndex);
		}
	}
	
	public static void printFrequencyTable(long[] table) {
		for (int i = 0; i < table.length; i++) {
			if (table[i] != 0) {
				System.out.printf("%d - %d\n", i, table[i]);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		int number_of_threads = 0;
		quiet = false;
		String file_path = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-f")) {
				file_path = args[i + 1];
			}
			else if (args[i].equals("-t") || args[i].equals("-tasks") || 
					args[i].equals("threads") || args[i].equals("tasks")) {
				number_of_threads = Integer.parseInt(args[i + 1]);
			}
			else if (args[i].equals("-q") || args[i].equals("-quiet") || args[i].equals("quiet")) {
				quiet = true;
			}
		}
		if (file_path == null || number_of_threads == 0) {
			System.err.println("Arguments not initialized properly");
			System.exit(1);
		}
		String filecontent = readFile(file_path);
		long start = System.currentTimeMillis();
		computeInParallel(filecontent, number_of_threads);
		long time = System.currentTimeMillis() - start;
		System.out.printf("Threads used in current run: %s\n", number_of_threads);
        	System.out.println("Total execution time for current run (millis): " + time);
	}
}
