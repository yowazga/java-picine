/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/12 11:13:32 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/19 17:20:03 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */


public class Program {

	public static void main(String[] args) {
		
		try {
			Menu menu = new Menu(true);
			menu.displayMenu();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}