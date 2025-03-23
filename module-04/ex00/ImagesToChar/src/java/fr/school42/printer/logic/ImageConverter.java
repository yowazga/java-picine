/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ImageConverter.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/22 16:31:10 by yowazga           #+#    #+#             */
/*   Updated: 2025/03/23 15:15:18 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer.logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageConverter {

	private char BLACK;
	private char WHITE;

	public ImageConverter(char black, char white) {
		this.BLACK = black;
		this.WHITE = white;
	}

	private void validateImageFile(String filePath) {
		
		File file = new File(filePath);
		if (!file.exists() || file.canRead()) {
			throw new IllegalArgumentException("The image file does not exist or cannot be read.");
		}
	}
	
	public char[][] convertImage(String imagePath) {
		
		
		File file = new File(imagePath);
		
		try {
			
			BufferedImage image = ImageIO.read(file);
			if (image == null)
				throw new IllegalArgumentException("Failed to read the image file.");
			char[][] arrayChar = new char[image.getWidth()][image.getHeight()];
			
			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					
					int rgb = image.getRGB(x, y);
					Color color = new Color(rgb);

					int grayscale = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
					
					arrayChar[x][y] = grayscale < 128 ? this.BLACK : this.WHITE;
				}
			}
			return arrayChar;
			
		} catch (Exception e) {
			System.err.println("Error Opning image: " + e.getMessage());
			return null;
		}
	}
}
