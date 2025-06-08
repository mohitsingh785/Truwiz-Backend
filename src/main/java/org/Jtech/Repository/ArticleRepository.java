package org.Jtech.Repository;

import org.Jtech.Entity.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository  extends CrudRepository<Article,Long> {

    @Query(value = "Select * from article", nativeQuery = true)
    List<Article> getAllArticle();

}
