/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   CrudRepository.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:03:30 by Younes            #+#    #+#             */
/*   Updated: 2025/06/29 18:15:04 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.util.List;

public interface CrudRepository<T> {

    T findById(Long id);
    
    List<T> findAll();
    
    void save(T entity);
    
    void update(T entity);
    
    void delete(Long id);
}
