package org.Jtech.Repository;

import org.Jtech.Entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {

    // Loads product + ingredient lines (already ordered by @OrderBy) + brand + category in one query
    @EntityGraph(attributePaths = {"productIngredients", "brand", "category"})
    Optional<Product> findByBarcode(String barcode);
}
