/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Transaction.java                                   :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/10 07:33:03 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/11 10:16:08 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.UUID;

public class Transaction {

	public enum TransferCategory {
		DEBITS("Outcome"),
		CREDITS("Income");

		private String TEXT;
		
		TransferCategory(String text) {
			TEXT = text;
		}

		public String getText() {
			return TEXT;
		}
	}
	
	private String identifier;
	private User recipient;
	private User sender;
	private TransferCategory category;
	private Integer amount;

	public Transaction(User sender, User recipient, Integer amount) {
		this.identifier = UUID.randomUUID().toString();
		this.recipient = recipient;
		this.sender = sender;
		setCategory(amount);
		this.amount = amount;
	}

	public void setCategory(Integer amount) {
		if (amount < 0) {
			this.category = TransferCategory.DEBITS;
		} else {
			this.category = TransferCategory.CREDITS;
		}
	}

	public String getIdentifier() {
		return this.identifier;
	}
	
	public User getRecipient() {
		return this.recipient;
	}

	public User getSender() {
		return this.sender;
	}

	public TransferCategory getCategory() {
		return this.category;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public String toString() {
		return String.format("Transaction [Identifier: %s, Recipient: %s, Sender: %s, Category: %s, Amount: %s]", getIdentifier(), getRecipient().getName(), getSender().getName(), getCategory().getText(), getAmount());
	}
}