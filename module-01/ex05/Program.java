/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/12 11:13:32 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/22 15:31:04 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */


public class Program {

	public static void main(String[] args) {
		
		try {
			boolean isDevMode;
			
			if (args[0].equals("--profile=dev")) {
				isDevMode = true;
			} else if (args[0].equals("--profile=production")) {
				isDevMode = false;
			} else {
				throw new IllegalArgumentException("invalid argument");
			}
		
			Menu menu = new Menu(isDevMode);
			menu.displayMenu();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}