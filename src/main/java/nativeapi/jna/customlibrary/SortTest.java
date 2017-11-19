package nativeapi.jna.customlibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import static nativeapi.jna.customlibrary.CustomLibraryJNATest.getLibPath;

public class SortTest {

	interface LibSort extends Library {

		LibSort INSTANCE = (LibSort) Native.loadLibrary(getLibPath() + "/libsort.so", LibSort.class);

		void sort(int[] arr, int size);

	}

	public static void main(String[] args) throws IOException {

		final int size = 50127;
		final int[] myArr = new int[size];
		final Scanner stream = new Scanner(Class.class.getResourceAsStream("/numbers.txt"));
		for(int i = 0; i < size; i++){
			myArr[i] = stream.nextInt();
		}

		final int[] myArrCopy = Arrays.copyOf(myArr, myArr.length);
		long cStartTime, cEndTime, javaStartTime, javaEndTime;

		cStartTime = System.currentTimeMillis();
		LibSort.INSTANCE.sort(myArrCopy, myArrCopy.length);
		cEndTime = System.currentTimeMillis();
		System.out.println("c sort");

		javaStartTime = System.currentTimeMillis();
		sort(myArr);
		javaEndTime = System.currentTimeMillis();
		System.out.println("JAVA sort");

		System.out.printf("java time=%d, c time=%d", (javaEndTime - javaStartTime) , (cEndTime - cStartTime) );

	}

	private static void sort(int[] arr){
		for(int i=0; i < arr.length; i++){
			for(int j=i; j < arr.length; j++){
				if(arr[i] > arr[j]){
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
	}

}
