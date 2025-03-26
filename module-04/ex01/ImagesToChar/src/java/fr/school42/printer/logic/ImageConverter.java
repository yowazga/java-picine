/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ImageConverter.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/22 16:31:10 by yowazga           #+#    #+#             */
/*   Updated: 2025/03/26 15:05:03 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer.logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageConverter {

	private char BLACK;
	private char WHITE;

	public ImageConverter(char black, char white) {
		this.BLACK = black;
		this.WHITE = white;
	}
	
	public char[][] convertImage() {
		
		
		InputStream inputStream =  getClass().getClassLoader().getResourceAsStream("image.bmp");
		if (inputStream == null)
			throw new IllegalArgumentException("Resource 'image.bmp' not found.");
		
		try {
			
			BufferedImage image = ImageIO.read(inputStream);
			if (image == null)
				throw new IllegalArgumentException("Failed to read the image.");
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
