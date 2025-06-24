/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   OrmManager.java                                    :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/23 12:38:45 by Younes            #+#    #+#             */
/*   Updated: 2025/06/24 12:59:40 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import fr.school42.annotations.OrmColumn;
import fr.school42.annotations.OrmColumnId;
import fr.school42.annotations.OrmEntity;

public class OrmManager {

    private final Connection connection;

    public OrmManager(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize ORM", e);
        }
    }

    public void init(Class<?>... classes) throws SQLException {
        
        for (Class<?> clazz : classes) {
            
            if (!clazz.isAnnotationPresent(OrmEntity.class))
                continue ;

            OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
            String tableName = ormEntity.table();

            //Drop table
            String dropSQL = "DROP TABLE IF EXISTS " + tableName + ";";
            connection.createStatement().executeUpdate(dropSQL);
            System.out.println("[SQL] " + dropSQL);

            //Build comums
            List<String> columnDefs = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    
                    columnDefs.add("id INTEGER PRIMARY KEY AUTOINCREMENT");
                } else if (field.isAnnotationPresent(OrmColumn.class)){
                    
                    OrmColumn col = field.getAnnotation(OrmColumn.class);
                    String columName = col.name();
                    String sqlType = toSqlType(field.getType(), col.length());
                    columnDefs.add(columName + " " + sqlType);
                }
            }

            //Create table
            String createSQL = "CREATE TABLE " + tableName + "(\n" + String.join(",\n", columnDefs) + "\n);";
            connection.createStatement().executeUpdate(createSQL);
            System.out.println("[SQL] " + createSQL);
            
        }
    }

    private static String toSqlType(Class<?> type, Integer length) {
        
        if (type == String.class) return "VARCHAR (" + length + ")";
        if (type == Integer.class) return "INT";
        if (type == Double.class) return "DOUBLE";
        if (type == Long.class) return "BIGINT";
        if (type == Boolean.class) return "BOOLEAN";

        throw new RuntimeException("Unsupported type " + type.getName());
    }

    public void save(Object entity) {
        
        Class<?> clazz = entity.getClass();

        if (!clazz.isAnnotationPresent(OrmEntity.class)) {
            throw new RuntimeException("Missing @OrmEntity on class " + clazz.getName());
        }

        OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
        String tabelName = ormEntity.table();

        List<String> columnNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            
            field.setAccessible(true);

            try {
                
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    continue ;
                }
                if (field.isAnnotationPresent(OrmColumn.class)) {
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    columnNames.add(ormColumn.name());
                    values.add(field.get(entity));
                }
            } catch (Exception e) {
                
                throw new RuntimeException("Can't access field value", e);
            }
        }
        //Build SQL
        String sql = "INSERT INTO " + tabelName + 
        " (" + String.join(", ", columnNames) + ")" +
        " VALUES (" + "?,".repeat(values.size()).replaceAll(",$", "") + ");";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            stmt.executeUpdate();
            System.out.println("[SQL] " + sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error drunig INSERT" + e);
        }
    }

    public void update(Object entity) {
        
        Class<?> clazz = entity.getClass();

        if (!clazz.isAnnotationPresent(OrmEntity.class)) {
            throw new RuntimeException("Missing @OrmEntity on class: " + clazz.getName());
        }

        OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
        String tableName = ormEntity.table();

        Object idValue = null;

        List<String> assignments = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {

            field.setAccessible(true);
            try {

                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    idValue = field.get(entity);
                } else if (field.isAnnotationPresent(OrmColumn.class)) {
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    assignments.add(ormColumn.name() + " = ?");
                    values.add(field.get(entity));
                }
                
            } catch (IllegalAccessException e) {
                throw new RuntimeException("can't access field", e);
            }
        }
        if (idValue == null) {
            throw new RuntimeException("ID must not null for update");
        }
        
        // SQL: UPDATE table SET col1 = ?, col2 = ? WHERE id = ?
        String sql = "UPDATE " + tableName +
                    " SET " + String.join(", ", assignments) +
                    " WHERE id = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.setObject(values.size() + 1, idValue);
            stmt.executeUpdate();
            System.out.println("[SQL] " + sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error during update" + e);
        }
    }

    public <T> T findById(Long id, Class<T> clazz) {
        
        if (!clazz.isAnnotationPresent(OrmEntity.class)) {
            throw new RuntimeException("Missing @OrmEntity on class " + clazz.getName());
        }

        OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
        String tableName = ormEntity.table();

        String sql = "SELECT * FROM " + tableName + " WHERE id = ?;";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return null;

            T instance = clazz.getDeclaredConstructor().newInstance();
            
            for (Field field : clazz.getDeclaredFields()) {
                
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    Object value = rs.getObject("id");
                    if (field.getType() == Long.class && value instanceof Integer) {
                        field.set(instance, ((Integer) value).longValue());
                    } else {
                        
                        field.set(instance, value);
                    }
                } else if (field.isAnnotationPresent(OrmColumn.class)) {
                    OrmColumn col = field.getAnnotation(OrmColumn.class);
                    Object colValue = rs.getObject(col.name());
                    field.set(instance, colValue);
                }
            }
            System.out.println("[SQL] " + sql);
            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Error during find " + e);
        }
    }

}
