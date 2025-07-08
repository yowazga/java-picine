/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   RoomsRepositoryImpl.java                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/04 16:01:56 by Younes            #+#    #+#             */
/*   Updated: 2025/07/04 16:19:26 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import fr.school42.sockets.models.Room;

public class RoomsRepositoryImpl implements RoomsRepository, RowMapper<Room> {

    private JdbcTemplate jdbcTemplate;

    public RoomsRepositoryImpl(DataSource dataSource) {
        
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public Room findById(Long id) {
        
        String sql = "SELECT * FROM rooms WHERE id = ?";

        return jdbcTemplate.query(sql, this, id).stream().findFirst().orElseThrow(null);
    }

    @Override
    public List<Room> findAll() {
        
        String sql = "SELECT * FROM rooms ORDER BY id ASC";
        
        return jdbcTemplate.query(sql, this);
    }

    @Override
    public void save(Room entity) {
        
        String sql = "INSERT INTO rooms (name) VALUES (?)";

        jdbcTemplate.update(sql, entity.getName());
    }

    @Override
    public void update(Room entity) {
        
        String sql = "UPDATE rooms SET name = ? WHERE id = ?";

        jdbcTemplate.update(sql, entity.getName(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        
        String sql = "DELETE FROM rooms WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Room> findByName(String name) {
        
        String sql = "SELECT * FROM rooms WHERE name = ?";
        
        List<Room> rooms = jdbcTemplate.query(sql, this, name);
        
        return rooms.isEmpty() ? Optional.empty() : Optional.of(rooms.get(0));
    }

    @Override
    public Room mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
        
        return new Room(rs.getLong("id"), rs.getString("name"));
    }

}
