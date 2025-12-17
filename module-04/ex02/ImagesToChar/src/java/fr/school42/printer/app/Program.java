/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/22 16:27:43 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/17 14:33:26 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.diogonunes.jcolor.Attribute;
import com.beust.jcommander.ParameterException;
import com.diogonunes.jcolor.Ansi;

import fr.school42.printer.logic.ColorConverter;
import fr.school42.printer.logic.ImageConverter;
import fr.school42.printer.logic.ResourceNameValidator;

@Parameters(separators = "=")
public class Program {
	
	@Parameter(names = {"-w", "--white"}, description = "White color replacement, Values: BLACK,RED,GREEN,YELLOW,BLUE,MAGENTA,CYAN,WHITE", converter = ColorConverter.class)
	private Attribute whiteColor = Attribute.WHITE_BACK();
	
	@Parameter(names = {"-b", "--black"}, description = "black color replacement, Values: BLACK,RED,GREEN,YELLOW,BLUE,MAGENTA,CYAN,WHITE", converter = ColorConverter.class)
	private Attribute blackColor = Attribute.BLACK_BACK();

	@Parameter(names = {"-r", "--resource"}, description = "Resource name, (should be in resources folder)", order = 1, validateWith = ResourceNameValidator.class)
	private String resourceName = "resources/image.bmp";

	public static JCommander jcommander;

	public void run() {
		
		ImageConverter converter = new ImageConverter(resourceName, 'W', 'B');
		char[][] imageData = converter.convertImage();

		if (imageData != null) {
			for (int y = 0; y < imageData.length; y++) {
				for (int x = 0; x < imageData[y].length; x++) {
					System.out.print(imageData[y][x] == 'W' ? Ansi.colorize("  ", whiteColor) : Ansi.colorize("  ", blackColor));
				}
				System.out.println();
			}
		}
	}
	public static void main(String[] args) {
		
		try {
			
			Program program = new Program();
			
			jcommander = JCommander.newBuilder().addObject(program).build();
			
			jcommander.setProgramName("images-to-chars-printer");
			
			jcommander.parse(args);
			
			program.run();
			
		} catch (Exception e) {
			
			System.err.println("Error: " + e.getMessage());
		}
	}
}