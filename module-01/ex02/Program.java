/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 11:48:31 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/15 11:43:59 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {

	public static void main(String[] args) {
		
		UsersList users = new UsersArrayList();

		for (int i = 0; i < 11; i++) {
			users.addUser(new User(String.format("user_%s", i), 100 + i));
		}

		// System.out.println(users.getUserById(12));
		
		System.out.println(users.getUserById(5));
		
		System.out.println(users.getNumberOfUsers());
		
	}
}