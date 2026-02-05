package org.Jtech.Service;

import org.Jtech.Entity.Category;
import org.Jtech.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category Service
 *
 * Purpose:
 * Provides business logic for managing and retrieving
 * product and content categories within the application.
 *
 * Scope:
 * - Fetch all available categories
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This service focuses only on category-related operations
 * and does not contain any authentication or user-specific logic.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Retrieve all available categories.
     *
     * Used for:
     * - Displaying category lists (e.g., Personal Care, Food)
     * - Supporting product and content classification
     *
     * @return list of available categories
     */
    public List<Category> getAllCategory() {
        return categoryRepository.getAllCategories();
    }
}
