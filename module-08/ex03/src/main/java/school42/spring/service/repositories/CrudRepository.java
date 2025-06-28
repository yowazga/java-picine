/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   CrudRepository.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/26 14:48:45 by Younes            #+#    #+#             */
/*   Updated: 2025/06/26 14:49:37 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.repositories;

import java.util.List;

public interface CrudRepository<T> {

    T findById(Long id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(Long id);
}
