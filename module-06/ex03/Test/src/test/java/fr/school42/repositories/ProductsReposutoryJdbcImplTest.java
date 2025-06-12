/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ProductsReposutoryJdbcImplTest.java                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/05 11:28:36 by Younes            #+#    #+#             */
/*   Updated: 2025/06/11 20:42:06 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

// @formatter:off

package fr.school42.repositories;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import fr.school42.models.Product;

public class ProductsReposutoryJdbcImplTest {

    private DataSource dataSource;
    private ProductsRepository repository;

    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = List.of(
        new Product(1L, "Product A", 10.99),
        new Product(2L, "Product B", 15.50),
        new Product(3L, "Product C", 8.75),
        new Product(4L, "Product D", 20.00),
        new Product(5L, "Product E", 5.49)
    );
    final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(2L, "Product B", 15.50);
    final Product EXPECTED_UPDATED_PRODUCT = new Product(3L, "Updated Product", 25.00);

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder()
                         .setType(EmbeddedDatabaseType.HSQL)
                         .addScripts("schema.sql", "data.sql")
                         .build();

        repository = new ProductsReposutoryJdbcImpl(dataSource);
    }

    @Test
    void testFindAll() {

        List<Product> actual = repository.findAll();

        assertEquals(actual.size(), EXPECTED_FIND_ALL_PRODUCTS.size());
        assertTrue(EXPECTED_FIND_ALL_PRODUCTS.containsAll(actual));
    }

    @Test
    void testFindById_Existing() {
        Optional<Product> actual = repository.findById(EXPECTED_FIND_BY_ID_PRODUCT.getId());

        assertTrue(actual.isPresent());
        assertEquals(actual.get(), EXPECTED_FIND_BY_ID_PRODUCT);
    }

    @Test
    void testFindById_NotExisting() {
        Optional<Product> acutal = repository.findById(999L);

        assertTrue(acutal.isEmpty());
    }

    @Test
    void testSave() {

        Product newProduct = new Product(6L, "New Product", 18.1);
        repository.save(newProduct);

        List<Product> products = repository.findAll();
        assertEquals(products.size(), EXPECTED_FIND_ALL_PRODUCTS.size() + 1);
        assertTrue(products.stream().anyMatch(p -> 
            "New Product".equals(p.getName())
        ));
    }

    @Test
    void testUpdate() {

        Product toUpdate = EXPECTED_UPDATED_PRODUCT;

        repository.update(toUpdate);

        Optional<Product> updated = repository.findById(toUpdate.getId());
        assertTrue(updated.isPresent());
        assertEquals(toUpdate, updated.get());
    }

    @Test
    void testDelete() {

        Long idToDelete = 1L;

        repository.delete(idToDelete);

        Optional<Product> deleted = repository.findById(idToDelete);
        assertTrue(deleted.isEmpty());
    }
}
