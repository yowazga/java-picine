/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/06/30 18:21:09 by yowazga           #+#    #+#             */
/*   Updated: 2024/07/01 20:27:21 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

// package ex00;

public class Program {
	public static void main(String[] arg)
	{
		int number = 479598;
		int sum = 0;

		sum += number / 100000;
		sum += (number / 10000) % 10;
		sum += (number / 1000) % 10;
		sum += (number / 100) % 10;
		sum += (number / 10) % 10;
		sum += number % 10;
		
		System.out.println(sum);
	}
}