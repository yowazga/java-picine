/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/02/26 17:28:13 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/17 11:25:25 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.*;

public class Program {

	private static int arraySum(int[] array) {
		int sum = 0;
		for (int elem : array) {
			sum += elem;
		}
		return sum;
	}

	private static int[] generateArray(int sizeOfArray) {
		int[] array = new int[sizeOfArray];
		Random random = new Random();

		for (int i = 0; i < sizeOfArray; i++) {
			array[i] = random.nextInt(1000);
		}

		return array;
	}

	private static int getArg(String[] args, String prefix) {

		try {
			for (int i = 0; i < args.length; i ++) {
				if (args[i].startsWith(prefix)) {
					int value = Integer.parseInt(args[i].split("=")[1]);
					if (value <= 0)
						throw new NumberFormatException("Number must be positive");
					return value;
				}
			}
		}
		catch (Exception e) {
			System.err.println("Wrong argument: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return 0;
	}
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.err.println("Wrong number of arguments. Expected 2 arguments: --arraySize=<size> --threadsCount=<count>");
			System.exit(-1);
		}
		int arraySize = getArg(args, "--arraySize");
		int threadCount = getArg(args, "--threadsCount");
		if (arraySize > 2000000 || threadCount > arraySize) {
			System.err.println("Thread count must be less than or equal to array size.");
			System.exit(-1);
		}

		int[] array = generateArray(arraySize);
		System.out.println("sum: " + arraySum(array));
		int sectionNumber = arraySize / threadCount;
		int rest = arraySize % threadCount;

		SumThread[] threads = new SumThread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			int start = i * sectionNumber;
			int end = start + sectionNumber;
			if (i == threadCount - 1)
				end += rest;
			threads[i] = new SumThread(array, start, end);
			threads[i].start();
		}
		
		int totalSum = 0;
		for (SumThread thread : threads) {
			try {
				thread.join();
				totalSum += thread.getSum();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Sum by threads: " + totalSum);
	}
}
