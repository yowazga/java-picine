/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Greeter.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/30 14:55:06 by yowazga           #+#    #+#             */
/*   Updated: 2025/04/05 17:27:50 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */


import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

import java.util.List;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

class ColorConverter implements IStringConverter<String> {
	
	private static final String[] VALID_COLORS = {"RED", "GREEN", "BLUE", "YELLOW", "PURPLE"};
	
	@Override
	public String convert(String value) {
		
		String upperValue = value.toUpperCase();
		for (String color : VALID_COLORS) {
			if (color.equals(upperValue))
				return upperValue;
		}
		throw new ParameterException("Invalid color. Choose from: RED, GREEN, BLUE, YELLOW, PURPLE");
	}
}

public class Greeter {

	@Parameter(names = {"--names", "-n"},
			   description = "List of names to greet",
			   required = true,
			   variableArity = true)
	private List<String> names;
	
	@Parameter(names = {"--color", "-c"},
			   description = "Text color (RED, GREEN, BLUE, YELLOW, PURPLE)",
			   converter = ColorConverter.class,
			   required = false)
	private String color = "BLUE";

	@Parameter(names = "--help",
			   description = "Show usage help",
			   help = true)
	private boolean help;
	public static void main(String[] args) {
		
		Greeter greeter = new Greeter();
		JCommander jc = JCommander.newBuilder()
		.addObject(greeter)
		.build();

		try {
			jc.parse(args);
			if (greeter.help) {
				jc.usage();
				return;
			}
			String colorCode = switch (greeter.color) {
				case "RED"    -> "\u001B[31m";
				case "GREEN"  -> "\u001B[32m";
				case "BLUE"   -> "\u001B[34m";
				case "YELLOW" -> "\u001B[33m";
				case "PURPLE" -> "\u001B[35m";
				default       -> "\u001B[0m";  // Reset (should never happen due to converter)
			};
			for (String name : greeter.names) {
				
				System.out.println(colorCode + "Hello " + name+ "!\u001B[0m");
			}
			
			
		} catch(ParameterException e) {
			System.err.println("Error: " + e.getMessage());
			jc.usage();
		}

	}
}
