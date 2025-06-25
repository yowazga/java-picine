/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   PrinterWithDateTimeImpl.java                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/25 17:12:58 by Younes            #+#    #+#             */
/*   Updated: 2025/06/25 17:14:43 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer;

import java.time.LocalDateTime;

import fr.school42.renderer.Renderer;

public class PrinterWithDateTimeImpl implements Printer {

    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void print(String message) {
        
        renderer.render(LocalDateTime.now() + " " + message);
    }
}
