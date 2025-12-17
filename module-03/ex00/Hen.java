/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Hen.java                                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/12/16 15:07:24 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/16 15:07:25 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Hen implements Runnable {
	private Integer count;

	public Hen(Integer number) {this.count = number;}
	
	@Override
	public void run() {
		for (int i = 0; i < this.count; i++)
			System.out.println("Hen");
	}
}
