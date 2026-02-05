package org.Jtech.Service;


import org.Jtech.Entity.Article;
import org.Jtech.Repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Article Service
 *
 * Purpose:
 * Manages educational and informational articles displayed in the application,
 * including content related to personal care, food safety, and ingredient awareness
 * (e.g., skin benefits, safety guidelines, and health-related insights).
 *
 * Scope:
 * - Fetch all available articles
 * - Add and manage articles shown in the application
 * - Support categorized and topic-based content delivery
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * Articles served by this service are used to educate users on
 * product usage, safety, and health-related topics. Personalization
 * and recommendation logic, if any, is handled separately.
 */

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Retrieve all available articles.
     *
     * Used for:
     * - Displaying educational content in the application
     * - Showing articles related to personal care and food safety
     *
     * @return list of available articles
     */
    public List<Article> getAllArticle(){

        return articleRepository.getAllArticle();
    }
}
