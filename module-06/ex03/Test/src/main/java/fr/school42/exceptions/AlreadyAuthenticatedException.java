/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   AlreadyAuthenticatedException.java                 :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/12 18:34:35 by Younes            #+#    #+#             */
/*   Updated: 2025/06/12 18:42:03 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.exceptions;

public class AlreadyAuthenticatedException extends RuntimeException {

    public AlreadyAuthenticatedException() {
        super("User is already authenticated");
    }
    
    public AlreadyAuthenticatedException(String message) {
        super(message);
    }
}
