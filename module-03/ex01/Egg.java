/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Egg.java                                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/02/25 16:02:09 by yowazga           #+#    #+#             */
/*   Updated: 2025/02/25 16:43:46 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Egg extends Thread
{
	private Integer count;

	public Egg(Integer number) {this.count = number;}

	@Override
	public void run() {
		for (int i = 0; i < this.count; i++)
			System.out.println("Egg");
	}
	
}
