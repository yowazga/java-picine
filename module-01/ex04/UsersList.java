/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersList.java                                     :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 12:08:02 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/11 14:58:08 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

/**
 * UsersList
 */
public interface UsersList {

	public void addUser(User user);
	
	public User getUserById(Integer id);

	public User getUserByIndex(Integer index);
	
	public Integer getNumberOfUsers();
}