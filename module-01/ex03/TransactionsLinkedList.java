/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   TransactionsLinkedList.java                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/12 11:14:10 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/15 11:55:26 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class TransactionsLinkedList implements TransactionsList {
	
	private class Node {
		
		private Transaction transaction;
		private Node next;
		private Node prev;
		
		public Node(Transaction transaction) {
			this.transaction = transaction;
			this.next = null;
			this.prev = null;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public void setPrev(Node prev) {
			this.prev = prev;
		}

		public Node getNext() {
			return this.next;
		}

		public Node getPrev() {
			return this.prev;
		}

		public Transaction getTransaction() {
			return this.transaction;
		}
	}

	private Node head;
	private Node tail;
	private Integer size;

	public TransactionsLinkedList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	@Override
	public void addTransaction(Transaction transaction) {
		if (transaction == null) {
			throw new NullPointerException("can't add transaction");
		}
		Node newNode = new Node(transaction);
		if (this.head == null) {
			this.head = newNode;
			this.tail = newNode;
		} else {
			this.tail.setNext(newNode);
			newNode.setPrev(this.tail);
			this.tail = newNode;
		}
		this.size++;
	}

	@Override
	public void removeTransactionById(String idenifier) {
		
		Node curent = this.head;
		while (curent != null) {
			if (curent.getTransaction().getIdentifier().equals(idenifier)) {
				
				if (curent.getPrev() != null) {
					curent.getPrev().setNext(curent.getNext());
				} else {
					this.head = curent.getNext();
				}
				if (curent.getNext() != null) {
					curent.getNext().setPrev(curent.getPrev());
				} else {
					this.tail = curent.getPrev();
				}
				this.size--;
				return ;
			}
			curent = curent.getNext();
		}
		throw new TransactionNotFoundException();
	}

	@Override
	public Transaction[] toArray() {
		
		Transaction[] transactions = new Transaction[this.size];
		Node curent = this.head;
		for (int i = 0; i < this.size; i++) {
			transactions[i] = curent.getTransaction();
			curent = curent.getNext();
		}
		return transactions;
	}

	public class TransactionNotFoundException extends RuntimeException {
		public TransactionNotFoundException() {
			super("transaction not found.");
		}
	}
	
}