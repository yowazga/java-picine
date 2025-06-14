/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersServiceImplTest.java                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/12 18:09:45 by Younes            #+#    #+#             */
/*   Updated: 2025/06/14 15:42:55 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.school42.exceptions.AlreadyAuthenticatedException;
import fr.school42.exceptions.EntityNotFoundException;
import fr.school42.models.User;
import fr.school42.repositories.UsersRepository;

@ExtendWith(MockitoExtension.class)
public class UsersServiceImplTest {
    
    @Mock
    private UsersRepository usersRepository;
    
    @InjectMocks
    private UsersServiceImpl usersServiceImpl;

    @Test
    void authenticate_Success_WhenCredentialsCorrect() {
         // Arrange
         // 1. Create test user  
        User mockUser = new User(1L, "admin", "pass123", false);
        // 2. Configure mock behavior
        when(usersRepository.findByLogin("admin")).thenReturn(mockUser);

        // Act
        // 3. Execute the authentication
        boolean resault = usersServiceImpl.authenticate("admin", "pass123");

        // Assert
        // 4. Verify the result is true
        assertTrue(resault);

        // 5. Verify the user was updated
        verify(usersRepository).update(mockUser);
    }

    @Test
    void authenticate_Fails_WhenLoginInvalid() {
        
        when(usersRepository.findByLogin("unknown"))
            .thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () -> {
            usersServiceImpl.authenticate("unknown", "anypass");
        });

        verify(usersRepository, never()).update(any());
    }

    @Test
    void authenticate_Fails_WhenPasswordWrong() {

        User mockUser = new User(1L, "admin", "correctPass", false);

        when(usersRepository.findByLogin("admin")).thenReturn(mockUser);

        boolean resault = usersServiceImpl.authenticate("admin", "wrongpass");

        assertFalse(resault);

        verify(usersRepository, never()).update(any());
    }

    @Test
    void authenticate_Throws_WhenUserAlreadyLoggedIn() {

        User mockUser = new User(1L, "admin", "pass123", true);

        when(usersRepository.findByLogin("admin")).thenReturn(mockUser);

        assertThrows(AlreadyAuthenticatedException.class, () -> {
            usersServiceImpl.authenticate("admin", "pass123");
        });

        verify(usersRepository, never()).update(any());
    }
    
}
