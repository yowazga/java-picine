/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ImageConverter.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/22 16:31:10 by yowazga           #+#    #+#             */
/*   Updated: 2025/04/07 12:55:15 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer.logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageConverter {

	private String resourceName;

	private char BLACK;
	private char WHITE;

	public ImageConverter(String resourceName, char black, char white) {
		this.resourceName = resourceName;
		this.BLACK = black;
		this.WHITE = white;
	}
	
	public char[][] convertImage() {
		try {
			// Try loading from resources first
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
			
			// If not found in resources, try as a file
			if (inputStream == null) {
				File file = new File(resourceName);
				if (file.exists()) {
					inputStream = file.toURI().toURL().openStream();
				} else {
					throw new IllegalArgumentException("Resource " + resourceName + " not found in classpath or as file.");
				}
			}
			
			BufferedImage image = ImageIO.read(inputStream);
			if (image == null) {
				throw new IllegalArgumentException("Failed to read the image. Make sure it's a valid BMP file.");
			}
			
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
			System.err.println("Error processing image: " + e.getMessage());
			return null;
		}
	}
}
