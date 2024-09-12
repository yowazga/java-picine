/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   User.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 11:48:23 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/11 11:48:27 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class User {
	
	private Integer id;
	private String name;
	private Integer balance;

	public User(String name, Integer balance) {
		this.id = UserIdsGenerator.getInstance().generateId();
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