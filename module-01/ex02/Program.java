/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 11:48:31 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/12 08:25:42 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {

	public static void main(String[] args) {
		
		UsersArrayList users = new UsersArrayList();

		for (int i = 0; i < 11; i++) {
			users.addUser(new User(String.format("younes%s", i), 100 + i));
		}

		// System.out.println(users.getUserById(12));
		
		System.out.println(users.getUserById(9));
		
		System.out.println(users.getNumberOfUsers());
		
	}
}