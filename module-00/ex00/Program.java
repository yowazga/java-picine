/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/06/30 18:21:09 by yowazga           #+#    #+#             */
/*   Updated: 2025/08/07 15:56:35 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */


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