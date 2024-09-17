/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/12 11:13:32 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/17 13:16:57 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {

	public static void main(String[] args) {
		
		try {
			TransactionsService transactionsService = new TransactionsService();

			transactionsService.addUser("younes", 100);
			transactionsService.addUser("ilyass", 200);
			transactionsService.addUser("asma", 300);

			transactionsService.performTransfer(0, 1, 50);

			System.out.println(transactionsService.getUserBalance(0));
			System.out.println(transactionsService.getUserBalance(1));
			
			System.out.println("======================================================");

			transactionsService.performTransfer(1, 2, 100);

			System.out.println(transactionsService.getUserBalance(1));
			System.out.println(transactionsService.getUserBalance(2));
			
			System.out.println("======================================================");

			for (Transaction transaction : transactionsService.getUserTransactions(0)) {
				System.out.println(transaction);
			}

			System.out.println("======================================================");
			
			for (Transaction transaction : transactionsService.getUserTransactions(1)) {
				System.out.println(transaction);
			}

			System.out.println("======================================================");
			
			for (Transaction transaction : transactionsService.getUserTransactions(2)) {
				System.out.println(transaction);
			}

			System.out.println("======================================================");

			System.out.println();

			transactionsService.removeTransaction(1, transactionsService.getUserTransactions(1)[0].getIdentifier());

			for (Transaction transaction : transactionsService.getUnpairedTransactions()) {
				System.out.println(transaction);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}