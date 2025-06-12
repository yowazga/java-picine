/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Product.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/05 11:25:55 by Younes            #+#    #+#             */
/*   Updated: 2025/06/09 01:09:15 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.models;

import java.util.Objects;

public class Product {

    Long   id;
    String name;
    Double price;
    
    
    
    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {return id;}
        
    public void setId(Long id) {this.id = id;}
       
    public String getName() {return name;}
    
    public void setName(String name) {this.name = name;}
        
    public Double getPrice() {return price;}
        
    public void setPrice(Double price) { this.price = price;}
       
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.price);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.price, other.price);
    }
    
    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + "]";
    }
}
