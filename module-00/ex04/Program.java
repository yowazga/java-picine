/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/03 13:28:34 by yowazga           #+#    #+#             */
/*   Updated: 2025/08/09 13:16:06 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.Scanner;

public class Program {

	private static Scanner scanner = new Scanner(System.in);
	private static final int MAX_CHARS = 65535;

	private static short[] getFrequency(String input) {
		short[] countChars = new short[MAX_CHARS];
		char[] charArray = input.toCharArray();

		for (int i = 0; i < charArray.length; i++) {
			countChars[charArray[i]]++;
		}

		return countChars;
	}

	private static char[][] getTopTen(short[] countChars) {
		char[][] topTen = new char[2][10];

		for (int i = 0; i < MAX_CHARS; i++) {
			if (countChars[i] > topTen[1][9]) {
				topTen[0][9] = (char )i;
				topTen[1][9] = (char)countChars[i];
				for (int j = 8; j >= 0; j--) {
					if (topTen[1][j] < topTen[1][j + 1]) {
						char tempChar = topTen[0][j + 1];
						char tempCounter = topTen[1][j + 1];
						topTen[0][j + 1] = topTen[0][j];
						topTen[1][j + 1] = topTen[1][j];
						topTen[0][j] = tempChar;
						topTen[1][j] = tempCounter;
					} else {
						break ;
					}
				}
			}
		}
		return topTen;
	}

	private static void printRow(char[] row, int i) {
		for (int j = 0; j < 10; j++) {
			if (row[j] * 10 / row[0] == i) {
				System.out.printf("%3d ", (int)row[j]);
			} else if (row[j] * 10 / row[0] > i) {
				System.out.print("  # ");
			} else {
				System.out.print("    ");
			}
		}
	}

	private static void printGraph(char[][] topTen) {
		for (int i = 10; i >= 0; i--) {
			printRow(topTen[1], i);
			System.out.println();
		}
		for (int i = 0; i < 10; i++) {
			System.out.printf("%3c ", (char )topTen[0][i]);
		}
	}
	
	public static void main(String[] args) {
		System.out.print("-> ");
		String input = scanner.nextLine();
		
		short[] countChars = getFrequency(input);
		char[][] topTen = getTopTen(countChars);
		printGraph(topTen);
		
		scanner.close();
	}
}