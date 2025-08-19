/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UserForm.java                                      :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/21 17:12:13 by Younes            #+#    #+#             */
/*   Updated: 2025/06/29 17:58:32 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.form;

import fr.school42.annotations.HtmlForm;
import fr.school42.annotations.HtmlInput;

@HtmlForm(fileName = "user_form.html", action = "/users", method = "post")
public class UserForm {

    @HtmlInput(type = "text", name = "first_name", placeholder = "Enter First Name")
    private String firstName;

    @HtmlInput(type = "text", name = "last_name", placeholder = "Enter Last Name")
    private String lastName;

    @HtmlInput(type = "password", name = "password", placeholder = "Enter Password")
    private String password;
}
