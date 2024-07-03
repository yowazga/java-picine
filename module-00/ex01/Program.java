/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/06/30 18:40:57 by yowazga           #+#    #+#             */
/*   Updated: 2024/07/02 16:48:40 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

// package ex01;

import java.util.Scanner; //import the scanner class


public class Program {

	public int iterations = 0;
	
	boolean isPrime(int num)
	{
		if (num == 2 || num == 3)
		{
			iterations++;
			return true;
		}
		iterations++;
		if (num % 2 == 0 || num % 3 == 0)
			return false;
		for (int i = 5; i * i <= num; i += 6)
		{
			iterations++;
			if (num % i == 0 || num % (i + 2) ==0)
				return false;
		}
		return true;
	}
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Inter a number");

	
		int num = input.nextInt();
		if (num <= 1)
		{
			System.err.println("IllegalArgument");
			System.exit(-1);
		}
		Program program = new Program();
		if (program.isPrime(num))
			System.out.println("true " + program.iterations);
		else
			System.out.println("false " + program.iterations);
		input.close();
	}
}
