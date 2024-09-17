/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersArrayList.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 12:16:31 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/12 09:05:11 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class UsersArrayList implements UsersList {

	User users[];
	Integer sizeUsers;

	public UsersArrayList() {
		this.users = new User[10];
		this.sizeUsers = 0;
	}

	@Override
	public void addUser(User user) throws NullPointerException {
		
		if (user.equals(null)) {
			throw new NullPointerException();
		}
		if (sizeUsers.equals(users.length)) {

			int newSize = users.length + (users.length / 2);
			User newUsers[] = new User[newSize];
			for (int i = 0; i < this.sizeUsers; i++) {
				newUsers[i] = users[i];
			}
			users = newUsers;
		}
		users[this.sizeUsers++] = user;
	}
	
	@Override
	public User getUserById(Integer id) throws UserNotFoundException {

		if (id < 0 || id >= this.sizeUsers) {
			throw new UserNotFoundException();
		}
		for (int i = 0; i < this.sizeUsers; i++) {
			if (users[i].getId().equals(id)) {
				return users[i];
			}
		}
		throw new UserNotFoundException();
	}

	@Override
	public User getUserByIndex(Integer index) throws UserNotFoundException, ArrayIndexOutOfBoundsException {

		if (index < 0 || index > users.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (users[index].equals(null)) {
			throw new UserNotFoundException();
		}
		return users[index];
	}
	
	@Override
	public Integer getNumberOfUsers() {
		
		return this.sizeUsers;
	}

	public class UserNotFoundException extends RuntimeException  {
		public UserNotFoundException() {
			super("User not found.");
		}
	}
}


