/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Car.java                                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/16 14:49:01 by Younes            #+#    #+#             */
/*   Updated: 2025/06/18 14:11:32 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.classes;

public class Car {

    private String model;
    private Integer year;
    private Double price;
    private Boolean isUsed;
    
    public Car() {
        
        this.model = "Default model";
        this.year = 0;
        this.price = 0.0;
        this.isUsed = false;
    }

    public Car(String model, Integer year, Double price, Boolean isUsed) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.isUsed = isUsed;
    }

    public void startEngine() {
        System.out.println("Engine started");
    }

    public Double stopEngine() {
        System.out.println("Engine stopped");
        return this.price;
    }

    @Override
    public String toString() {
        return "Car [model=" + model + ", year=" + year + ", price=" + price + ", isUsed=" + isUsed + "]";
    }

    
}