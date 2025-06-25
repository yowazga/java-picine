/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   PrinterWithPrefixImpl.java                         :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/25 17:08:46 by Younes            #+#    #+#             */
/*   Updated: 2025/06/25 17:11:33 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer;

import fr.school42.renderer.Renderer;

public class PrinterWithPrefixImpl implements Printer {

    private final Renderer renderer;
    private String prefix;

    public PrinterWithPrefixImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void print(String message) {
        
        renderer.render(prefix + " " + message);
    }
}
