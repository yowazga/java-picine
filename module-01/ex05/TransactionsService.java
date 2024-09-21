/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   TransactionsService.java                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/15 20:52:52 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/19 19:56:23 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.UUID;

public class TransactionsService {

	private UsersList usersList;
	private TransactionsList unpairedTransactions;

	public TransactionsService() {
		
		this.usersList = new UsersArrayList();
		this.unpairedTransactions = new TransactionsLinkedList();
	}

	public UsersList getUsersList() {
		return this.usersList;
	}
	
	public void addUser(User user) {
		
		usersList.addUser(user);
	}

	public Integer getUserBalance(Integer userId) {
		
		return usersList.getUserById(userId).getBalance();
	}

	public void performTransfer(int senderId, int recipientId, int amount) {
		
		User sender = usersList.getUserById(senderId);
		User recipient = usersList.getUserById(recipientId);

		if (amount < 0)
			amount = -amount;
		if (sender.getBalance() < amount) {
			throw new IllegalTransactionException();
		}

		String identifier = UUID.randomUUID().toString();

		Transaction tr1 = new Transaction(sender, recipient, amount, Transaction.TransferCategory.DEBITS, identifier);
		Transaction tr2 = new Transaction(sender, recipient, amount, Transaction.TransferCategory.CREDITS, identifier);

		sender.setBalance(sender.getBalance() - amount);
		recipient.setBalance(recipient.getBalance() + amount);

		sender.getTransactionsList().addTransaction(tr1);
		recipient.getTransactionsList().addTransaction(tr2);
		
	}

	public Transaction[] getUserTransactions(Integer userId) {
		
		User user = this.usersList.getUserById(userId);
		return user.getTransactionsList().toArray();
	}

	public void removeTransaction(Integer userId, String transactionId) {
		
		User user1 = usersList.getUserById(userId);
		Transaction trToDelete = user1.getTransactionsList().getTransactionById(transactionId);

		User user2 = (userId.equals(trToDelete.getRecipient().getId()))
								? this.usersList.getUserById(trToDelete.getSender().getId())
								: this.usersList.getUserById(trToDelete.getRecipient().getId());
		
		if (this.unpairedTransactions.getTransactionById(transactionId) != null) {
		System.out.println("check here");

			this.unpairedTransactions.removeTransactionById(transactionId);
		} else {
			Transaction secondTransaction = user2.getTransactionsList().getTransactionById(transactionId);
			if (secondTransaction != null) {
				this.unpairedTransactions.addTransaction(secondTransaction);
			}
		}
		user1.getTransactionsList().removeTransactionById(transactionId);
	}

	public Transaction[] getUnpairedTransactions() {
		return this.unpairedTransactions.toArray();
	}

	public class IllegalTransactionException extends RuntimeException {
		public IllegalTransactionException() {
			super("Insufficient funds.");
		}
	}
}