/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Main.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/25 16:28:11 by Younes            #+#    #+#             */
/*   Updated: 2025/06/25 19:18:10 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import fr.school42.printer.Printer;


public class Main 
{
    public static void main( String[] args )
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

        Printer print = context.getBean("printerWithDate", Printer.class);
        print.print("hello");
    }
}
