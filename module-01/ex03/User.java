/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   User.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 10:27:19 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/14 19:05:13 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class User {
	
	private Integer id;
	private String name;
	private Integer balance;
	private TransactionsList transactionsList;

	public User(String name, Integer balance) {
		this.id = UserIdsGenerator.getInstance().generateId();
		this.name = name;
		setBalance(balance);
		this.transactionsList = new TransactionsLinkedList();
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

	public TransactionsList getTransactionsList() {
		return this.transactionsList;
	}

	@Override 
	public String toString() {
		return "User [ID: "
			+ getId() + ", Name: "
			+ getName() + ", Balance: "
			+ getBalance() + "].";
	}
}