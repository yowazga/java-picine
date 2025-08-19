/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   NumberWorker.java                                  :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/19 16:01:53 by Younes            #+#    #+#             */
/*   Updated: 2025/05/26 17:43:01 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.numbers;

public class NumberWorker {

    public static class IllegalNumberException extends RuntimeException {
        public IllegalNumberException(String message) {super(message);}
    }
    
    public boolean isPrime(int number) {
        
        if (number <= 1) 
            throw new IllegalNumberException("Wrong argument");
        if (number <= 3)
            return true;
        if (number % 2 == 0 || number % 3 == 0)
            return false;
        for (int i = 5; i * i <= number; i +=6) {
            if (number % i == 0 || number % (i + 2) == 0)
                return false;
        }
        return true;
    }
    
    public int digitsSum(int number) {
        
        number = Math.abs(number);

        return String.valueOf(number)
                    .chars()
                    .map(Character::getNumericValue)
                    .sum();
    }
}
