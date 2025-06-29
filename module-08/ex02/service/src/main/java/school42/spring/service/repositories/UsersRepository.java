/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersRepository.java                               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/26 14:51:02 by Younes            #+#    #+#             */
/*   Updated: 2025/06/26 15:25:00 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.repositories;

import java.util.Optional;

import school42.spring.service.models.User;

public interface UsersRepository extends CrudRepository<User> {

    Optional<User> findByEmail(String email);
}
