/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/12 11:13:32 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/15 14:44:15 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {

	public static void main(String[] args) {
		
		try {
			UsersList usersList = new UsersArrayList();
			TransactionsList transactionsList = new TransactionsLinkedList();
	
			usersList.addUser(new User("younes", 100));
			usersList.addUser(new User("ilyass", 200));
			usersList.addUser(new User("asma", 300));
			usersList.addUser(new User("maryem", 400));
	
			Transaction transaction1 = new Transaction(usersList.getUserById(1), usersList.getUserById(2), 300);
			Transaction transaction2 = new Transaction(usersList.getUserById(3), usersList.getUserById(0), -20);
			Transaction transaction3 = new Transaction(usersList.getUserById(2), usersList.getUserById(3), 150);
	
			transactionsList.addTransaction(transaction1);
			transactionsList.addTransaction(transaction2);
			transactionsList.addTransaction(transaction3);
	
			Transaction[] arrTransactions = transactionsList.toArray();
	
			for (Transaction tr : arrTransactions) {
				System.out.println(tr);
			}
			
			transactionsList.removeTransactionById(transaction2.getIdentifier());
			System.out.println();
			
			arrTransactions = transactionsList.toArray();
			for (Transaction tr : arrTransactions) {
				System.out.println(tr);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}