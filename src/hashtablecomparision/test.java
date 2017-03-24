package hashtablecomparision;

/**
 *
 * @author param
 */


import java.util.Arrays;
import java.util.Random;




public class test {

	public static String generateString(int length) {
		Random rng = new Random();
		String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] text = new char[rng.nextInt(length) + 1];
		for (int i = 0; i < text.length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	public static int generateIntger(int length) {
		Random rng = new Random();
		return rng.nextInt(length);
	}

	public static void rehashingTest(HashTable<Integer, String> hashTable,
			int hashNum) {
	
		int numOfKeys = 100000;
		
		int[] keys = new int[numOfKeys];
		for (int i = 0; i < numOfKeys; i++) {
			int key = generateIntger(1999999999);
			String value = generateString(20);
			hashTable.put(key, value);
			keys[i] = key;
		}

		
		System.out.println("Number of elements = " + hashTable.size()
				+ hashTable.toString() + "\nMemory Used = " + getMemoryUsed()
				+ " MB");
		String collisions[] = hashTable.toString().split(" ");
		Arrays.toString(collisions);
		collision[hashNum] += Integer
				.parseInt(collisions[collisions.length - 1]);
	
	}


	public static double getMemoryUsed() {
		runtime.gc();
		double memory = runtime.totalMemory() - runtime.freeMemory();
		memory /= 1024;
		memory /= 1024;
		return memory;
	}

	public static Runtime runtime;
	public static int[] collision;

	public static void main(String[] args) {
		runtime = Runtime.getRuntime();
		collision = new int[6];
		int numOfTests = 5;
		for (int i = 0; i < numOfTests; i++) {
			System.out.println("\nTEST #" + i + ":");
			

			System.out.println("\nBucketing HashTable Test");
			HashTable<Integer, String> hashTable = new BucketingHashTable<Integer, String>();
			// hashTableTest(bHashTable);
			rehashingTest(hashTable, 1);

			System.out.println("\nLinear Probing HashTable Test");
                        long startTime = System.currentTimeMillis();
                        hashTable = new LinearProbingHashTable<Integer, String>();
			
			rehashingTest(hashTable, 2);
                        long endTime = System.currentTimeMillis();
                        long executionTime = System.currentTimeMillis();
                        System.out.println("Total Time taken : " +executionTime + "ms");
                        
                        
                        
                        
			System.out.println("\nQuadratic Probing HashTable Test");
			long startTime1 = System.currentTimeMillis();
                        hashTable = new QuadraticProbingHashTable<Integer, String>();
			
			rehashingTest(hashTable, 3);
                           long endTime1 = System.currentTimeMillis();
                        long executionTime1 = endTime1-startTime1;
                        System.out.println("Total Time taken : " +executionTime1 + "ms");

			
			System.out.println("\nDouble Hashing HashTable Test");
                        long startTime2 = System.currentTimeMillis();
			hashTable = new DoubleHashingHashTable<Integer, String>();
			
			rehashingTest(hashTable, 4);
                            long endTime2 = System.currentTimeMillis();
                        long executionTime2 = endTime2-startTime2;
                        System.out.println("Total Time taken : " +executionTime2 + "ms");
		}
		for (int i = 0; i < collision.length; i++) {
			collision[i] /= numOfTests;
		}
		System.out.println();
		System.out.println(Arrays.toString(collision));
	}
}
