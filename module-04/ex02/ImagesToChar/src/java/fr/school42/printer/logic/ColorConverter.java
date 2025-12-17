/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ColorConverter.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/04/05 18:44:49 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/17 14:24:41 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer.logic;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import com.diogonunes.jcolor.Attribute;

/**
 * Converts string color names to JColor Attribute objects.
 * Used by JCommander to parse command line color arguments.
 */
public class ColorConverter implements IStringConverter<Attribute> {
	
	/**
	 * Converts a string color name to its corresponding JColor Attribute.
	 * 
	 * @param value The color name to convert (e.g., "RED", "BLUE")
	 * @return The corresponding JColor Attribute
	 * @throws ParameterException If the color name is invalid
	 */
	@Override
	public Attribute convert(String value) {
		switch (value) {
			case "BLACK":
				return Attribute.BLACK_BACK();
			case "RED":
				return Attribute.RED_BACK();
			case "GREEN":
				return Attribute.GREEN_BACK();
			case "YELLOW":
				return Attribute.YELLOW_BACK();
			case "BLUE":
				return Attribute.BLUE_BACK();
			case "MAGENTA":
				return Attribute.MAGENTA_BACK();
			case "CYAN":
				return Attribute.CYAN_BACK();
			case "WHITE":
				return Attribute.WHITE_BACK();
			default:
				throw new ParameterException("Invalid color string value: " + value);
		}
	}
}
