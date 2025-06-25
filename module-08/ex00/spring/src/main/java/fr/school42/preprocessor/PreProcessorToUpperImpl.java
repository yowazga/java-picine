/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   PreProcessorToUpperImpl.java                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/25 16:31:12 by Younes            #+#    #+#             */
/*   Updated: 2025/06/25 16:31:54 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.preprocessor;

public class PreProcessorToUpperImpl implements PreProcessor {

    @Override
    public String prossece(String message) {
        
        return message.toUpperCase();
    }

}
