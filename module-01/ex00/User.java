/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   User.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/10 07:32:56 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/10 20:36:47 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class User {
	
	private static Integer lastId = 0; 
	private Integer id;
	private String name;
	private Integer balance;

	public User(String name, Integer balance) {
		this.id = lastId++;
		this.name = name;
		setBalance(balance);
	}

	public void setBalance(Integer balance) {
		this.balance = balance < 0 ? 0 : balance;
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Integer getBalance() {
		return this.balance;
	}

	@Override 
	public String toString() {
		return "User [ID: "
			+ getId() + ", Name: "
			+ getName() + ", Balance: "
			+ getBalance() + "].";
	}
}