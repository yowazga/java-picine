/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/22 16:27:43 by yowazga           #+#    #+#             */
/*   Updated: 2025/03/23 13:35:15 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer.app;

import fr.school42.printer.logic.ImageConverter;

public class Program {

	private final static char DEFAULT_BLACK = '0';
	private final static char DEFAULT_WHITE = '.';


	private static char getColor(String[] args, int index, char defaultColor) {

		if (args.length == 3) {
			if (args[index].length() != 1 )
			throw new IllegalArgumentException("The color argument must be a single character.");
			
			return args[index].charAt(0);
		}
		return defaultColor;
	}
	
	public static void main(String[] args) {
		
		try {
			char blackColor = getColor(args, 1, DEFAULT_BLACK);
			char whiteColor = getColor(args, 2, DEFAULT_WHITE);
			String imagePath = args[0];
			
			ImageConverter converter = new ImageConverter(blackColor, whiteColor);

			char[][] arrayChar = converter.convertImage(imagePath);

			for (int y = 0; y < arrayChar.length; y++) {
				
				for (int x = 0; x < arrayChar[y].length; x++) {
					
					System.out.print(arrayChar[x][y]);
				}
				System.out.println();
			}
			
		} catch (Exception e) {
			
			System.err.println("Error: " + e.getMessage());;
		}
	}
}