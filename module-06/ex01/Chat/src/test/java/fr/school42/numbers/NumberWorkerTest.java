/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   NumberWorkerTest.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/19 16:09:13 by Younes            #+#    #+#             */
/*   Updated: 2025/05/29 12:56:03 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.numbers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.school42.numbers.NumberWorker.IllegalNumberException;

public class NumberWorkerTest {
    
    NumberWorker numberWorker;
    
    @BeforeEach
    public void setUp() {
        numberWorker = new NumberWorker();    
    }
    
    @ParameterizedTest
    @ValueSource(ints = {2, 11, 17, 19319})
    @DisplayName("is prime for primes test")
    public void isPrimeForPrimes(int number) {

        assertTrue(numberWorker.isPrime(number));
    }

    @DisplayName("is prime for not primes test")
    @ParameterizedTest
    @ValueSource(ints = {4, 6, 10, 25252})
    public void isPrimeForNotPrimes(int number) {

        assertFalse(numberWorker.isPrime(number));
    }

    @DisplayName("is prime for incorrect numbers test")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, -3})
    public void isPrimeForIncorrectNumbers(int number) {
        
        assertThrows(IllegalNumberException.class, () -> {
           numberWorker.isPrime(number); 
        });
    }
    
    @DisplayName("digit sum is correct test")
    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    public void digitsSumIsCorrect(int number, int expected) {

        assertEquals(numberWorker.digitsSum(number), expected);
        
    }
}