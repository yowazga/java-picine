/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   User.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/16 14:19:59 by Younes            #+#    #+#             */
/*   Updated: 2025/06/16 14:24:07 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.classes;

import java.util.StringJoiner;

public class User {

    private String firstName;
    private String lastName;
    private int height;

    public User() {}

    public User(String firstName, String lastName, int height) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
    }

    public int grow(int value) {
        
        this.height += value;
        return this.height;
    }

    @Override
    public String toString() {
        
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
            .add("firstName='" + firstName + "'")
            .add("lastName='" + lastName + "'")
            .add("height=" + height)
            .toString();
    }
}
