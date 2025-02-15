package org.Jtech.Repository;

import org.Jtech.Entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository  extends CrudRepository<Category,Integer> {


    @Query(value = "SELECT * FROM category", nativeQuery = true)
    List<Category> getAllCategories();
}
