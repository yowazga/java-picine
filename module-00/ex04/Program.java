/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/08/31 13:35:48 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/02 20:38:52 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.Scanner;

public class Program {

	private static final int MAX_CHAR = 65535;
	private static Scanner scanner = new Scanner(System.in);

	private static short[] countCharacters(String input)
	{
		short [] charsCount = new short[MAX_CHAR];
		char[] inputChars = input.toCharArray();
		
		for (int i = 0; i < inputChars.length; i++) {
			charsCount[inputChars[i]]++;
		}
		return charsCount;
	}

	private static char[][] topTen(short[] charsCount)
	{
		char[][] top10 = new char[2][10];
		
		for (int i = 0; i < MAX_CHAR; i++){
			if (charsCount[i] > top10[1][9]){
				top10[0][9] = (char )i;
				top10[1][9] = (char )charsCount[i];
				for (int j = 8; j >= 0; j--) {
					if (top10[1][j] < top10[1][j + 1]) {
						char tempChar = top10[0][j];
						char tempCount = top10[1][j];
						top10[0][j] = top10[0][j + 1];
						top10[1][j] = top10[1][j + 1];
						top10[0][j + 1] = tempChar;
						top10[1][j + 1] = tempCount;
					} else {
						break ;
					}
				}
			}
		}
		return top10;
	}

	public static void putCharp(int i, char[] topTenCount)
	{
		for (int j = 0; j < 10; j++) {
			if (topTenCount[j] * 10 / topTenCount[0] == i) {
				System.out.printf("%3d ", (int) topTenCount[j]);
			} else if (topTenCount[j] * 10 / topTenCount[0] > i) {
				System.out.print("  # ");
			} else {
				System.out.print("    ");
			}
		}
		System.out.println();
	}

	private static void printGraph(char[][] topTen)
	{
		for (int i = 10; i >= 0; i--) {
			putCharp(i, topTen[1]);
		}
		for (int i = 0; i < 10; i++) {
			System.out.printf("%3c ", topTen[0][i]);
		}
	}
	
	public static void main(String[] args) 
	{
		String input = scanner.nextLine();

		short[] charsCount = countCharacters(input);
		char[][] topten = topTen(charsCount);
		printGraph(topten);

		scanner.close();
	}
}