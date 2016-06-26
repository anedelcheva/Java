import java.util.Random;

public class CountingSort {
	
	private int[] array;
	private int[] occurrences;
	private int[] sumCount;
	private int[] sortedArray;
	private static Random generator = new Random();
	
	public CountingSort(int size) {
		array = new int[size];
		for(int i = 0; i < array.length; ++i)
			array[i] = 1 + generator.nextInt(15);
	}
	
	public int getMaximum() {
		int max = array[0];
		for(int i = 1; i < array.length; ++i)
			if(max < array[i])
				max = array[i];
		return max;
	}
	
	public void output(int array[]) {
		for(int i = 0; i < array.length; ++i)
			System.out.printf("%2d ", array[i]);
		System.out.println();
	}
	
	public void countingSort() {
		int max = getMaximum();
		occurrences = new int[max + 1];
		for(int i = 0; i < array.length; ++i)
			++occurrences[array[i]];
		System.out.printf("%14s", "Array: ");
		output(array);
		System.out.printf("%14s", "Occurrences: ");
		output(occurrences);
		sumCount = new int[max + 1];
		sumCount[0] = occurrences[0];
		for(int i = 1; i < occurrences.length; ++i)
			sumCount[i] = occurrences[i] + sumCount[i - 1];
		System.out.printf("%14s", "SumCount: ");
		output(sumCount);
		sortedArray = new int[array.length];
		for(int i = 0; i < array.length; ++i) {
			sortedArray[sumCount[array[i]] - 1] = array[i];
			--sumCount[array[i]];
		}
		System.out.printf("%14s", "Sorted Array: ");
		output(sortedArray);
	}
}
