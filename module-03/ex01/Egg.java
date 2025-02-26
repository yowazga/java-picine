/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Egg.java                                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/02/25 16:02:09 by yowazga           #+#    #+#             */
/*   Updated: 2025/02/26 16:45:54 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Egg extends Thread {

	private LockObject lock;

	public Egg(LockObject lock) {this.lock = lock;}

	@Override
	public void run() {
		synchronized (lock) {
			for (int i = 0; i < this.lock.getNumberOfInswers(); i++) {
				while (!this.lock.turn){
					try { lock.wait(); } catch (Exception e) {}
				}
				System.out.println("Egg");
				this.lock.turn = false;
				lock.notify();
			}
		}
	}
}
