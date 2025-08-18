/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/07/01 19:05:14 by yowazga           #+#    #+#             */
/*   Updated: 2025/08/07 19:05:43 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

// package module-00.ex02;
import java.util.Scanner;

public class Program {
	boolean isPrime(int num)
	{
		if (num == 2 || num == 3)
			return true;
		if (num % 2 == 0 || num % 3 == 0)
			return false;
		for (int i = 5; i * i <= num; i += 6)
		{
			if (num % i == 0 || num % (i + 2) == 0)
				return false;
		}
		return true;
	}

	int sumOfDigits(int number)
	{
		int sum = 0;

		while (number != 0)
		{
			sum += number % 10;
			number /= 10;
		}
		return sum;
	}
	
	public static void main(String[] args) {

		Program program = new Program();
		Scanner input = new Scanner(System.in);
		int count = 0;
		int num = 0; 

		while (true) {
			System.out.print("-> ");
			num = input.nextInt();
			if (num == 42)
				break ;
			if (program.isPrime(program.sumOfDigits(num)))
				count++;
		}
		System.out.println("Count of coffee-request : " + count);
		input.close();
	}
}
