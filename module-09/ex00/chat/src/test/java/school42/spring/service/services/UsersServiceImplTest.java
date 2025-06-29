/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImplTest.java                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/28 16:29:38 by Younes            #+#    #+#             */
/*   Updated: 2025/06/29 12:51:44 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.services;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import school42.spring.service.config.TestApplicationConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
public class UsersServiceImplTest {

    @Autowired
    private UsersService usersService;

    @Test
    public void testSignUpReturnsPassword() {
        String password = usersService.signUp("test@h2.com");

        assertNotNull("Password should not be null", password);
        assertFalse(password.isEmpty(), "Password should not be empty");

        System.out.println("Generated password: " + password);
    }
}
