/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   NotSavedSubEntityException.java                    :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/15 19:11:52 by Younes            #+#    #+#             */
/*   Updated: 2025/05/15 19:11:53 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.exceptions;

public class NotSavedSubEntityException extends RuntimeException{
    public NotSavedSubEntityException(String message) {
        super(message);
    }
}
