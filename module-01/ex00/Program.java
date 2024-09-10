/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/10 07:32:50 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/10 19:28:27 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */


public class Program {

	public static void main(String[] args) {
		
		User user1 = new User("younes", 1000);
		User user2 = new User("ali", 500);

		Transaction t1 = new Transaction(user1, user2, -200);
		Transaction t2 = new Transaction(user2, user1, 200);

		System.out.println(t1);
		System.out.println(t2);
	}
}