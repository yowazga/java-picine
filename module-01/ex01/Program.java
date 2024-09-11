/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 10:26:53 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/11 11:31:03 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {

	public static void main(String[] args) {
		User user1 = new User("younes", 300);
		User user2 = new User("ilyass", 500);
		User user3 = new User("asma", 350);

		System.err.println(user1);
		System.err.println(user2);
		System.err.println(user3);
	}
}